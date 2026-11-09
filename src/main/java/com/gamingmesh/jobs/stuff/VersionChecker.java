package com.gamingmesh.jobs.stuff;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gamingmesh.jobs.Jobs;

import net.Zrips.CMILib.Messages.CMIMessages;
import net.Zrips.CMILib.Version.Schedulers.CMIScheduler;

public final class VersionChecker {

    private Jobs plugin;

    public VersionChecker(Jobs plugin) {
        this.plugin = plugin;
    }

    public void VersionCheck(final Player player) {
        if (!Jobs.getGCManager().isShowNewVersion())
            return;

        CMIScheduler.runTaskAsynchronously(plugin, () -> {
            String newVersion = getNewVersion();
            if (newVersion == null)
                return;

            String currentVersion = plugin.getDescription().getVersion();

            if (compareVersions(newVersion, currentVersion) <= 0)
                return;

            List<String> msg = Arrays.asList(
                ChatColor.GREEN + "*********************** " + plugin.getDescription().getName() + " **************************",
                ChatColor.GREEN + "* " + newVersion + " is now available! Your version: " + currentVersion,
                ChatColor.GREEN + "* " + ChatColor.DARK_GREEN + plugin.getDescription().getWebsite(),
                ChatColor.GREEN + "************************************************************");
            for (String one : msg)
                if (player != null)
                    player.sendMessage(one);
                else
                    CMIMessages.consoleMessage(one);
        });
    }

    /**
     * Compares dotted version strings segment by segment, ignoring any non-numeric
     * suffix such as "-Azisaba" or "-SNAPSHOT".
     *
     * @return positive if a is newer than b, negative if older, 0 if equal
     */
    static int compareVersions(String a, String b) {
        int[] pa = parseVersion(a);
        int[] pb = parseVersion(b);
        for (int i = 0; i < Math.max(pa.length, pb.length); i++) {
            int x = i < pa.length ? pa[i] : 0;
            int y = i < pb.length ? pb[i] : 0;
            if (x != y)
                return Integer.compare(x, y);
        }
        return 0;
    }

    private static int[] parseVersion(String version) {
        String cleaned = version.trim();
        int dash = cleaned.indexOf('-');
        if (dash >= 0)
            cleaned = cleaned.substring(0, dash);
        String[] parts = cleaned.split("\\.");
        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String digits = parts[i].replaceAll("\\D.*$", "");
            try {
                result[i] = digits.isEmpty() ? 0 : Integer.parseInt(digits);
            } catch (NumberFormatException e) {
                result[i] = 0;
            }
        }
        return result;
    }

    public String getNewVersion() {
        try {
            URLConnection con = new URL("https://api.spigotmc.org/legacy/update.php?resource=4216").openConnection();
            String version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            if (version.length() <= 8)
                return version;
        } catch (Throwable t) {
            CMIMessages.consoleMessage("&cFailed to check for " + plugin.getDescription().getName() + " update on spigot web page.");
        }
        return null;
    }

}
