package cn.xor7.xiaohei.icu.listeners.anticheat

import me.rerere.matrix.api.events.PlayerViolationEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MatrixListener : Listener {
    @EventHandler
    fun onPlayerViolation(e: PlayerViolationEvent) = antiCheatListener.onAntiCheatAction(e.player)
}