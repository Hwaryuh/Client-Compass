package kr.modless.compass

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent

class CompassListener(
    private val session: CompassSession,
) : Listener {
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) = session.disconnect(event.player)

    @EventHandler
    fun onChangedWorld(event: PlayerChangedWorldEvent) = session.stop(event.player)

    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) = session.stop(event.player)
}
