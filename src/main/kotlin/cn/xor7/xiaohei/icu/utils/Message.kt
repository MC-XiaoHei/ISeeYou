package cn.xor7.xiaohei.icu.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandSender

fun CommandSender.sendSuccess(msg: Component) {
    sendMessage(msg.color(NamedTextColor.GREEN))
}

fun CommandSender.sendSuccess(msg: String) {
    sendSuccess(Component.text(msg))
}

fun CommandSender.sendError(msg: Component) {
    sendMessage(msg.color(NamedTextColor.RED))
}

fun CommandSender.sendError(msg: String) {
    sendError(Component.text(msg))
}

fun CommandSender.sendInfo(msg: String) {
    sendMessage(Component.text(msg))
}