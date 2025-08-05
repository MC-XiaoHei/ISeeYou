package cn.xor7.xiaohei.icu.listeners

import cn.xor7.xiaohei.icu.utils.module
import cn.xor7.xiaohei.icu.utils.createPhotographer
import cn.xor7.xiaohei.icu.utils.createRecordFile
import cn.xor7.xiaohei.icu.utils.getPhotographerName
import cn.xor7.xiaohei.icu.utils.getRecordPhotographer
import cn.xor7.xiaohei.icu.utils.removePhotographer
import cn.xor7.xiaohei.icu.utils.setFollow
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.leavesmc.leaves.entity.photographer.Photographer

class SimpleRecordListener : Listener {
    private val highSpeedPausedPhotographers = mutableSetOf<Photographer>()

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player

        if (!module.shouldRecordPlayer(player)) return

        val photographer = createSimplePhotographer(player)
        val recordFile = createRecordFile(module.path, player)

        photographer.setRecordFile(recordFile)
        photographer.setFollow(player)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onPlayerMove(event: PlayerMoveEvent) {
        if (!module.pauseRecordingOnHighSpeed.enable) return

        val player = event.player
        val photographer = player.getRecordPhotographer() ?: return

        if (player.velocity.length() > module.pauseRecordingOnHighSpeed.thresholdTicks) {
            if (!highSpeedPausedPhotographers.contains(photographer)) {
                photographer.pauseRecording()
                highSpeedPausedPhotographers.add(photographer)
            }
        } else {
            if (highSpeedPausedPhotographers.remove(photographer)) {
                photographer.resumeRecording()
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val photographer = player.getRecordPhotographer() ?: return

        highSpeedPausedPhotographers.remove(photographer)
        photographer.removePhotographer()
    }

    private fun createSimplePhotographer(player: Player): Photographer =
        createPhotographer(
            player.getPhotographerName(),
            player.location,
        ) ?: throw RuntimeException(
            "Error when create photographer for player: {name:${player.name},UUID:${player.uniqueId}}",
        )
}