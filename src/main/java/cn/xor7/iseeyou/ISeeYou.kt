package cn.xor7.iseeyou

import cn.xor7.iseeyou.anticheat.AntiCheatListener
import cn.xor7.iseeyou.anticheat.listeners.MatrixListener
import cn.xor7.iseeyou.anticheat.listeners.ThemisListener
import cn.xor7.iseeyou.anticheat.suspiciousPhotographers
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.command.CommandExecutor
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import top.leavesmc.leaves.entity.Photographer
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
        if (toml != null) {
            if (toml!!.data.deleteTmpFileOnLoad) {
                try {
                    Files.walk(Paths.get(toml!!.data.recordPath), Int.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)
                        .use { paths ->
                            paths.filter { it.isDirectory() && it.fileName.toString().endsWith(".tmp") }
                                .forEach { deleteTmpFolder(it) }
                        }
                } catch (_: IOException) {
                }
            }
            EventListener.pauseRecordingOnHighSpeedThresholdPerTickSquared =
                (toml!!.data.pauseRecordingOnHighSpeed.threshold / 20).pow(2.0)
            if (toml!!.data.clearOutdatedRecordFile.enabled) {
                cleanOutdatedRecordings()
                var interval = toml!!.data.clearOutdatedRecordFile.interval
                if (interval !in 1..24 ) {
                    interval = 24
                    logger.warning("Failed to load the interval parameter, reset to the default value of 24.")
                }
                object : BukkitRunnable() {
                    override fun run() = cleanOutdatedRecordings()
                }.runTaskTimer(this, 0, 20 * 60 * 60 * (interval.toLong()))
            }
            Bukkit.getPluginManager().registerEvents(EventListener, this)
        } else {
            logger.warning("Failed to initialize configuration. Plugin will not enable.")
            Bukkit.getPluginManager().disablePlugin(this)
        }

        Bukkit.getPluginManager().registerEvents(AntiCheatListener, this)

        if (Bukkit.getPluginManager().isPluginEnabled("Themis") ||
            toml!!.data.recordSuspiciousPlayer.enableThemisIntegration
        ) Bukkit.getPluginManager().registerEvents(ThemisListener(), this)

        if (Bukkit.getPluginManager().isPluginEnabled("Matrix") ||
            toml!!.data.recordSuspiciousPlayer.enableMatrixIntegration
        ) Bukkit.getPluginManager().registerEvents(MatrixListener(), this)
    }

    private fun registerCommand() {
        commandTree("photographer") {
            literalArgument("create") {
                stringArgument("name") {
                    playerExecutor { player, args ->
                        val location = player.location
                        val name = args["name"] as String
                        if (name.length !in 4..16) {
                            player.sendMessage("§4摄像机名称长度必须在4-16之间！")
                            return@playerExecutor
                        }
                        createPhotographer(name, location)
                        player.sendMessage("§a成功创建摄像机：$name")
                    }
                    locationArgument("location") {
                        anyExecutor { sender, args ->
                            val location = args["location"] as Location
                            val name = args["name"] as String
                            if (name.length !in 4..16) {
                                sender.sendMessage("§4摄像机名称长度必须在4-16之间！")
                                return@anyExecutor
                            }
                            createPhotographer(name, location)
                            sender.sendMessage("§a成功创建摄像机：$name")
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
                            sender.sendMessage("§4不存在该摄像机！")
                            return@anyExecutor
                        }
                        photographers[uuid]?.stopRecording()
                        sender.sendMessage("§a成功移除摄像机：$name")
                    }
                }
            }
            literalArgument("list") {
                anyExecutor { sender, _ ->
                    val photographerNames = commandPhotographersNameUUIDMap.keys.joinToString(", ")
                    sender.sendMessage("§a摄像机列表：$photographerNames")
                }
            }
        }
    }

    private fun createPhotographer(name: String, location: Location) {
        val photographer = Bukkit
            .getPhotographerManager()
            .createPhotographer(name, location)
        if (photographer == null) throw RuntimeException("Error on create photographer $name")
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

    override fun onDisable() {
        CommandAPI.onDisable()
        for (photographer in photographers.values) {
            photographer.stopRecording()
        }
        photographers.clear()
        highSpeedPausedPhotographers.clear()
        suspiciousPhotographers.clear()
        instance = null
    }

    private fun deleteTmpFolder(folderPath: Path) {
        try {
            Files.walkFileTree(
                folderPath,
                EnumSet.noneOf(FileVisitOption::class.java),
                Int.MAX_VALUE,
                object : SimpleFileVisitor<Path>() {
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
            e.printStackTrace()
        }
    }

    private fun cleanOutdatedRecordings() {
        try {
            val recordPathA: String = toml!!.data.recordPath
            val recordPathB: String = toml!!.data.recordSuspiciousPlayer.recordPath
            val recordingsDirA = Paths.get(recordPathA).parent
            val recordingsDirB = Paths.get(recordPathB).parent

            logger.info("Start to delete outdated recordings in $recordingsDirA and $recordingsDirB")
            var deletedCount = 0

            Files.walk(recordingsDirA).use { paths ->
                paths.filter { Files.isDirectory(it) && it.parent == recordingsDirA }
                    .forEach { folder ->
                        deletedCount += deleteRecordingFiles(folder)
                    }
            }

            Files.walk(recordingsDirB).use { paths ->
                paths.filter { Files.isDirectory(it) && it.parent == recordingsDirB }
                    .forEach { folder ->
                        deletedCount += deleteRecordingFiles(folder)
                    }
            }

            logger.info("Finished deleting outdated recordings, deleted $deletedCount files")
        } catch (e: IOException) {
            logger.severe("Error occurred while cleaning outdated recordings: ${e.message}")
            e.printStackTrace()
        }
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
                        val daysSinceCreation =
                            Duration.between(creationDate.atStartOfDay(), currentDate.atStartOfDay()).toDays()
                        if (daysSinceCreation > outdatedRecordRetentionDays) {
                            val executor = Executors.newSingleThreadExecutor()
                            val future = executor.submit(Callable {
                                try {
                                    Files.delete(file)
                                    logger.info("Deleted recording file: $fileName")
                                    true
                                } catch (e: IOException) {
                                    logger.severe("Error occurred while deleting recording file: $fileName, Error: ${e.message}")
                                    e.printStackTrace()
                                    false
                                }
                            })

                            try {
                                if (future.get(2, TimeUnit.SECONDS)) {
                                    deletedCount++
                                }
                            } catch (e: TimeoutException) {
                                logger.warning("Timeout deleting file: $fileName. Skipping...")
                                future.cancel(true)
                            } finally {
                                executor.shutdown()
                            }
                        }
                    }
            }
            if (fileCount == 0 || deletedCount == 0) {
                logger.info("No outdated recording files found to delete.")
            }
        } catch (e: IOException) {
            logger.severe("Error occurred while processing recording files in folder: $folderPath, Error: ${e.message}")
            e.printStackTrace()
        }
        return deletedCount
    }

}
