package cn.xor7.iseeyou;

import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author MC_XiaoHei
 */
public class ConfigData {
    @Nullable
    public String isConfigValid() {
        if (!"name".equals(checkBy) && !"uuid".equals(checkBy)) {
            return "invalid checkBy value!";
        }
        if (!"blacklist".equals(recordMode) && !"whitelist".equals(recordMode)) {
            return "invalid recordMode value!";
        }
        return null;
    }

    public void setConfig() {
        if ("blacklist".equals(recordMode)) {
            if (blacklist == null) {
                blacklist = new HashSet<>();
            }
        } else if (whitelist == null) {
            whitelist = new HashSet<>();
        }
    }

    public boolean shouldRecordPlayer(Player player) {
        if ("blacklist".equals(recordMode)) {
            return !containsPlayer(player, blacklist);
        } else {
            return containsPlayer(player, whitelist);
        }
    }

    private boolean containsPlayer(Player player, Set<String> list) {
        if ("name".equals(checkBy)) {
            return list.contains(player.getName());
        } else {
            return list.contains(player.getUniqueId().toString());
        }
    }

    public static final class HighSpeedPauseConfig {
        public Boolean enable = false;
        public Double threshold = 20.00;
    }

    public HighSpeedPauseConfig pauseRecordingOnHighSpeed = new HighSpeedPauseConfig();
    public Boolean deleteTmpFileOnLoad = true;
    public Boolean pauseInsteadOfStopRecordingOnPlayerQuit = false;
    public String recordMode = "blacklist";
    public String checkBy = "name";
    public String recordPath = "replay/player/${name}@${uuid}";
    public Set<String> blacklist;
    public Set<String> whitelist;
}
