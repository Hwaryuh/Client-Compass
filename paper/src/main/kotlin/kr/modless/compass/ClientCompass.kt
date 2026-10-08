@file:Suppress("UnstableApiUsage")

package kr.modless.compass

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class ClientCompass : JavaPlugin() {
    private lateinit var session: CompassSession

    override fun onEnable() {
        if (Bukkit.getMinecraftVersion() != "26.3") {
            componentLogger.error("ClientCompass requires Minecraft 26.3")
            server.pluginManager.disablePlugin(this)
            return
        }
        session = CompassSession(WaypointAdapterImpl())
        server.pluginManager.registerEvents(CompassListener(session), this)
        val commands = CompassCommands(session)
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            commands.register(event.registrar())
        }
    }

    override fun onDisable() {
        if (::session.isInitialized) session.shutdown()
    }
}
