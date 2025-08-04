package cn.xor7.xiaohei.icu.listeners.anticheat

import me.vekster.lightanticheat.api.event.LACViolationEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class LightAntiCheatListener : Listener {
    @EventHandler
    fun onFlag(e: LACViolationEvent) = antiCheatListener.onAntiCheatAction(e.player)
}