package cn.xor7.iseeyou.themis

import cn.xor7.iseeyou.EventListener
import cn.xor7.iseeyou.instance
import cn.xor7.iseeyou.toml
import com.gmail.olexorus.themis.api.ViolationEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

val suspiciousPhotographers: MutableMap<String, SuspiciousPhotographer> = mutableMapOf()

object ThemisListener : Listener {
    init {
        object : BukkitRunnable() {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                suspiciousPhotographers.forEach { (_, susPhotographer) ->
                    if (currentTime - susPhotographer.lastTagged >
                        Duration.ofMinutes(toml!!.data.recordSuspiciousPlayer.recordMinutes).toMillis()
                    ) {
                        susPhotographer.photographer.stopRecording()
                        suspiciousPhotographers.remove(susPhotographer.name)
                    }
                }
            }
        }.runTaskTimer(instance!!, 0, 20 * 60 * 5)
    }

    @EventHandler
    fun onViolation(e: ViolationEvent) {
        val suspiciousPhotographer = suspiciousPhotographers[e.player.name]
        if (suspiciousPhotographer != null) {
            suspiciousPhotographers[e.player.name] =
                suspiciousPhotographer.copy(lastTagged = System.currentTimeMillis())
        } else {
            val photographer = Bukkit
                .getPhotographerManager()
                .createPhotographer(
                    (e.player.name + "_sus_" + UUID.randomUUID().toString().replace("-".toRegex(), ""))
                        .substring(0, 16),
                    e.player.location
                )
            if (photographer == null) {
                throw RuntimeException(
                    "Error on create photographer for player: {name: " + e.player.name + " , UUID:" + e.player.uniqueId + "}"
                )
            }
            photographer.setFollowPlayer(e.player)
            val currentTime = LocalDateTime.now()
            val recordPath: String = toml!!.data.recordSuspiciousPlayer.recordPath
                .replace("\${name}", e.player.name)
                .replace("\${uuid}", e.player.uniqueId.toString())
            File(recordPath).mkdirs()
            val recordFile =
                File(recordPath + "/" + currentTime.format(EventListener.DATE_FORMATTER) + ".mcpr")
            if (recordFile.exists()) {
                recordFile.delete()
            }
            recordFile.createNewFile()
            photographer.setRecordFile(recordFile)
            suspiciousPhotographers[e.player.name] = SuspiciousPhotographer(
                photographer = photographer,
                name = e.player.name,
                lastTagged = System.currentTimeMillis()
            )
        }
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        val suspiciousPhotographer = suspiciousPhotographers[e.player.name]
        if (suspiciousPhotographer != null) {
            suspiciousPhotographer.photographer.stopRecording()
            suspiciousPhotographers.remove(e.player.name)
        }
    }
}