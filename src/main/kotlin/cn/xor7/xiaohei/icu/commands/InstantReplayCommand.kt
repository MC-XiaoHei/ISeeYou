package cn.xor7.xiaohei.icu.commands

import cn.xor7.xiaohei.icu.listeners.instantReplayListener
import cn.xor7.xiaohei.icu.utils.module
import cn.xor7.xiaohei.icu.utils.perms
import cn.xor7.xiaohei.icu.utils.sendError
import cn.xor7.xiaohei.icu.utils.sendSuccess
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.playerArgument
import org.bukkit.entity.Player

fun registerInstantReplayCommand() = commandTree("instantreplay") {
    withPermission(perms.instantReplay.self)
    playerArgument("player", optional = true) {
        anyExecutor { sender, args ->
            if (!module.instantReplay.enable) {
                sender.sendError("Instant Replay is not enabled")
                return@anyExecutor
            }

            val playerArg: Player? by args

            if (playerArg != null && !sender.hasPermission(perms.instantReplay.others)) {
                sender.sendError("You do not have permission to save Instant Replay for other players")
                return@anyExecutor
            }

            val player = playerArg ?: run {
                if (sender is Player) {
                    sender
                } else {
                    sender.sendError("Must specify a target player when use this command in console")
                    return@anyExecutor
                }
            }

            val minutes = instantReplayListener.triggerInstantReplaySave(player)
            if (minutes > 0) {
                sender.sendSuccess("Saved Instant Replay for player '${player.name}' for about $minutes minutes (may not accurate)")
            } else {
                sender.sendError("No Instant Replay data found for player '${player.name}', are you trigger instant replay too fast?")
            }
        }
    }
}