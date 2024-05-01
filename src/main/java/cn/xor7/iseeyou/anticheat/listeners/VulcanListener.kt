package cn.xor7.iseeyou.anticheat.listeners

import cn.xor7.iseeyou.anticheat.AntiCheatListener
import com.gmail.olexorus.themis.api.ActionEvent
import me.frep.vulcan.api.event.VulcanPunishEvent
import me.rerere.matrix.api.events.PlayerViolationEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class VulcanListener : Listener {
    @EventHandler
    fun onPunish(e: VulcanPunishEvent) = AntiCheatListener.onAntiCheatAction(e.player)
}