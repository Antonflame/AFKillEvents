package ru.anton_flame.afkillevents.events.firstevent;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.anton_flame.afkillevents.utils.InfoFile;
import ru.anton_flame.afkillevents.utils.Utils;

import java.util.Objects;

public class FirstEvent {

    public static boolean isFirstEventActive() {
        return InfoFile.get().getBoolean("first-event.active");
    }

    public static String startTime = Utils.getString("first-event.settings.start-time");
    public static String stopTime = Utils.getString("first-event.settings.stop-time");

    public static void start() {
        if (Utils.getBoolean("first-event.settings.enabled")) {
            if (!isFirstEventActive()) {
                InfoFile.get().set("first-event.active", true);
                InfoFile.save();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    Utils.sendMessageFromConfig(player, null, "first-event.messages.started");
                }
            }
        }
    }

    public static void stop() {
        if (isFirstEventActive()) {
            int count = 0;
            String name = "";

            ConfigurationSection playersSection = InfoFile.get().getConfigurationSection("first-event.players");
            if (playersSection != null) {
                for (String key : playersSection.getKeys(false)) {
                    if (InfoFile.get().getInt("first-event.players." + key) > count) {
                        count = InfoFile.get().getInt("first-event.players." + key);
                        name = key;
                    }
                }
            }

            if (count > 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    for (String message : Utils.getStringList("first-event.messages.stopped-have-members")) {
                        Utils.sendMessage(player, message.replace("%player%", name).replace("%kills%", Integer.toString(count)));
                    }
                }

                for (String command : Utils.getStringList("first-event.settings.reward-commands")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", name));
                }

            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Utils.sendMessageFromConfig(player, null, "first-event.messages.stopped-no-members");
                }

            }

            InfoFile.get().set("first-event.active", false);
            InfoFile.get().set("first-event.players", "");
            InfoFile.save();
        }
    }
}
