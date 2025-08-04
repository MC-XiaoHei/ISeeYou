package cn.xor7.xiaohei.icu.commands

import cn.xor7.xiaohei.icu.utils.loadConfig
import cn.xor7.xiaohei.icu.utils.sendInfo
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.literalArgument

fun registerICUCommand() = commandTree("icu") {
    literalArgument("reload") {
        withPermission("icu.reload")
        anyExecutor { sender, _ ->
            loadConfig()
            sender.sendInfo("ICU plugin config reloaded successfully.")
        }
    }
}