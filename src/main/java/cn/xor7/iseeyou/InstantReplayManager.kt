package cn.xor7.iseeyou

import cn.xor7.iseeyou.EventListener.DATE_FORMATTER
import net.jodah.expiringmap.ExpirationPolicy
import net.jodah.expiringmap.ExpiringMap
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import top.leavesmc.leaves.entity.Photographer
import java.io.File
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit

object InstantReplayManager {
    val photographerMap: ExpiringMap<String, Photographer> = ExpiringMap.builder()
        .expiration(toml!!.data.instantReplay.replayMinutes.toLong(), TimeUnit.MINUTES)
        .expirationPolicy(ExpirationPolicy.CREATED)
        .expirationListener { playerUUID: String, photographer: Photographer ->
            recordFileMap[photographer.name]?.delete()
            recordFileMap.remove(photographer.name)
            photographer.stopRecording(toml!!.data.asyncSave, false)
            uuidMap[playerUUID]?.remove(photographer.name)
        }
        .build()
    val uuidMap = mutableMapOf<String, MutableSet<String>>()
    val taskMap = mutableMapOf<String, BukkitTask>()
    val recordFileMap = mutableMapOf<String, File>()

    fun watch(player: Player) {
        object : BukkitRunnable() {
            override fun run() {
                val uuid = UUID.randomUUID().toString()
                uuidMap[player.uniqueId.toString()] =
                    (uuidMap[player.uniqueId.toString()] ?: mutableSetOf()).apply {
                        add(uuid)
                    }
                val photographer = Bukkit
                    .getPhotographerManager()
                    .createPhotographer(UUID.randomUUID().toString(), player.location)?.apply {
                        val currentTime = LocalDateTime.now()
                        val recordPath: String = toml!!.data.instantReplay.recordPath
                            .replace("\${name}", player.name)
                            .replace("\${uuid}", player.uniqueId.toString())
                        File(recordPath).mkdirs()
                        val recordFile = File(recordPath + "/" + currentTime.format(DATE_FORMATTER) + ".mcpr")
                        if (recordFile.exists()) {
                            recordFile.delete()
                        }
                        recordFile.createNewFile()
                        recordFileMap[uuid] = recordFile
                        setRecordFile(recordFile)
                        setFollowPlayer(player)
                    }
                photographerMap[uuid] = photographer
            }
        }.runTaskTimer(
            instance ?: return,
            0,
            (toml!!.data.instantReplay.createPerMinutes * 60 * 1000).toLong()
        ).also { task ->
            taskMap[player.uniqueId.toString()] = task
        }
    }

    fun replay(player: Player): Boolean {
        var firstSubmitUUID = ""
        var firstSubmitTime = Int.MAX_VALUE
        uuidMap[player.uniqueId.toString()]?.forEach { uuid ->
            val expectedExpiration = photographerMap.getExpectedExpiration(uuid).toInt()
            if (firstSubmitTime > expectedExpiration) {
                firstSubmitUUID = uuid
                firstSubmitTime = expectedExpiration
            }
        }
        photographerMap[firstSubmitUUID]?.stopRecording(toml!!.data.asyncSave) ?: return false
        return true
    }
}