package cn.xor7.iseeyou.anticheat.listeners

import cn.xor7.iseeyou.anticheat.AntiCheatListener
import com.gmail.olexorus.themis.api.ActionEvent
import me.rerere.matrix.api.events.PlayerViolationEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class MatrixListener : Listener {
    @EventHandler
    fun onPlayerViolation(e: PlayerViolationEvent) = AntiCheatListener.onAntiCheatAction(e.player)
}