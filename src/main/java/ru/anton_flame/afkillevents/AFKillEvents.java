package ru.anton_flame.afkillevents;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.anton_flame.afkillevents.commands.AFKillEventsCommand;
import ru.anton_flame.afkillevents.utils.Hex;
import ru.anton_flame.afkillevents.utils.InfoFile;

import java.util.Objects;

public final class AFKillEvents extends JavaPlugin {

    public static AFKillEvents plugin;
    public static AFKillEvents getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        System.out.println("AFKillEvents включен");

        saveDefaultConfig();
        InfoFile.load(this);

        Bukkit.getPluginManager().registerEvents(new ru.anton_flame.afkillevents.events.firstevent.Listeners(), this);
        Bukkit.getPluginManager().registerEvents(new ru.anton_flame.afkillevents.events.secondevent.Listeners(), this);

        ru.anton_flame.afkillevents.events.firstevent.Tasks firstEventTasks = new ru.anton_flame.afkillevents.events.firstevent.Tasks();
        firstEventTasks.runTaskTimerAsynchronously(this, 0, 10 * 20);

        ru.anton_flame.afkillevents.events.secondevent.Tasks secondEventTasks = new ru.anton_flame.afkillevents.events.secondevent.Tasks();
        secondEventTasks.runTaskTimerAsynchronously(this, 0, 10 * 20);

        Objects.requireNonNull(getCommand("afkillevents")).setExecutor(new AFKillEventsCommand());
    }

    @Override
    public void onDisable() {
        System.out.println("AFKillEvents выключен");
    }
}
