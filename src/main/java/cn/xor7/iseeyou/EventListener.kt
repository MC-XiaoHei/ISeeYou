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

/**
 * 事件监听器对象，用于监听玩家加入、移动和退出事件
 */
object EventListener : Listener {
    // 日期格式化器，用于格式化日期时间
    private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ss")
    // 高速移动暂停录制的速度阈值，每 tick 的速度阈值的平方
    var pauseRecordingOnHighSpeedThresholdPerTickSquared = 0.00

    /**
     * 监听玩家加入事件
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    @Throws(IOException::class)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val playerUniqueId = player.uniqueId.toString()
        // 如果不需要记录该玩家的行为，则直接返回
        if (!toml!!.data.shouldRecordPlayer(player)) {
            return
        }
        // 如果配置为玩家退出时暂停录制而不是停止录制，并且该玩家已有摄影师对象，则恢复录制并设置跟随玩家
        if (toml!!.data.pauseInsteadOfStopRecordingOnPlayerQuit && photographers.containsKey(playerUniqueId)) {
            val photographer: Photographer = photographers[playerUniqueId]!!
            photographer.resumeRecording()
            photographer.setFollowPlayer(player)
            return
        }
        var prefix = player.name
        // 如果玩家名长度大于 10，则截取前 10 个字符
        if (prefix.length > 10) {
            prefix = prefix.substring(0, 10)
        }
        // 如果玩家名以 "." 开头，则替换为 "_"
        if (prefix.startsWith(".")) { // fix Floodgate
            prefix = prefix.replace(".", "_")
        }
        // 创建摄影师对象
        val photographer = Bukkit
                .getPhotographerManager()
                .createPhotographer(
                        (prefix + "_" + UUID.randomUUID().toString().replace("-".toRegex(), "")).substring(0, 16),
                        player.location
                )
        // 如果创建失败，则抛出异常
        if (photographer == null) {
            throw RuntimeException(
                    "Error on create photographer for player: {name: " + player.name + " , UUID:" + playerUniqueId + "}"
            )
        }

        val currentTime = LocalDateTime.now()
        // 构建录制文件保存路径
        val recordPath: String = toml!!.data.recordPath
                .replace("\${name}", player.name)
                .replace("\${uuid}", playerUniqueId)
        File(recordPath).mkdirs()
        // 创建录制文件
        val recordFile = File(recordPath + "/" + currentTime.format(DATE_FORMATTER) + ".mcpr")
        if (recordFile.exists()) {
            recordFile.delete()
        }
        recordFile.createNewFile()
        photographer.setRecordFile(recordFile)

        photographers[playerUniqueId] = photographer
        photographer.setFollowPlayer(player)
    }

    /**
     * 监听玩家移动事件
     */
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val photographer: Photographer = photographers[event.player.uniqueId.toString()]!!
        val velocity = event.player.velocity
        // 如果配置为启用高速移动暂停录制，并且玩家速度超过阈值，则暂停录制
        if (toml!!.data.pauseRecordingOnHighSpeed.enabled &&
                velocity.x.pow(2.0) + velocity.z.pow(2.0) > pauseRecordingOnHighSpeedThresholdPerTickSquared &&
                !highSpeedPausedPhotographers.contains(photographer)
        ) {
            photographer.pauseRecording()
            highSpeedPausedPhotographers.add(photographer)
        }
        // 恢复录制并设置跟随玩家
        photographer.resumeRecording()
        photographer.setFollowPlayer(event.player)
        highSpeedPausedPhotographers.remove(photographer)
    }

    /**
     * 监听玩家退出事件
     */
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val photographer: Photographer = photographers[event.player.uniqueId.toString()] ?: return
        highSpeedPausedPhotographers.remove(photographer)
        // 如果配置为玩家退出时暂停录制而不是停止录制，则恢复录制
        if (toml!!.data.pauseInsteadOfStopRecordingOnPlayerQuit) {
            photographer.resumeRecording()
        } else {
            // 否则停止录制并移除摄影师对象
            photographer.stopRecording()
            photographers.remove(event.player.uniqueId.toString())
        }
    }
}
