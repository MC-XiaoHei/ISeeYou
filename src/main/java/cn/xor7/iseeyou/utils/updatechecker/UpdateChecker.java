package cn.xor7.iseeyou.utils.updatechecker;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class UpdateChecker {

    private final JavaPlugin plugin;
    private final String projectName;

    public UpdateChecker(JavaPlugin plugin, String projectName) {
        this.plugin = plugin;
        this.projectName = projectName;
    }

    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try {
                URL url = new URL("https://hangar.papermc.io/api/v1/projects/" + projectName + "/versions");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
                    Gson gson = new Gson();
                    HangarApiResponse response = gson.fromJson(inputStreamReader, HangarApiResponse.class);
                    inputStreamReader.close();

                    if (response != null && response.getResult() != null && response.getResult().length > 0) {
                        String latestVersion = response.getResult()[0].getName();
                        consumer.accept(latestVersion);
                    }
                } else {
                    plugin.getLogger().info("Unable to check for updates: HTTP error code " + responseCode);
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    private static class HangarApiResponse {
        private HangarVersion[] result;

        public HangarVersion[] getResult() {
            return result;
        }
    }

    private static class HangarVersion {
        private String name;

        public String getName() {
            return name;
        }
    }
}
