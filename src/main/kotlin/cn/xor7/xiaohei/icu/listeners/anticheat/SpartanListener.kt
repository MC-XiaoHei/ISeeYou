package cn.xor7.xiaohei.icu.listeners.anticheat

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import me.vagdedes.spartan.api.PlayerViolationEvent
import org.bukkit.event.Listener

class SpartanListener : Listener {
    @EventHandler
    fun onAlert(e: PlayerViolationEvent) = Bukkit.getPlayer(e.player.uniqueId)
        ?.let { antiCheatListener.onAntiCheatAction(it) }
}