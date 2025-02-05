package ru.anton_flame.afkillevents.events.secondevent;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.anton_flame.afkillevents.utils.ConfigManager;
import ru.anton_flame.afkillevents.utils.Hex;
import ru.anton_flame.afkillevents.utils.InfoFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecondEvent {

    public static boolean isSecondEventActive() {
        return InfoFile.secondEventActive;
    }
    public static String winnerName() {
        return InfoFile.secondEventWinnerName;
    }
    public static String victimName() {
        return InfoFile.secondEventVictimName;
    }

    public static String startTime = ConfigManager.secondEventStartTime;
    public static String stopTime = ConfigManager.secondEventStopTime;

    public static Random random = new Random();

    public static void start() {
        if (ConfigManager.secondEventEnabled) {
            if (!isSecondEventActive()) {
                if (!Bukkit.getOnlinePlayers().isEmpty()) {
                    InfoFile.secondEventActive = true;
                    InfoFile.get().set("second-event.active", true);

                    Random random = new Random();
                    Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                    String victimName = players[random.nextInt(players.length)].getName();
                    InfoFile.get().set("second-event.victim-name", victimName);
                    InfoFile.secondEventVictimName = victimName;
                    InfoFile.save();

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        for (String message : ConfigManager.secondEventStartedForPlayers) {
                            player.sendMessage(Hex.color(message.replace("%player%", victimName())));
                        }
                    }
                }
            }
        }
    }

    public static void stopVictimKilled() {
        if (isSecondEventActive()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (String message : ConfigManager.secondEventStoppedVictimKilled) {
                    player.sendMessage(Hex.color(message.replace("%player%", winnerName())));
                }
            }

            dispatchRewardsCommand(ConfigManager.secondEventRewardsForWinner, "%player%", winnerName());

            InfoFile.secondEventActive = false;
            InfoFile.get().set("second-event.active", false);
            InfoFile.get().set("second-event.winner-name", "");
            InfoFile.get().set("second-event.victim-name", "");
            InfoFile.save();
        }
    }

    public static void stopVictimNotKilled() {
        if (isSecondEventActive()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (String message : ConfigManager.secondEventStoppedVictimNotKilled) {
                    player.sendMessage(Hex.color(message));
                }
            }

            if (ConfigManager.rewardVictimNotKilledEnabled) {
                dispatchRewardsCommand(ConfigManager.secondEventRewardsVictimNotKilled, "%player%", victimName());
            }

            InfoFile.secondEventActive = false;
            InfoFile.get().set("second-event.active", false);
            InfoFile.get().set("second-event.winner-name", "");
            InfoFile.get().set("second-event.victim-name", "");
            InfoFile.save();
        }
    }

    public static void dispatchRewardsCommand(ConfigurationSection rewardsSection, String target, String playerName) {
        List<String> rewards = new ArrayList<>(rewardsSection.getKeys(false));
        String reward = rewards.get(random.nextInt(rewards.size()));
        List<String> commands = rewardsSection.getStringList(reward);

        for (String command : commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace(target, playerName));
        }
    }
}
