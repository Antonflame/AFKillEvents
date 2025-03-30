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

    public static boolean isFirstEventActive() {
        return InfoFile.firstEventActive;
    }

    public static String startTime = ConfigManager.firstEventStartTime;
    public static String stopTime = ConfigManager.firstEventStopTime;

    public static BossBar bossBar;

    public static void start() {
        if (ConfigManager.firstEventEnabled) {
            if (!isFirstEventActive()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String message : ConfigManager.firstEventStartedForPlayers) {
                        player.sendMessage(message);
                    }
                }

                if (ConfigManager.firstEventBossBarEnabled) {
                    bossBar = Bukkit.createBossBar(ConfigManager.firstEventBossBarText, BarColor.valueOf(ConfigManager.firstEventBossBarColor), BarStyle.valueOf(ConfigManager.firstEventBossBarStyle));
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        bossBar.addPlayer(onlinePlayer);
                    }
                }

                InfoFile.firstEventActive = true;
                InfoFile.get().set("first-event.active", true);
                InfoFile.save();
            }
        }
    }

    public static void stop(Plugin plugin) {
        if (isFirstEventActive()) {
            int count = 0;
            String name = "";

            ConfigurationSection playersSection = InfoFile.players;
            if (playersSection != null) {
                for (String key : playersSection.getKeys(false)) {
                    if (playersSection.getInt(key) > count) {
                        count = playersSection.getInt(key);
                        name = key;
                    }
                }
            }

            if (count > 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String message : ConfigManager.firstEventStoppedHaveMembers) {
                        player.sendMessage(message.replace("%player%", name).replace("%kills%", Integer.toString(count)));
                    }
                }

                Random random = new Random();
                List<String> rewards = new ArrayList<>(ConfigManager.firstEventRewards.getKeys(false));
                String reward = rewards.get(random.nextInt(rewards.size()));
                List<String> commands = ConfigManager.firstEventRewards.getStringList(reward);

                for (String command : commands) {
                    String finalName = name;
                    Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", finalName)));
                }

            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String message : ConfigManager.firstEventStoppedNoMembers) {
                        player.sendMessage(message);
                    }
                }

            }

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
}
