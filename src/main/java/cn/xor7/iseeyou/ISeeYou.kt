package cn.xor7.iseeyou

import cn.xor7.iseeyou.anticheat.AntiCheatListener
import cn.xor7.iseeyou.anticheat.EventListener
import cn.xor7.iseeyou.anticheat.listeners.*
import cn.xor7.iseeyou.anticheat.suspiciousPhotographers
import cn.xor7.iseeyou.utils.ConfigData
import cn.xor7.iseeyou.utils.InstantReplayManager
import cn.xor7.iseeyou.utils.TomlEx
import cn.xor7.iseeyou.utils.metrics.Metrics
import cn.xor7.iseeyou.utils.updatechecker.CompareVersions
import cn.xor7.iseeyou.utils.updatechecker.UpdateChecker
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.command.CommandExecutor
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import org.leavesmc.leaves.entity.Photographer
import java.io.File
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import kotlin.io.path.isDirectory
import kotlin.math.pow

var toml: TomlEx<ConfigData>? = null
var photographers = mutableMapOf<String, Photographer>()
var highSpeedPausedPhotographers = mutableSetOf<Photographer>()
var instance: JavaPlugin? = null

@Suppress("unused")
class ISeeYou : JavaPlugin(), CommandExecutor {
    private var outdatedRecordRetentionDays: Int = 0
    private val commandPhotographersNameUUIDMap = mutableMapOf<String, String>() // Name => UUID

    override fun onLoad() = CommandAPI.onLoad(
        CommandAPIBukkitConfig(this)
            .verboseOutput(false)
            .silentLogs(true)
    )

    override fun onEnable() {
        instance = this
        CommandAPI.onEnable()
        registerCommand()
        setupConfig()

        logInfo("██╗███████╗███████╗███████╗██╗   ██╗ ██████╗ ██╗   ██╗")
        logInfo("██║██╔════╝██╔════╝██╔════╝╚██╗ ██╔╝██╔═══██╗██║   ██║")
        logInfo("██║███████╗█████╗  █████╗   ╚████╔╝ ██║   ██║██║   ██║")
        logInfo("██║╚════██║██╔══╝  ██╔══╝    ╚██╔╝  ██║   ██║██║   ██║")
        logInfo("██║███████║███████╗███████╗   ██║   ╚██████╔╝╚██████╔╝")
        logInfo("╚═╝╚══════╝╚══════╝╚══════╝   ╚═╝    ╚═════╝  ╚═════╝")

        if (toml != null) {
            // 清理临时文件
            if (toml!!.data.deleteTmpFileOnLoad) {
                try {
                    Files.walk(Paths.get(toml!!.data.recordPath), Int.MAX_VALUE, FileVisitOption.FOLLOW_LINKS).use { paths ->
                        paths.filter { it.isDirectory() && it.fileName.toString().endsWith(".tmp") }
                            .forEach { deleteTmpFolder(it) }
                    }
                } catch (_: IOException) {
                    // 忽略异常
                }
            }

            EventListener.pauseRecordingOnHighSpeedThresholdPerTickSquared = (toml!!.data.pauseRecordingOnHighSpeed.threshold / 20).pow(2.0)

            // 清理过期记录文件
            if (toml!!.data.clearOutdatedRecordFile.enabled) {
                cleanOutdatedRecordings()
                var interval = toml!!.data.clearOutdatedRecordFile.interval
                if (interval !in 1..24) {
                    interval = 24
                    logWarning("加载清理间隔参数失败，已重置为默认值24。")
                }
                object : BukkitRunnable() {
                    override fun run() = cleanOutdatedRecordings()
                }.runTaskTimer(this, 0, 20 * 60 * 60 * interval.toLong())
            }

            Bukkit.getPluginManager().registerEvents(EventListener, this)
        } else {
            logError("配置初始化失败，插件无法启用。")
            Bukkit.getPluginManager().disablePlugin(this)
        }

        // bStats 集成
        if (toml!!.data.bStats) {
            val pluginId = 21845
            val metrics: Metrics = Metrics(this, pluginId)
            metrics.addCustomChart(Metrics.SimplePie("chart_id") { "My value" })
        }

        // 注册反作弊监听器
        Bukkit.getPluginManager().registerEvents(AntiCheatListener, this)

        // 注册第三方插件监听器
        registerThirdPartyListeners()

        // 更新检查
        if (toml!!.data.check_for_updates) {
            checkForUpdates()
        }
    }

