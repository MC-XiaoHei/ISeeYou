package cn.xor7.iseeyou;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import top.leavesmc.leaves.entity.Photographer;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Stream;

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
        if (toml.data.deleteTmpFileOnLoad) {
            try (Stream<Path> walk = Files.walk(Paths.get("replay/"), Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)) {
                walk.filter(Files::isDirectory)
                        .filter(path -> path.getFileName().toString().endsWith(".tmp"))
                        .forEach(ISeeYou::deleteTmpFolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        photographers = new HashMap<>();
        highSpeedPausedPhotographers = new HashSet<>();
        EventListener.setPauseRecordingOnHighSpeedThresholdPerTickSquared(Math.pow(ISeeYou.getToml().data.pauseRecordingOnHighSpeed.threshold / 20, 2));
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
    }

    private static void deleteTmpFolder(Path folderPath) {
        try {
            Files.walkFileTree(folderPath, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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
