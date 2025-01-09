package ru.anton_flame.afkillevents.events.secondevent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.anton_flame.afkillevents.utils.InfoFile;
import ru.anton_flame.afkillevents.utils.Utils;

import java.util.Random;

public class SecondEvent {

    public static boolean isSecondEventActive() {
        return InfoFile.get().getBoolean("second-event.active");
    }
    public static String winnerName() {
        return InfoFile.get().getString("second-event.winner-name");
    }
    public static String victimName() {
        return InfoFile.get().getString("second-event.victim-name");
    }

    public static String startTime = Utils.getString("second-event.settings.start-time");
    public static String stopTime = Utils.getString("second-event.settings.stop-time");

    public static void start() {
        if (Utils.getBoolean("second-event.settings.enabled")) {
            if (!isSecondEventActive()) {
                if (!Bukkit.getOnlinePlayers().isEmpty()) {
                    InfoFile.get().set("second-event.active", true);
                    InfoFile.save();

                    Random random = new Random();
                    Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                    InfoFile.get().set("second-event.victim-name", players[random.nextInt(players.length)].getName());

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        for (String message : Utils.getStringList("second-event.messages.started")) {
                            Utils.sendMessage(player, message.replace("%player%", victimName()));
                        }
                    }
                }
            }
        }
    }

    public static void stopVictimKilled() {
        if (isSecondEventActive()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                for (String message : Utils.getStringList("second-event.messages.stopped-victim-killed")) {
                    Utils.sendMessage(player, message.replace("%player%", winnerName()));
                }
            }

            for (String command : Utils.getStringList("second-event.settings.reward-commands-victim-killed")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", winnerName()));
            }

            InfoFile.get().set("second-event.active", false);
            InfoFile.get().set("second-event.winner-name", "");
            InfoFile.get().set("second-event.victim-name", "");
            InfoFile.save();
        }
    }

    public static void stopVictimNotKilled() {
        if (isSecondEventActive()) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Utils.sendMessageFromConfig(player, null, "second-event.messages.stopped-victim-not-killed");
            }

            if (Utils.getBoolean("second-event.settings.reward-victim-not-killed.enabled")) {
                for (String command : Utils.getStringList("second-event.settings.reward-commands-victim-not-killed")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", victimName()));
                }
            }

            InfoFile.get().set("second-event.active", false);
            InfoFile.get().set("second-event.winner-name", "");
            InfoFile.get().set("second-event.victim-name", "");
            InfoFile.save();
        }
    }
}
