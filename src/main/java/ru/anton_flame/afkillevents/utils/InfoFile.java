package ru.anton_flame.afkillevents.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class InfoFile {

    private static FileConfiguration config;
    private static File file;
    private static final String fileName = "info.yml";

    public static void load(Plugin plugin) {
        file = new File(plugin.getDataFolder(), fileName);

        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource(fileName, true);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return config;
    }

    public static void reload(Plugin plugin) {
        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveResource(fileName, true);
        }
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            System.out.println("Ошибка при перезагрузке файла info.yml:");
            System.out.println(e.getMessage());
        }
    }

    public static void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении файла info.yml:");
            System.out.println(e.getMessage());
        }
    }
}