    private fun registerThirdPartyListeners() {
        if (Bukkit.getPluginManager().isPluginEnabled("Themis") && toml!!.data.recordSuspiciousPlayer.enableThemisIntegration) {
            Bukkit.getPluginManager().registerEvents(ThemisListener(), this)
            logInfo("注册 Themis 监听器...")
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Matrix") && toml!!.data.recordSuspiciousPlayer.enableMatrixIntegration) {
            Bukkit.getPluginManager().registerEvents(MatrixListener(), this)
            logInfo("注册 Matrix 监听器...")
        }

        if (Bukkit.getPluginManager()
                .isPluginEnabled("Vulcan") && toml!!.data.recordSuspiciousPlayer.enableVulcanIntegration
        ) {
            Bukkit.getPluginManager().registerEvents(VulcanListener(), this)
            logInfo("注册 Vulcan 监听器...")
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Negativity") && toml!!.data.recordSuspiciousPlayer.enableNegativityIntegration) {
            Bukkit.getPluginManager().registerEvents(NegativityListener(), this)
            logInfo("注册 Negativity 监听器...")
        }

        if (Bukkit.getPluginManager().isPluginEnabled("GrimAC") && toml!!.data.recordSuspiciousPlayer.enableGrimACIntegration) {
            Bukkit.getPluginManager().registerEvents(GrimACListener(), this)
            logInfo("注册 GrimAC 监听器...")
        }

        if (Bukkit.getPluginManager()
                .isPluginEnabled("LightAntiCheat") && toml!!.data.recordSuspiciousPlayer.enableLightAntiCheatIntegration
        ) {
            Bukkit.getPluginManager().registerEvents(LightAntiCheatListener(), this)
            logInfo("注册 LightAntiCheat 监听器...")
        }

        if (Bukkit.getPluginManager()
                .isPluginEnabled("Spartan") && toml!!.data.recordSuspiciousPlayer.enableSpartanIntegration
        ) {
            Bukkit.getPluginManager().registerEvents(SpartanListener(), this)
            logInfo("注册 Spartan 监听器...")
        }
    }

    private fun checkForUpdates() {
        val updateChecker = UpdateChecker(this, "ISeeYou")
        updateChecker.getVersion { latestVersion: String ->
            val currentVersion = description.version
            val comparisonResult = CompareVersions.compareVersions(currentVersion, latestVersion)
            val logMessage = when {
                comparisonResult < 0 -> {
                    "有新版本可用: $latestVersion\n" +
                            "MineBBS：https://www.minebbs.com/resources/iseeyou.7276/updates\n" +
                            "Hangar：https://hangar.papermc.io/CerealAxis/ISeeYou/versions\n" +
                            "Github：https://github.com/MC-XiaoHei/ISeeYou/releases/"
                }

                comparisonResult == 0 -> "您的插件已经是最新版本！"
                else -> "您可能在使用测试版插件，最新正式版版本为 $latestVersion ！"
            }
            logInfo(logMessage)
        }
    }

    private fun registerCommand() {
        commandTree("photographer") {
            literalArgument("create") {
                stringArgument("name") {
                    playerExecutor { player, args ->
                        val location = player.location
                        val name = args["name"] as String
                        if (name.length !in 4..16) {
                            player.sendMessage("摄像机名称长度必须在4-16之间！")
                            return@playerExecutor
                        }
                        createPhotographer(name, location)
                        player.sendMessage("成功创建摄像机：$name")
                    }
                    locationArgument("location") {
                        anyExecutor { sender, args ->
                            val location = args["location"] as Location
                            val name = args["name"] as String
                            if (name.length !in 4..16) {
                                sender.sendMessage("摄像机名称长度必须在4-16之间！")
                                return@anyExecutor
                            }
                            createPhotographer(name, location)
                            sender.sendMessage("成功创建摄像机：$name")
                        }
                    }
                }
            }
            literalArgument("remove") {
                stringArgument("name") {
                    replaceSuggestions(ArgumentSuggestions.strings(commandPhotographersNameUUIDMap.keys.toList())) // 这不会工作
                    anyExecutor { sender, args ->
                        val name = args["name"] as String
                        val uuid = commandPhotographersNameUUIDMap[name] ?: run {
                            sender.sendMessage("不存在该摄像机！")
                            return@anyExecutor
                        }
                        photographers[uuid]?.stopRecording(toml!!.data.asyncSave)
                        sender.sendMessage("成功移除摄像机：$name")
                    }
                }
            }
            literalArgument("list") {
                anyExecutor { sender, _ ->
                    val photographerNames = commandPhotographersNameUUIDMap.keys.joinToString(", ")
                    sender.sendMessage("摄像机列表：$photographerNames")
                }
            }
        }
        commandTree("instantreplay") {
            playerExecutor { player, _ ->
                if (InstantReplayManager.replay(player)) {
                    player.sendMessage("成功创建即时回放")
                } else {
                    player.sendMessage("操作过快，即时回放创建失败！")
                }
            }
            playerArgument("player") {

            }
        }
    }

    private fun createPhotographer(name: String, location: Location) {
        val photographer = Bukkit
            .getPhotographerManager()
            .createPhotographer(name, location)
        if (photographer == null) throw RuntimeException("创建摄像机失败: $name")
        val uuid = UUID.randomUUID().toString()

        photographer.teleport(location)
        photographers[uuid] = photographer
        commandPhotographersNameUUIDMap[name] = uuid
        val currentTime = LocalDateTime.now()
        val recordPath: String = toml!!.data.recordPath
            .replace("\${name}", "$name@Command")
            .replace("\${uuid}", uuid)
        File(recordPath).mkdirs()
        val recordFile = File(recordPath + "/" + currentTime.format(EventListener.DATE_FORMATTER) + ".mcpr")
        if (recordFile.exists()) recordFile.delete()
        recordFile.createNewFile()
        photographer.setRecordFile(recordFile)
    }

