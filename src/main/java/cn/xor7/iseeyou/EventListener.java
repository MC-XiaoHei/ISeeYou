package cn.xor7.iseeyou;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import top.leavesmc.leaves.entity.Photographer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author MC_XiaoHei
 */
public class EventListener implements Listener {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ss");

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
        String playerUniqueId = player.getUniqueId().toString();
        if (!ISeeYou.getToml().data.shouldRecordPlayer(player)) {
            return;
        }
        if (ISeeYou.getToml().data.pauseInsteadOfStopRecordingOnPlayerQuit && ISeeYou.getPhotographers().containsKey(playerUniqueId)) {
            Photographer photographer = ISeeYou.getPhotographers().get(playerUniqueId);
            photographer.resumeRecording();
            photographer.setFollowPlayer(player);
            return;
        }
        String prefix = player.getName();
        if (prefix.length() > 10) {
            prefix = prefix.substring(0, 10);
        }
        if (prefix.startsWith(".")) { // fix Floodgate
            prefix = prefix.replace(".", "_");
        }
        Photographer photographer = Bukkit
                .getPhotographerManager()
                .createPhotographer(
                        (prefix + "_" + UUID.randomUUID().toString().replaceAll("-", "")).substring(0, 16),
                        player.getLocation());
        if (photographer == null) {
            throw new RuntimeException(
                    "Error on create photographer for player: {name: " + player.getName() + " , UUID:" + playerUniqueId + "}");
        }

        LocalDateTime currentTime = LocalDateTime.now();
        String recordPath = "replay/player/" + player.getName() + "@" + playerUniqueId;
        new File(recordPath).mkdirs();
        File recordFile = new File(recordPath + "/" + currentTime.format(DATE_FORMATTER) + ".mcpr");
        if (recordFile.exists()) {
            recordFile.delete();
        }
        recordFile.createNewFile();
        photographer.setRecordFile(recordFile);

        ISeeYou.getPhotographers().put(playerUniqueId, photographer);
        photographer.setFollowPlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        Photographer photographer = ISeeYou.getPhotographers().get(event.getPlayer().getUniqueId().toString());
        if (photographer == null) {
            return;
        }
        if (ISeeYou.getToml().data.pauseInsteadOfStopRecordingOnPlayerQuit) {
            photographer.resumeRecording();
        } else {
            photographer.stopRecording();
        }
    }
}
