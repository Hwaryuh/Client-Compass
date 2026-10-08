package kr.modless.compass

import org.bukkit.entity.Player
import java.util.UUID

interface WaypointPacket {
    fun send(player: Player)
}

interface WaypointAdapter {
    fun azimuthPacket(
        id: UUID,
        style: String,
        yawDegrees: Float,
    ): WaypointPacket

    fun removePacket(id: UUID): WaypointPacket
}
