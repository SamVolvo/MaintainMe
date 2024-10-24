package com.samvolvo.maintainme.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpdateChecker {
    private final Plugin plugin;
    private final String gitHubUserName;
    private final String repoName;
    private final String downloadUrl;

    public UpdateChecker(Plugin plugin, String gitHubUserName, String repoName, String downloadUrl) {
        this.plugin = plugin;
        this.gitHubUserName = gitHubUserName;
        this.repoName = repoName;
        this.downloadUrl = downloadUrl;
    }

    public List<String> generateUpdateMessage(String v) {
        try {
            String currentVersion = "v" + v;
            int releasesBehind = getReleasesBehind(currentVersion);
            JsonArray releases = getAllReleases();
            String tagName = releases.get(0).getAsJsonObject().get("tag_name").getAsString();
            List<String> message = new ArrayList<>();
            if (releasesBehind > 0) {
                message.add("*********************************************************************");
                message.add(plugin.getName() + " is outdated!");
                message.add("Latest version: " + tagName);
                message.add("Your version: v" + plugin.getDescription().getVersion());
                message.add(downloadUrl);
                message.add("*********************************************************************");
            } else {
                return message;
            }
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.singletonList("Unable to connect to version Check!");
        }
    }

    public String generateUpdateMessageColored(String v) {
        String currentVersion = "V" + v;
        int releasesBehind = getReleasesBehind(currentVersion);
        if (releasesBehind > 0) {
            return ("§cYou are §4" + releasesBehind + "§c release(s) behind!\n" +
                    "Download the newest release at " + downloadUrl);
        } else {
            return null;
        }
    }

    private int getReleasesBehind(String currentVersion) {
        try {
            int behind = 0;
            JsonArray releases = getAllReleases();
            if (releases != null) {
                for (JsonElement release : releases) {
                    String tagName = release.getAsJsonObject().get("tag_name").getAsString();
                    if (!tagName.equalsIgnoreCase(currentVersion)) behind++;
                    else return behind;
                }
                return behind;
            }
        } catch (IOException ignore) {
        }
        return 0;
    }

    private JsonArray getAllReleases() throws IOException {
        OkHttpClient client = new OkHttpClient();
        String releasesUrl = "https://api.github.com/repos/" + gitHubUserName + "/" + repoName + "/releases";
        Request request = new Request.Builder()
                .url(releasesUrl)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonParser parser = new JsonParser();
                return parser.parse(response.body().string()).getAsJsonArray();
            } else {
                return null;
            }
        }
    }
}
