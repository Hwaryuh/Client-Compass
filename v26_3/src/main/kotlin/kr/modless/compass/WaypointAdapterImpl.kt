package kr.modless.compass

import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.waypoints.Waypoint
import net.minecraft.world.waypoints.WaypointStyleAssets
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import java.util.Optional
import java.util.UUID

class WaypointAdapterImpl : WaypointAdapter {
    override fun azimuthPacket(
        id: UUID,
        style: String,
        yawDegrees: Float,
    ): WaypointPacket {
        require(yawDegrees.isFinite()) { "Waypoint yaw must be finite" }
        val icon =
            Waypoint.Icon().apply {
                this.style = ResourceKey.create(WaypointStyleAssets.ROOT_ID, Identifier.parse(style))
                color = Optional.of(0xFFFFFF)
            }
        return WaypointPacketImpl(
            ClientboundTrackedWaypointPacket.addWaypointAzimuth(
                id,
                icon,
                Math.toRadians(yawDegrees.toDouble()).toFloat(),
            ),
        )
    }

    override fun removePacket(id: UUID): WaypointPacket =
        WaypointPacketImpl(ClientboundTrackedWaypointPacket.removeWaypoint(id))
}

internal class WaypointPacketImpl(
    val handle: ClientboundTrackedWaypointPacket,
) : WaypointPacket {
    override fun send(player: Player) {
        if (player.isOnline) {
            (player as CraftPlayer).handle.connection.send(handle)
        }
    }
}
