package cn.xor7.iseeyou

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import top.leavesmc.leaves.entity.Photographer
import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import kotlin.io.path.isDirectory
import kotlin.math.pow

var toml: TomlEx<ConfigData>? = null
var photographers = mutableMapOf<String, Photographer>()
var highSpeedPausedPhotographers = mutableSetOf<Photographer>()

@Suppress("unused")
class ISeeYou : JavaPlugin() {
    override fun onEnable() {
        setupConfig()
        if (toml!!.data.deleteTmpFileOnLoad) {
            try {
                Files.walk(Paths.get("replay/"), Int.MAX_VALUE, FileVisitOption.FOLLOW_LINKS).use { paths ->
                    paths.filter { it.isDirectory() && it.fileName.toString().endsWith(".tmp") }
                        .forEach { deleteTmpFolder(it) }
                }
            } catch (_: IOException) {}
        }
        EventListener.pauseRecordingOnHighSpeedThresholdPerTickSquared =
            (toml!!.data.pauseRecordingOnHighSpeed.threshold / 20).pow(2.0)
        Bukkit.getPluginManager().registerEvents(EventListener, this)
    }

    private fun setupConfig() {
        toml = TomlEx("plugins/ISeeYou/config.toml", ConfigData::class.java)
        val errMsg = toml!!.data.isConfigValid()
        if (errMsg != null) {
            throw Error(errMsg)
        }
        toml!!.data.setConfig()
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
                    override fun postVisitDirectory(dir: Path, exc: IOException): FileVisitResult {
                        Files.delete(dir)
                        return FileVisitResult.CONTINUE
                    }
                })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}