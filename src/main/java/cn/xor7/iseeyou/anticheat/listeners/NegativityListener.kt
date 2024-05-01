package cn.xor7.iseeyou.anticheat.listeners

import cn.xor7.iseeyou.anticheat.AntiCheatListener
import com.elikill58.negativity.api.events.negativity.PlayerCheatAlertEvent
import com.gmail.olexorus.themis.api.ActionEvent
import me.rerere.matrix.api.events.PlayerViolationEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class NegativityListener : Listener {
    @EventHandler
    fun onAlert(e: PlayerCheatAlertEvent) = Bukkit.getPlayer(e.player.uniqueId)
        ?.let { AntiCheatListener.onAntiCheatAction(it) }
}