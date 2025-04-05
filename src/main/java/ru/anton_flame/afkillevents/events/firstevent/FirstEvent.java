package ru.anton_flame.afkillevents.events.firstevent;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.anton_flame.afkillevents.utils.ConfigManager;
import ru.anton_flame.afkillevents.utils.InfoFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FirstEvent {

    public static BossBar bossBar;

    public static boolean isFirstEventActive() {
        return InfoFile.firstEventActive;
    }

    public static void start() {
        if (!ConfigManager.firstEventEnabled || isFirstEventActive()) return;

        sendMessagesToAll(ConfigManager.firstEventStartedForPlayers);

        if (ConfigManager.firstEventBossBarEnabled) {
            bossBar = Bukkit.createBossBar(
                    ConfigManager.firstEventBossBarText,
                    BarColor.valueOf(ConfigManager.firstEventBossBarColor),
                    BarStyle.valueOf(ConfigManager.firstEventBossBarStyle)
            );
            Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
        }

        InfoFile.firstEventActive = true;
        InfoFile.get().set("first-event.active", true);
        InfoFile.save();
    }

    public static void stop(Plugin plugin) {
        if (!isFirstEventActive()) return;

        String topPlayer = "";
        int topKills = 0;

        ConfigurationSection playersSection = InfoFile.players;
        if (playersSection != null) {
            for (String key : playersSection.getKeys(false)) {
                int kills = playersSection.getInt(key);
                if (kills > topKills) {
                    topKills = kills;
                    topPlayer = key;
                }
            }
        }

        if (topKills > 0) {
            sendMessagesToAll(ConfigManager.firstEventStoppedHaveMembers, topPlayer, topKills);
            giveRewardTo(plugin, topPlayer);
        } else {
            sendMessagesToAll(ConfigManager.firstEventStoppedNoMembers);
        }

        cleanup();
    }

    private static void giveRewardTo(Plugin plugin, String playerName) {
        List<String> rewardsKeys = new ArrayList<>(ConfigManager.firstEventRewards.getKeys(false));
        if (rewardsKeys.isEmpty()) return;

        String rewardKey = rewardsKeys.get(new Random().nextInt(rewardsKeys.size()));
        List<String> commands = ConfigManager.firstEventRewards.getStringList(rewardKey);

        for (String command : commands) {
            Bukkit.getScheduler().runTask(plugin,
                    () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", playerName))
            );
        }
    }

    private static void sendMessagesToAll(List<String> messages) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            messages.forEach(player::sendMessage);
        }
    }

    private static void sendMessagesToAll(List<String> messages, String playerName, int kills) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (String message : messages) {
                player.sendMessage(message
                        .replace("%player%", playerName)
                        .replace("%kills%", String.valueOf(kills)));
            }
        }
    }

    private static void cleanup() {
        InfoFile.firstEventActive = false;
        InfoFile.get().set("first-event.active", false);
        InfoFile.get().set("first-event.players", "");
        InfoFile.save();

        if (bossBar != null) {
            bossBar.removeAll();
            bossBar = null;
        }
    }
}
