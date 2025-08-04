package cn.xor7.xiaohei.icu.listeners.anticheat

import com.elikill58.negativity.api.events.negativity.PlayerCheatAlertEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class NegativityListener : Listener {
    @EventHandler
    fun onAlert(e: PlayerCheatAlertEvent) = Bukkit.getPlayer(e.player.uniqueId)
        ?.let { antiCheatListener.onAntiCheatAction(it) }
}