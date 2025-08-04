package cn.xor7.xiaohei.icu.listeners.anticheat

import com.gmail.olexorus.themis.api.ActionEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class ThemisListener : Listener {
    @EventHandler
    fun onAction(e: ActionEvent) = antiCheatListener.onAntiCheatAction(e.player)
}