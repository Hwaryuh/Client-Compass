import kr.modless.compass.CompassSession
import kr.modless.compass.WaypointAdapter
import kr.modless.compass.WaypointPacket
import org.bukkit.entity.Player
import java.lang.reflect.Proxy
import java.util.UUID

object CompassSessionCheck {
    @JvmStatic
    fun main(args: Array<String>) {
        val marks = mutableListOf<Triple<UUID, String, Float>>()
        val sent = mutableListOf<Pair<Player, Boolean>>()
        val adapter =
            object : WaypointAdapter {
                override fun azimuthPacket(
                    id: UUID,
                    style: String,
                    yawDegrees: Float,
                ): WaypointPacket {
                    marks += Triple(id, style, yawDegrees)
                    return object : WaypointPacket {
                        override fun send(player: Player) {
                            sent += player to true
                        }
                    }
                }

                override fun removePacket(id: UUID): WaypointPacket =
                    object : WaypointPacket {
                        override fun send(player: Player) {
                            sent += player to false
                        }
                    }
            }
        val session = CompassSession(adapter)
        check(marks.size == 72 && marks.map { it.first }.toSet().size == 72)
        check(marks.map { it.third } == (0 until 360 step 5).map(Int::toFloat))
        check(marks.single { it.third == 0f }.second == "clientcompass:compass/south")
        check(marks.single { it.third == 90f }.second == "clientcompass:compass/west")
        check(marks.single { it.third == 180f }.second == "clientcompass:compass/north")
        check(marks.single { it.third == 270f }.second == "clientcompass:compass/east")

        val first = player()
        val second = player()
        session.stop(first)
        check(sent.isEmpty())
        session.start(first)
        session.start(second)
        check(sent.count { it.first === first && it.second } == 72)
        check(sent.count { it.first === second && it.second } == 72)
        session.stop(first)
        session.stop(first)
        check(sent.count { it.first === first && !it.second } == 72)
        session.disconnect(second)
        session.shutdown()
        check(sent.none { it.first === second && !it.second })
        session.start(first)
        session.shutdown()
        session.shutdown()
        check(sent.count { it.first === first && !it.second } == 144)
    }

    private fun player(): Player =
        Proxy.newProxyInstance(Player::class.java.classLoader, arrayOf(Player::class.java)) { proxy, method, args ->
            when (method.name) {
                "hashCode" -> System.identityHashCode(proxy)
                "equals" -> proxy === args?.get(0)
                else -> null
            }
        } as Player
}
