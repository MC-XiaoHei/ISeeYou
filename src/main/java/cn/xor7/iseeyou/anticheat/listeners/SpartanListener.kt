package cn.xor7.iseeyou.anticheat.listeners

import cn.xor7.iseeyou.anticheat.AntiCheatListener
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import me.vagdedes.spartan.api.PlayerViolationEvent
import org.bukkit.event.Listener


class SpartanListener : Listener {
    @EventHandler
    fun onAlert(e: PlayerViolationEvent) = Bukkit.getPlayer(e.player.uniqueId)
        ?.let { AntiCheatListener.onAntiCheatAction(it) }
}