package kr.modless.compass

import io.papermc.paper.command.brigadier.Commands
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

class CompassCommands(
    private val session: CompassSession,
) {
    fun register(registrar: Commands) {
        val command =
            Commands
                .literal("compass")
                .requires { it.sender is Player && it.sender.isOp }
                .then(
                    Commands.literal("on").executes { context ->
                        val player = context.source.sender as Player
                        session.start(player)
                        player.sendMessage(Component.text("Compass activated.", NamedTextColor.WHITE))
                        player.sendMessage(
                            Component.text(
                                "If compass markings aren't showing, wait about 5 seconds.",
                                NamedTextColor.GRAY,
                            ),
                        )
                        1
                    },
                ).then(
                    Commands.literal("off").executes { context ->
                        val player = context.source.sender as Player
                        session.stop(player)
                        player.sendMessage("Compass deactivated.")
                        1
                    },
                ).build()
        registrar.register(command, "Paper Plugin for Client-Compass", listOf("cc"))
    }
}
