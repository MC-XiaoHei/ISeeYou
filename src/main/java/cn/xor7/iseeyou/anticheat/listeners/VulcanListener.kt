package cn.xor7.iseeyou.anticheat.listeners

import cn.xor7.iseeyou.anticheat.AntiCheatListener
import me.frep.vulcan.api.event.VulcanPunishEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class VulcanListener : Listener {
    @EventHandler
    fun onPunish(e: VulcanPunishEvent) = AntiCheatListener.onAntiCheatAction(e.player)
}