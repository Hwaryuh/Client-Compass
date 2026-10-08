import io.netty.buffer.Unpooled
import kr.modless.compass.WaypointAdapterImpl
import kr.modless.compass.WaypointPacketImpl
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.waypoints.TrackedWaypoint
import net.minecraft.world.waypoints.WaypointStyleAssets
import java.util.UUID
import kotlin.math.PI
import kotlin.math.abs

object WaypointPacketCheck {
    @JvmStatic
    fun main(args: Array<String>) {
        val adapter = WaypointAdapterImpl()
        val id = UUID.fromString("d85e5c5a-cd6b-4ab6-ae51-94de3e30b892")
        val style = "clientcompass:compass/north"
        val expected = mapOf(0f to 0.0, 90f to PI / 2, 180f to PI, 270f to 3 * PI / 2)
        for ((yaw, angle) in expected) {
            val packet = (adapter.azimuthPacket(id, style, yaw) as WaypointPacketImpl).handle
            val buffer = Unpooled.buffer()
            try {
                TrackedWaypoint.STREAM_CODEC.encode(buffer, packet.waypoint())
                check(abs(buffer.getFloat(buffer.writerIndex() - 4) - angle) < 0.000001)
                val decoded = TrackedWaypoint.STREAM_CODEC.decode(buffer)
                check(decoded.id().left().orElseThrow() == id)
                check(decoded.icon().style == ResourceKey.create(WaypointStyleAssets.ROOT_ID, Identifier.parse(style)))
                check(decoded.icon().color.orElseThrow() == -1)
                check(buffer.readableBytes() == 0)
            } finally {
                buffer.release()
            }
        }
        val removed = (adapter.removePacket(id) as WaypointPacketImpl).handle
        val buffer = Unpooled.buffer()
        try {
            TrackedWaypoint.STREAM_CODEC.encode(buffer, removed.waypoint())
            val decoded = TrackedWaypoint.STREAM_CODEC.decode(buffer)
            check(decoded.id().left().orElseThrow() == id)
            check(buffer.readableBytes() == 0)
        } finally {
            buffer.release()
        }
        check(runCatching { adapter.azimuthPacket(id, style, Float.NaN) }.exceptionOrNull() is IllegalArgumentException)
    }
}
