package cn.xor7.iseeyou.matrix

import cn.xor7.iseeyou.EventListener
import cn.xor7.iseeyou.instance
import cn.xor7.iseeyou.toml
import me.rerere.matrix.api.MatrixAPI
import me.rerere.matrix.api.MatrixAPIProvider
import me.rerere.matrix.api.events.PlayerViolationEvent

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable
import java.io.File
import java.time.Duration
import java.time.LocalDateTime
import java.util.*

val matrixSuspiciousPhotographers: MutableMap<String, MatrixSuspiciousPhotographer> = mutableMapOf()

object MatrixListener : Listener {
    val api: MatrixAPI = MatrixAPIProvider.getAPI()

    init {
        object : BukkitRunnable() {
            override fun run() {
                val currentTime = System.currentTimeMillis()
                matrixSuspiciousPhotographers.forEach { (_, susPhotographer) ->
                    if (currentTime - susPhotographer.lastTagged >
                        Duration.ofMinutes(toml!!.data.recordMatrixSuspiciousPlayer.recordMinutes).toMillis()
                    ) {
                        susPhotographer.photographer.stopRecording()
                        matrixSuspiciousPhotographers.remove(susPhotographer.name)
                    }
                }
            }
        }.runTaskTimer(instance!!, 0, 20 * 60 * 5)
    }

    @EventHandler
    fun onPlayerViolation(e: PlayerViolationEvent) {
        val playerName = e.player.name
        val suspiciousPhotographer = matrixSuspiciousPhotographers[playerName]
        if (suspiciousPhotographer != null) {
            matrixSuspiciousPhotographers[e.player.name] =
                suspiciousPhotographer.copy(lastTagged = System.currentTimeMillis())
        } else {
            val runnable = object : BukkitRunnable() {
                override fun run() {
                    var prefix = e.player.name
                    if (prefix.length > 10) {
                        prefix = prefix.substring(0, 10)
                    }
                    if (prefix.startsWith(".")) {
                        prefix = prefix.replace(".", "_")
                    }
                    val photographer = Bukkit
                        .getPhotographerManager()
                        .createPhotographer(
                            (prefix + "_" + UUID.randomUUID().toString().replace("-".toRegex(), "")).substring(0, 16),
                            e.player.location
                        )
                    if (photographer == null) {
                        throw RuntimeException(
                            "Error on create photographer for player: {name: " + e.player.name + " , UUID:" + e.player.uniqueId + "}"
                        )
                    }
                    photographer.setFollowPlayer(e.player)
                    val currentTime = LocalDateTime.now()
                    val recordPath: String = toml?.data?.recordMatrixSuspiciousPlayer?.recordPath
                        ?.replace("\${name}", e.player.name)
                        ?.replace("\${uuid}", e.player.uniqueId.toString()) ?: ""
                    File(recordPath).mkdirs()
                    val recordFile =
                        File(recordPath + "/" + currentTime.format(EventListener.DATE_FORMATTER) + ".mcpr")
                    if (recordFile.exists()) {
                        recordFile.delete()
                    }
                    recordFile.createNewFile()
                    photographer.setRecordFile(recordFile)
                    matrixSuspiciousPhotographers[e.player.name] = MatrixSuspiciousPhotographer(
                        photographer = photographer,
                        name = e.player.name,
                        lastTagged = System.currentTimeMillis()
                    )
                }
            }
            runnable.runTask(instance!!)
        }
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        val suspiciousPhotographer = matrixSuspiciousPhotographers[e.player.name]
        if (suspiciousPhotographer != null) {
            suspiciousPhotographer.photographer.stopRecording()
            matrixSuspiciousPhotographers.remove(e.player.name)
        }
    }
}
