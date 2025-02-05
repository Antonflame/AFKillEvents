package ru.anton_flame.afkillevents.events.firstevent;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.anton_flame.afkillevents.utils.ConfigManager;
import ru.anton_flame.afkillevents.utils.Hex;
import ru.anton_flame.afkillevents.utils.InfoFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FirstEvent {

    public static boolean isFirstEventActive() {
        return InfoFile.firstEventActive;
    }

    public static String startTime = ConfigManager.secondEventStartTime;
    public static String stopTime = ConfigManager.secondEventStopTime;

    public static void start() {
        if (ConfigManager.secondEventEnabled) {
            if (!isFirstEventActive()) {
                InfoFile.firstEventActive = true;
                InfoFile.get().set("first-event.active", true);
                InfoFile.save();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String message : ConfigManager.firstEventStartedForPlayers) {
                        player.sendMessage(Hex.color(message));
                    }
                }
            }
        }
    }

    public static void stop() {
        if (isFirstEventActive()) {
            int count = 0;
            String name = "";

            ConfigurationSection playersSection = InfoFile.players;
            if (playersSection != null) {
                for (String key : playersSection.getKeys(false)) {
                    if (playersSection.getInt(key) > count) {
                        count = InfoFile.get().getInt("first-event.players." + key);
                        name = key;
                    }
                }
            }

            if (count > 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String message : ConfigManager.firstEventStoppedHaveMembers) {
                        player.sendMessage(Hex.color(message.replace("%player%", name).replace("%kills%", Integer.toString(count))));
                    }
                }

                Random random = new Random();
                List<String> rewards = new ArrayList<>(ConfigManager.firstEventRewards.getKeys(false));
                String reward = rewards.get(random.nextInt(rewards.size()));
                List<String> commands = ConfigManager.firstEventRewards.getStringList(reward);

                for (String command : commands) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", name));
                }

            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String message : ConfigManager.firstEventStoppedNoMembers) {
                        player.sendMessage(Hex.color(message));
                    }
                }

            }

            InfoFile.firstEventActive = false;
            InfoFile.get().set("first-event.active", false);
            InfoFile.get().set("first-event.players", "");
            InfoFile.save();
        }
    }
}
