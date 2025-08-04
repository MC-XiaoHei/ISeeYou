package cn.xor7.xiaohei.icu.listeners.anticheat

import ac.grim.grimac.api.event.events.FlagEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GrimACListener : Listener {
    @EventHandler
    fun onFlag(e: FlagEvent) = Bukkit.getPlayer(e.player.uniqueId)
        ?.let { antiCheatListener.onAntiCheatAction(it) }
}