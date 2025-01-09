package ru.anton_flame.afkillevents.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import ru.anton_flame.afkillevents.AFKillEvents;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static FileConfiguration getConfig() {
        return AFKillEvents.getPlugin().getConfig();
    }

    public static String getString(String path) {
        return getConfig().getString(path);
    }
    public static List<String> getStringList(String path) {
        return getConfig().getStringList(path);
    }
    public static List<String> getKeys(String path) {
        ConfigurationSection section = getConfig().getConfigurationSection(path);

        if (section == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>((section.getKeys(false)));
    }

    public static int getInt(String path) {
        return getConfig().getInt(path);
    }
    public static double getDouble(String path) {
        return getConfig().getDouble(path);
    }
    public static long getLong(String path) {
        return getConfig().getLong(path);
    }

    public static boolean getBoolean(String path) {
        return getConfig().getBoolean(path);
    }

    public static void sendMessageFromConfig(CommandSender user, String stringPath, String stringListPath) {
        if (stringPath != null) {
            user.sendMessage(Hex.color(getString(stringPath)));
        } else if (stringListPath != null) {
            for (String message : getStringList(stringListPath)) {
                user.sendMessage(Hex.color(message));
            }
        }
    }
    public static void sendMessage(CommandSender user, String message) {
        user.sendMessage(Hex.color(message));
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(Hex.color(title), Hex.color(subtitle), 10, 70, 20);
    }

    public static void sendActionBar(Player player, String message) {
        player.sendActionBar(Component.text(Hex.color(message)));
    }
}
