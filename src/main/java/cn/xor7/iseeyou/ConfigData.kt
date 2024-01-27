package cn.xor7.iseeyou

import org.bukkit.entity.Player

data class ConfigData(
    var pauseRecordingOnHighSpeed: HighSpeedPauseConfig = HighSpeedPauseConfig(),
    var deleteTmpFileOnLoad: Boolean = true,
    var pauseInsteadOfStopRecordingOnPlayerQuit: Boolean = false,
    var recordMode: String = "blacklist",
    var checkBy: String = "name",
    var recordPath: String = "replay/player/\${name}@\${uuid}",
    var blacklist: Set<String>? = null,
    var whitelist: Set<String>? = null,
    // var asyncSave: Boolean = false,
) {
    fun isConfigValid(): String? {
        if ("name" != checkBy && "uuid" != checkBy) {
            return "invalid checkBy value!"
        }
        if ("blacklist" != recordMode && "whitelist" != recordMode) {
            return "invalid recordMode value!"
        }
        return null
    }

    fun setConfig() {
        if ("blacklist" == recordMode) {
            if (blacklist == null) {
                blacklist = mutableSetOf()
            }
        } else if (whitelist == null) {
            whitelist = mutableSetOf()
        }
    }

    fun shouldRecordPlayer(player: Player): Boolean {
        return if ("blacklist" == recordMode) {
            !containsPlayer(player, blacklist!!)
        } else {
            containsPlayer(player, whitelist!!)
        }
    }

    private fun containsPlayer(player: Player, list: Set<String>): Boolean {
        return if ("name" == checkBy) {
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
