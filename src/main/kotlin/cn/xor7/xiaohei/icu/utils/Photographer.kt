package cn.xor7.xiaohei.icu.utils

import cn.xor7.xiaohei.icu.plugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.leavesmc.leaves.entity.photographer.Photographer
import org.leavesmc.leaves.replay.BukkitRecorderOption
import java.util.UUID
import java.util.UUID.randomUUID
import kotlin.collections.set

val photographers = mutableSetOf<UUID>()
val player2PhotographerMap = mutableMapOf<UUID, Photographer>()
val allPhotographers: Collection<Photographer>
    get() = Bukkit.getPhotographerManager().photographers

fun getPhotographer(id: String): Photographer? = Bukkit.getPhotographerManager().getPhotographer(id)

fun getPhotographer(uuid: UUID): Photographer? = Bukkit.getPhotographerManager().getPhotographer(uuid)

fun createPhotographer(
    id: String,
    location: Location,
    recorderOption: BukkitRecorderOption = BukkitRecorderOption()
): Photographer? = Bukkit.getPhotographerManager().createPhotographer(id, location, recorderOption).also {
    it?.let { photographers.add(it.uniqueId) }
}

fun Photographer.remove(save: Boolean = true) {
    try {
        this.stopRecording(true, save)
    } catch (e: Exception) {
        plugin.logger.warning("Err while removing photographer ${this.name}: ${e.message}")
    }
    photographers.remove(this.uniqueId)
    player2PhotographerMap.entries.removeIf { it.value.uniqueId == this.uniqueId }
}

fun removeAllPhotographers() = photographers.forEach {
    val photographer = getPhotographer(it)
    photographer?.remove(false)
}

fun Photographer.setFollow(player: Player) {
    player2PhotographerMap[player.uniqueId] = this
    this.setFollowPlayer(player)
}

fun Player.getRecordPhotographer(): Photographer? {
    return player2PhotographerMap[this.uniqueId]
}

fun Player.getPhotographerName(type: String = ""): String {
    val trimmedPlayerName = this.name
        .replace(".", "_")
        .replace("-", "_")
    val leftPart = (module.photographerPrefix + trimmedPlayerName).run {
        if (length > 6) substring(0, 6) else this
    }
    val rightPart = type + randomUUID().toString().replace("-", "")
    return "${leftPart}_${rightPart}".run {
        if (length > 16) substring(0, 16) else this
    }
}