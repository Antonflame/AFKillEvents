package ru.anton_flame.afkillevents;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import ru.anton_flame.afkillevents.commands.AFKillEventsCommand;
import ru.anton_flame.afkillevents.utils.ConfigManager;
import ru.anton_flame.afkillevents.utils.InfoFile;

public final class AFKillEvents extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Плагин включен!");
        saveDefaultConfig();
        ConfigManager.setupConfigValues(this);
        InfoFile.load(this);
        InfoFile.setupConfigValues();
        PluginCommand afKillEventsCommand = getCommand("afkillevents");
        AFKillEventsCommand afKillEventsCommandClass = new AFKillEventsCommand(this);
        afKillEventsCommand.setExecutor(afKillEventsCommandClass);
        afKillEventsCommand.setTabCompleter(afKillEventsCommandClass);

        Bukkit.getPluginManager().registerEvents(new ru.anton_flame.afkillevents.events.firstevent.Listeners(), this);
        Bukkit.getPluginManager().registerEvents(new ru.anton_flame.afkillevents.events.secondevent.Listeners(this), this);

        ru.anton_flame.afkillevents.events.firstevent.Tasks firstEventTasks = new ru.anton_flame.afkillevents.events.firstevent.Tasks(this);
        firstEventTasks.runTaskTimerAsynchronously(this, 0, 10 * 20);

        ru.anton_flame.afkillevents.events.secondevent.Tasks secondEventTasks = new ru.anton_flame.afkillevents.events.secondevent.Tasks(this);
        secondEventTasks.runTaskTimerAsynchronously(this, 0, 10 * 20);
    }

    @Override
    public void onDisable() {
        getLogger().info("Плагин выключен!");
    }
}
