package cn.xor7.xiaohei.icu.utils

import org.bukkit.entity.Player
import java.io.File

fun createRecordFile(path: String, player: Player): File {
    val recordPath: String = path
        .replace($$"${name}", player.name)
        .replace($$"${uuid}", player.uniqueId.toString())

    val recordDir = File(recordPath)
    if (!recordDir.exists() && !recordDir.mkdirs()) throw RuntimeException("Error when create record directory: $recordPath")

    val recordFile = recordDir.resolve("${getNowDateString()}.mcpr")
    if (recordFile.exists()) recordFile.delete()
    if (!recordFile.createNewFile()) throw RuntimeException("Error when create record file: $recordFile")

    return recordFile
}