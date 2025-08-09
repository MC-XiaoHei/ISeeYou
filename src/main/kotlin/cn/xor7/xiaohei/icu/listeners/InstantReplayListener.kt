package cn.xor7.xiaohei.icu.listeners

import cn.xor7.xiaohei.icu.plugin
import cn.xor7.xiaohei.icu.utils.createPhotographer
import cn.xor7.xiaohei.icu.utils.createRecordFile
import cn.xor7.xiaohei.icu.utils.module
import cn.xor7.xiaohei.icu.utils.removePhotographer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.leavesmc.leaves.entity.photographer.Photographer
import java.util.*
import java.util.UUID.randomUUID

lateinit var instantReplayListener: InstantReplayListener

class InstantReplayListener : Listener {
    val instantReplayPhotographers = mutableMapOf<UUID, MutableSet<Photographer>>()

    init {
        instantReplayListener = this
        Bukkit.getPluginManager().registerEvents(this, plugin)
        Bukkit.getGlobalRegionScheduler().runAtFixedRate(
            plugin,
            {
                if (!module.instantReplay.enable) return@runAtFixedRate
                Bukkit.getOnlinePlayers()
                    .filter(Player::isOnline)
                    .forEach { player ->
                        triggerOneInstantReplayPhotographer(player)
                    }
            },
            1, module.instantReplay.intervalTicks,
        )
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (!instantReplayPhotographers[player.uniqueId].isNullOrEmpty()) return
        triggerOneInstantReplayPhotographer(player)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        instantReplayPhotographers[player.uniqueId]?.forEach { it.removePhotographer(false) }
        instantReplayPhotographers.remove(player.uniqueId)
    }

    fun triggerInstantReplaySave(player: Player): Int {
        if (!module.instantReplay.enable) return 0
        val photographers = instantReplayPhotographers[player.uniqueId]
        if (photographers.isNullOrEmpty()) return 0
        val photographer = photographers.first()
        val minutes = photographers.size
        photographers.remove(photographer)
        photographer.removePhotographer()
        return minutes * module.instantReplay.interval
    }

    private fun triggerOneInstantReplayPhotographer(player: Player) {
        val photographer = createOneInstantReplayPhotographer(player)
        val recordFile = createRecordFile(module.instantReplay.path, player)

        photographer.setRecordFile(recordFile)
        photographer.setFollowPlayer(player)
        instantReplayPhotographers
            .computeIfAbsent(player.uniqueId) { LinkedHashSet() }
            .add(photographer)

        photographer.scheduler.runDelayed(
            plugin,
            {
                photographer.removePhotographer(false)
                instantReplayPhotographers[player.uniqueId]?.remove(photographer)
            },
            {},
            module.instantReplay.lengthTicks,
        )
    }

    private fun createOneInstantReplayPhotographer(player: Player): Photographer =
        createPhotographer(
            randomUUID().toString().replace("-", "").substring(0, 16),
            player.location,
        ) ?: throw RuntimeException(
            "Error when create instant replay photographer for player: {name:${player.name},UUID:${player.uniqueId}}",
        )
}