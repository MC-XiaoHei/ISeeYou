package cn.xor7.iseeyou


import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.plugin.java.JavaPlugin
import top.leavesmc.leaves.entity.Photographer
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.Duration
import java.time.LocalDate
import java.util.*
import kotlin.io.path.isDirectory
import kotlin.math.pow

var toml: TomlEx<ConfigData>? = null
var photographers = mutableMapOf<String, Photographer>()
var highSpeedPausedPhotographers = mutableSetOf<Photographer>()
var outdatedRecordRetentionDays: Int = 0

@Suppress("unused")
class ISeeYou : JavaPlugin(), CommandExecutor {
    override fun onEnable() {
        setupConfig()
        if (toml != null) {
            if (toml!!.data.deleteTmpFileOnLoad) {
                try {
                    Files.walk(Paths.get(toml!!.data.recordPath), Int.MAX_VALUE, FileVisitOption.FOLLOW_LINKS).use { paths ->
                        paths.filter { it.isDirectory() && it.fileName.toString().endsWith(".tmp") }
                                .forEach { deleteTmpFolder(it) }
                    }
                } catch (_: IOException) {
                }
            }
            EventListener.pauseRecordingOnHighSpeedThresholdPerTickSquared =
                    (toml!!.data.pauseRecordingOnHighSpeed.threshold / 20).pow(2.0)
            cleanOutdatedRecordings()
            Bukkit.getPluginManager().registerEvents(EventListener, this)
        } else {
            logger.warning("Failed to initialize configuration. Plugin will not enable.")
            Bukkit.getPluginManager().disablePlugin(this)
        }
    }

    private fun setupConfig() {
        toml = TomlEx("plugins/ISeeYou/config.toml", ConfigData::class.java)
        val errMsg = toml!!.data.isConfigValid()
        if (errMsg != null) {
            throw Error(errMsg)
        }
        toml!!.data.setConfig()
        outdatedRecordRetentionDays = toml!!.data.outdatedRecordRetentionDays
        toml!!.save()
    }

    override fun onDisable() {
        for (photographer in photographers.values) {
            photographer.stopRecording()
        }
        photographers.clear()
        highSpeedPausedPhotographers.clear()
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
            val recordingsDir = Paths.get(".")
            logger.info("Start to delete outdated recordings in $recordingsDir")
            var deletedCount = 0
            Files.walk(recordingsDir).use { paths ->
                paths.filter { Files.isDirectory(it) && it.parent == recordingsDir }
                        .forEach { folder ->
                            deletedCount += deleteRecordingFiles(folder)
                        }
            }
            logger.info("Finished deleting outdated recordings in $recordingsDir, deleted $deletedCount files")
        } catch (e: IOException) {
            logger.severe("Error occurred while cleaning outdated recordings: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun deleteRecordingFiles(folderPath: Path): Int {
        var deletedCount = 0
        try {
            val currentDate = LocalDate.now()
            Files.walk(folderPath).use { paths ->
                paths.filter { Files.isRegularFile(it) && it.toString().endsWith(".mcpr") }
                        .forEach { file ->
                            val fileName = file.fileName.toString()
                            val creationDateStr = fileName.substringBefore('@')
                            val creationDate = LocalDate.parse(creationDateStr)
                            val daysSinceCreation = Duration.between(creationDate.atStartOfDay(), currentDate.atStartOfDay()).toDays()
                            if (daysSinceCreation > outdatedRecordRetentionDays) {
                                try {
                                    Files.delete(file)
                                    logger.info("Deleted recording file: $fileName")
                                    deletedCount++
                                } catch (e: IOException) {
                                    logger.severe("Error occurred while deleting recording file: $fileName, Error: ${e.message}")
                                    e.printStackTrace()
                                }
                            }
                        }
            }
        } catch (e: IOException) {
            logger.severe("Error occurred while processing recording files in folder: $folderPath, Error: ${e.message}")
            e.printStackTrace()
        }
        return deletedCount
    }

}
