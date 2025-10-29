package cn.xor7.xiaohei.icu.utils

import cn.xor7.xiaohei.icu.plugin
import io.papermc.paper.threadedregions.scheduler.ScheduledTask
import org.bukkit.Bukkit
import java.io.IOException
import java.nio.file.FileVisitOption
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.BasicFileAttributes
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import kotlin.io.path.deleteRecursively
import kotlin.io.path.isDirectory

fun tryRemoveTempFile() {
    if (!module.deleteTmpFileOnLoad) return
    Files.walk(Paths.get("./replay"), 5, FileVisitOption.FOLLOW_LINKS).use { paths ->
        paths.filter { it.isDirectory() }
            .filter { it.fileName.toString().endsWith(".tmp") }
            .forEach { it.deleteRecursively() }
    }
}

fun scheduleDeleteOutdateFiles(delayHours: Long = 0): ScheduledTask = Bukkit.getAsyncScheduler().runDelayed(
    plugin,
    {
        tryDeleteOutdateFiles()
        scheduleDeleteOutdateFiles(module.clearOutdatedRecord.interval)
    },
    delayHours, TimeUnit.HOURS,
)

fun tryDeleteOutdateFiles() {
    if (!module.clearOutdatedRecord.enable) return
    listOf(
        module.path,
        module.recordSuspicious.path,
        module.instantReplay.path,
    )
        .map { Paths.get(it).parent }
        .filter { it != null }
        .forEach { deleteOutdateFilesIn(it) }
}

private fun deleteOutdateFilesIn(directory: Path) {
    val cutoffTime = Instant.now().minus(module.clearOutdatedRecord.days, ChronoUnit.DAYS)
    deleteFilesOlderThan(directory, cutoffTime)
}

private fun deleteFilesOlderThan(directory: Path, cutoffTime: Instant) {
    if (!Files.isDirectory(directory)) {
        throw IllegalArgumentException("The provided path is not a directory: $directory")
    }

    try {
        Files.walk(directory).use { paths ->
            paths.filter { Files.isRegularFile(it) }
                .filter { it.fileName.toString().endsWith(".mcpr") }
                .filter { file ->
                    val attrs = Files.readAttributes(file, BasicFileAttributes::class.java)
                    attrs.lastModifiedTime().toInstant().isBefore(cutoffTime)
                }
                .forEach { file ->
                    try {
                        Files.delete(file)
                        val parent = file.parent ?: return@forEach
                        if (
                            Files.isDirectory(parent) &&
                            Files.newDirectoryStream(parent).use { !it.iterator().hasNext() }
                        ) {
                            Files.delete(parent)
                        }
                    } catch (e: IOException) {
                        plugin.logger.warning("Failed to delete file: $file. Error: ${e.message}")
                    }
                }
        }
    } catch (e: IOException) {
        plugin.logger.warning("Error while accessing directory: $directory. Error: ${e.message}")
    }
}
