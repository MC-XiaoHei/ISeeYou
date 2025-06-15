package cn.xor7.iseeyou.utils

import org.bukkit.entity.Player

data class ConfigData(
    var pauseRecordingOnHighSpeed: HighSpeedPauseConfig = HighSpeedPauseConfig(),
    var deleteTmpFileOnLoad: Boolean = true,
    var pauseInsteadOfStopRecordingOnPlayerQuit: Boolean = false,
    var filter: FilterConfig = FilterConfig(),
    var recordPath: String = "replay/player/\${name}@\${uuid}",
    var clearOutdatedRecordFile: OutdatedRecordRetentionConfig = OutdatedRecordRetentionConfig(),
    var recordSuspiciousPlayer: RecordSuspiciousPlayerConfig = RecordSuspiciousPlayerConfig(),
    var instantReplay: InstantReplayConfig = InstantReplayConfig(),
    var asyncSave: Boolean = false,
    var bStats: Boolean = true,
    var recorderNamePrefix: String = "",
    var check_for_updates: Boolean = true,
) {
    fun isConfigValid(): String? {
        if ("name" != filter.checkBy && "uuid" != filter.checkBy) {
            return "invalid checkBy value!"
        }
        if ("blacklist" != filter.recordMode && "whitelist" != filter.recordMode) {
            return "invalid recordMode value!"
        }
        return null
    }

    fun setConfig() {
        if ("blacklist" == filter.recordMode) {
            if (filter.blacklist == null) {
                filter.blacklist = mutableSetOf()
            }
        } else if (filter.whitelist == null) {
            filter.whitelist = mutableSetOf()
        }
    }

    fun shouldRecordPlayer(player: Player): Boolean {
        return if ("blacklist" == filter.recordMode) {
            !containsPlayer(player, filter.blacklist!!)
        } else {
            containsPlayer(player, filter.whitelist!!)
        }
    }

    private fun containsPlayer(player: Player, list: Set<String>): Boolean {
        return if ("name" == filter.checkBy) {
            list.contains(player.name)
        } else {
            list.contains(player.uniqueId.toString())
        }
    }
}

data class HighSpeedPauseConfig(
    var enabled: Boolean = false,
    var threshold: Double = 20.00,
)

data class OutdatedRecordRetentionConfig(
    var enabled: Boolean = false,
    var interval: Int = 24,
    var days: Int = 7,
)

data class RecordSuspiciousPlayerConfig(
    var enableThemisIntegration: Boolean = false,
    var enableMatrixIntegration: Boolean = false,
    var enableVulcanIntegration: Boolean = false,
    var enableNegativityIntegration: Boolean = false,
    var enableGrimACIntegration: Boolean = false,
    var enableLightAntiCheatIntegration: Boolean = false,
    var enableSpartanIntegration: Boolean = false,
    var recordMinutes: Long = 5,
    var recordPath: String = "replay/suspicious/\${name}@\${uuid}",
)

data class FilterConfig(
    var checkBy: String = "name",
    var recordMode: String = "blacklist",
    var blacklist: Set<String>? = null,
    var whitelist: Set<String>? = null,
)

data class InstantReplayConfig(
    var enabled: Boolean = false,
    var replayMinutes: Int = 5,
    var createMinutes: Int = 1,
    var recordPath: String = "replay/instant/\${name}@\${uuid}",
)
