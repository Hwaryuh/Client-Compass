package kr.modless.compass

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet
import org.bukkit.entity.Player
import java.util.UUID

class CompassSession(
    adapter: WaypointAdapter,
) {
    private val players = ObjectOpenHashSet<Player>()
    private val ids =
        (0 until 360 step 5).map { yaw ->
            UUID.nameUUIDFromBytes("clientcompass:demo:$yaw".toByteArray(Charsets.UTF_8))
        }
    private val additions =
        ids.mapIndexed { index, id ->
            val yaw = index * 5
            val style =
                when (yaw) {
                    0 -> "south"
                    90 -> "west"
                    180 -> "north"
                    270 -> "east"
                    else -> if (yaw % 30 == 0) "major" else "minor"
                }
            adapter.azimuthPacket(id, "clientcompass:compass/$style", yaw.toFloat())
        }
    private val removals = ids.map(adapter::removePacket)

    fun start(player: Player) {
        additions.forEach { it.send(player) }
        players.add(player)
    }

    fun stop(player: Player) {
        if (!players.remove(player)) return
        removals.forEach { it.send(player) }
    }

    fun disconnect(player: Player) {
        players.remove(player)
    }

    fun shutdown() {
        players.forEach { player -> removals.forEach { it.send(player) } }
        players.clear()
    }
}
