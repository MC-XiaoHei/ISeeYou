package cn.xor7.xiaohei.icu.listeners.anticheat

import me.frep.vulcan.api.event.VulcanPunishEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class VulcanListener : Listener {
    @EventHandler
    fun onPunish(e: VulcanPunishEvent) = antiCheatListener.onAntiCheatAction(e.player)
}