    private fun setupConfig() {
        toml = TomlEx("plugins/ISeeYou/config.toml", ConfigData::class.java)
        val errMsg = toml!!.data.isConfigValid()
        if (errMsg != null) {
            throw InvalidConfigurationException(errMsg)
        }
        toml!!.data.setConfig()
        outdatedRecordRetentionDays = toml!!.data.clearOutdatedRecordFile.days
        toml!!.save()
    }

    private fun deleteTmpFolder(folderPath: Path) {
        try {
            Files.walkFileTree(folderPath, EnumSet.noneOf(FileVisitOption::class.java), Int.MAX_VALUE, object : SimpleFileVisitor<Path>() {
                @Throws(IOException::class)
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    Files.delete(file)
                    return FileVisitResult.CONTINUE
                }

                @Throws(IOException::class)
                override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                    Files.delete(dir)
                    return FileVisitResult.CONTINUE
                }
            })
        } catch (e: IOException) {
            logSevere("删除临时文件夹时出错: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun cleanOutdatedRecordings() {
        try {
            val recordPathA: String = toml!!.data.recordPath
            val recordingsDirA = Paths.get(recordPathA).parent
            val recordingsDirB: Path? =
                if (toml!!.data.recordSuspiciousPlayer.enableMatrixIntegration || toml!!.data.recordSuspiciousPlayer.enableThemisIntegration) {
                    Paths.get(toml!!.data.recordSuspiciousPlayer.recordPath).parent
                } else {
                    null
                }

            logInfo("开始删除过期的记录文件在 $recordingsDirA 和 $recordingsDirB")
            var deletedCount = 0

            deletedCount += deleteFilesInDirectory(recordingsDirA)
            recordingsDirB?.let {
                deletedCount += deleteFilesInDirectory(it)
            }

            logInfo("已删除过期的记录文件，删除了 $deletedCount 个文件")
        } catch (e: IOException) {
            logSevere("清理过期记录时出错: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun deleteFilesInDirectory(directory: Path): Int {
        var count = 0
        Files.walk(directory).use { paths ->
            paths.filter { Files.isDirectory(it) && it.parent == directory }
                .forEach { folder ->
                    count += deleteRecordingFiles(folder)
                }
        }
        return count
    }

    private fun deleteRecordingFiles(folderPath: Path): Int {
        var deletedCount = 0
        var fileCount = 0
        try {
            val currentDate = LocalDate.now()
            Files.walk(folderPath).use { paths ->
                paths.filter { Files.isRegularFile(it) && it.toString().endsWith(".mcpr") }
                    .forEach { file ->
                        fileCount++
                        val fileName = file.fileName.toString()
                        val creationDateStr = fileName.substringBefore('@')
                        val creationDate = LocalDate.parse(creationDateStr)
                        val daysSinceCreation = Duration.between(creationDate.atStartOfDay(), currentDate.atStartOfDay()).toDays()
                        if (daysSinceCreation > outdatedRecordRetentionDays) {
                            val executor = Executors.newSingleThreadExecutor()
                            val future = executor.submit(Callable {
                                try {
                                    Files.delete(file)
                                    logInfo("删除了记录文件: $fileName")
                                    true
                                } catch (e: IOException) {
                                    logSevere("删除记录文件时出错: $fileName, 错误: ${e.message}")
                                    e.printStackTrace()
                                    false
                                }
                            })

                            try {
                                if (future.get(2, TimeUnit.SECONDS)) {
                                    deletedCount++
                                }
                            } catch (e: TimeoutException) {
                                logWarning("删除文件超时: $fileName. 跳过此文件...")
                                future.cancel(true)
                            } finally {
                                executor.shutdown()
                            }
                        }
                    }
            }
            if (fileCount == 0 || deletedCount == 0) {
                logInfo("未找到需要删除的过期记录文件。")
            }
        } catch (e: IOException) {
            logSevere("处理文件夹时出错: $folderPath, 错误: ${e.message}")
            e.printStackTrace()
        }
        return deletedCount
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        for (photographer in photographers.values) {
            photographer.stopRecording(toml!!.data.asyncSave)
        }
        photographers.clear()
        highSpeedPausedPhotographers.clear()
        suspiciousPhotographers.clear()
        instance = null
    }

    private fun logInfo(message: String) {
        logger.info("${ChatColor.GREEN}[INFO] ${ChatColor.RESET}$message")
    }

    private fun logWarning(message: String) {
        logger.warning("${ChatColor.YELLOW}[WARNING] ${ChatColor.RESET}$message")
    }

    private fun logSevere(message: String) {
        logger.severe("${ChatColor.RED}[DANGER] ${ChatColor.RESET}$message")
    }

    private fun logError(message: String) {
        logger.severe("${ChatColor.RED}[ERROR] ${ChatColor.RESET}$message")
    }
}