package cn.xor7.iseeyou;

import cn.xor7.tomlex.TomlEx;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import top.leavesmc.leaves.entity.Photographer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author MC_XiaoHei
 */
public final class ISeeYou extends JavaPlugin {
    @Getter
    private static TomlEx<ConfigData> toml;
    @Getter
    private static Map<String, Photographer> photographers;
    @Getter
    private static Set<Photographer> highSpeedPausedPhotographers;

    @Override

    public void onEnable() {
        setupConfig();
        photographers = new HashMap<>();
        highSpeedPausedPhotographers = new HashSet<>();
        EventListener.setPauseRecordingOnHighSpeedThresholdPerTickSquared(Math.pow(ISeeYou.getToml().data.pauseRecordingOnHighSpeed.threshold / 20, 2));
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
    }

    private void setupConfig() {
        toml = new TomlEx<>("plugins/ISeeYou/config.toml", ConfigData.class);
        String errMsg = toml.data.isConfigValid();
        if (errMsg != null) {
            throw new Error(errMsg);
        }
        toml.data.setConfig();
        toml.save();
    }

    @Override
    public void onDisable() {
        for (Photographer photographer : photographers.values()) {
            photographer.stopRecording();
        }
    }
}
