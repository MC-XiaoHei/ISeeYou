package cn.xor7.iseeyou

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerQuitEvent
import top.leavesmc.leaves.entity.Photographer
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.pow

object EventListener : Listener {
    private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ss")
    var pauseRecordingOnHighSpeedThresholdPerTickSquared = 0.00

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    @Throws(IOException::class)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val playerUniqueId = player.uniqueId.toString()
        if (!toml!!.data.shouldRecordPlayer(player)) {
            return
        }
        if (toml!!.data.pauseInsteadOfStopRecordingOnPlayerQuit && photographers.containsKey(playerUniqueId)) {
            val photographer: Photographer = photographers[playerUniqueId]!!
            photographer.resumeRecording()
            photographer.setFollowPlayer(player)
            return
        }
        var prefix = player.name
        if (prefix.length > 10) {
            prefix = prefix.substring(0, 10)
        }
        if (prefix.startsWith(".")) { // fix Floodgate
            prefix = prefix.replace(".", "_")
        }
        val photographer = Bukkit
            .getPhotographerManager()
            .createPhotographer(
                (prefix + "_" + UUID.randomUUID().toString().replace("-".toRegex(), "")).substring(0, 16),
                player.location
            )
        if (photographer == null) {
            throw RuntimeException(
                "Error on create photographer for player: {name: " + player.name + " , UUID:" + playerUniqueId + "}"
            )
        }

        val currentTime = LocalDateTime.now()
        val recordPath: String = toml!!.data.recordPath
            .replace("\${name}", player.name)
            .replace("\${uuid}", playerUniqueId)
        File(recordPath).mkdirs()
        val recordFile = File(recordPath + "/" + currentTime.format(DATE_FORMATTER) + ".mcpr")
        if (recordFile.exists()) {
            recordFile.delete()
        }
        recordFile.createNewFile()
        photographer.setRecordFile(recordFile)

        photographers[playerUniqueId] = photographer
        photographer.setFollowPlayer(player)
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val photographer: Photographer = photographers[event.player.uniqueId.toString()]!!
        val velocity = event.player.velocity
        if (toml!!.data.pauseRecordingOnHighSpeed.enabled &&
            velocity.x.pow(2.0) + velocity.z.pow(2.0) > pauseRecordingOnHighSpeedThresholdPerTickSquared &&
            !highSpeedPausedPhotographers.contains(photographer)
        ) {
            photographer.pauseRecording()
            highSpeedPausedPhotographers.add(photographer)
        }
        photographer.resumeRecording()
        photographer.setFollowPlayer(event.player)
        highSpeedPausedPhotographers.remove(photographer)
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val photographer: Photographer = photographers[event.player.uniqueId.toString()] ?: return
        highSpeedPausedPhotographers.remove(photographer)
        if (toml!!.data.pauseInsteadOfStopRecordingOnPlayerQuit) {
            photographer.resumeRecording()
        } else {
            // photographer.stopRecording(toml!!.data.asyncSave)
            photographer.stopRecording()
            photographers.remove(event.player.uniqueId.toString())
        }
    }
}