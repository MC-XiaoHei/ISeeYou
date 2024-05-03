package cn.xor7.iseeyou.anticheat.listeners

import ac.grim.grimac.api.events.FlagEvent
import cn.xor7.iseeyou.anticheat.AntiCheatListener
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GrimACListener : Listener {
    @EventHandler
    fun onFlag(e: FlagEvent) = Bukkit.getPlayer(e.player.uniqueId)
        ?.let { AntiCheatListener.onAntiCheatAction(it) }
}