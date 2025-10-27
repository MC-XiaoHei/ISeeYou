package cn.xor7.xiaohei.icu.config

import org.bukkit.entity.Player
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class ConfigModule(
    val filter: FilterConfig = FilterConfig(),
    val path: String = $$"replay/player/${name}@${uuid}",
    val instantReplay: InstantReplayConfig = InstantReplayConfig(),
    val recordSuspicious: RecordSuspiciousPlayerConfig = RecordSuspiciousPlayerConfig(),
    val clearOutdatedRecord: ClearOutdatedRecordConfig = ClearOutdatedRecordConfig(),
    val pauseRecordingOnHighSpeed: HighSpeedPauseConfig = HighSpeedPauseConfig(),
    val deleteTmpFileOnLoad: Boolean = false,
    val photographerPrefix: String = "",
) {
    fun shouldRecordPlayer(player: Player): Boolean {
        val key = player.getKey(filter.checkBy)
        return when (filter.recordMode) {
            RecordMode.BLACKLIST -> !filter.blacklist.contains(key)
            RecordMode.WHITELIST -> filter.whitelist.contains(key)
        }
    }

    init {
        if (path.isBlank()) throw IllegalArgumentException("Replay path cannot be blank")
    }
}

@ConfigSerializable
data class HighSpeedPauseConfig(
    val enable: Boolean = false,
    val threshold: Double = 30.0,
) {
    @delegate:Transient
    val thresholdTicks by lazy { threshold / 20.0 }
}

@ConfigSerializable
data class ClearOutdatedRecordConfig(
    val enable: Boolean = false,
    val interval: Long = 24,
    val days: Long = 7,
) {
    init {
        if (interval < 1) throw IllegalArgumentException("Clear outdated record interval must be at least 1 hour")
        if (days < 1) throw IllegalArgumentException("Clear outdated record days must be at least 1 day")
    }
}

@ConfigSerializable
data class RecordSuspiciousPlayerConfig(
    val themis: Boolean = false,
    val matrix: Boolean = false,
    val vulcan: Boolean = false,
    val negativity: Boolean = false,
    val grim: Boolean = false,
    val lightAntiCheat: Boolean = false,
    val spartan: Boolean = false,
    val length: Long = 5,
    val path: String = $$"replay/suspicious/${name}@${uuid}",
) {
    @delegate:Transient
    val lengthMillis by lazy { length * 60 * 1000L }

    @delegate:Transient
    val enable by lazy { themis || matrix || vulcan || negativity || grim || lightAntiCheat || spartan }

    init {
        if (length < 1) throw IllegalArgumentException("Suspicious replay length must be at least 1 minute")
        if (path.isBlank()) throw IllegalArgumentException("Suspicious replay path cannot be blank")
    }
}

enum class PlayerCheckBy { NAME, UUID }
enum class RecordMode { BLACKLIST, WHITELIST }

@ConfigSerializable
data class FilterConfig(
    val checkBy: PlayerCheckBy = PlayerCheckBy.NAME,
    val recordMode: RecordMode = RecordMode.BLACKLIST,
    val blacklist: Set<String> = emptySet(),
    val whitelist: Set<String> = emptySet(),
)

@ConfigSerializable
data class InstantReplayConfig(
    val enable: Boolean = false,
    val length: Int = 5,
    val interval: Int = 1,
    val path: String = $$"replay/instant/${name}@${uuid}",
) {
    @delegate:Transient
    val lengthTicks by lazy { length * 60 * 20L }

    @delegate:Transient
    val intervalTicks by lazy { interval * 60 * 20L }

    init {
        if (length < 1) throw IllegalArgumentException("Instant replay length must be at least 1 minute")
        if (interval < 1) throw IllegalArgumentException("Instant replay interval must be at least 1 second")
        if (length < interval) throw IllegalArgumentException("Instant replay length must be greater than or equal to interval")
        if (path.isBlank()) throw IllegalArgumentException("Instant replay path cannot be blank")
    }
}

fun Player.getKey(checkBy: PlayerCheckBy): String = when (checkBy) {
    PlayerCheckBy.NAME -> this.name
    PlayerCheckBy.UUID -> this.uniqueId.toString()
}