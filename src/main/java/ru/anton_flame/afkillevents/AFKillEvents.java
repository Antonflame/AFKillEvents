package ru.anton_flame.afkillevents;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import ru.anton_flame.afkillevents.commands.AFKillEventsCommand;
import ru.anton_flame.afkillevents.events.firstevent.FirstEvent;
import ru.anton_flame.afkillevents.events.secondevent.SecondEvent;
import ru.anton_flame.afkillevents.utils.ConfigManager;
import ru.anton_flame.afkillevents.utils.InfoFile;
import ru.anton_flame.afkillevents.utils.Placeholders;

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
        ru.anton_flame.afkillevents.events.firstevent.Tasks firstEventTasks = new ru.anton_flame.afkillevents.events.firstevent.Tasks(this);
        firstEventTasks.runTaskTimerAsynchronously(this, 0, 10 * 20);

        Bukkit.getPluginManager().registerEvents(new ru.anton_flame.afkillevents.events.secondevent.Listeners(this), this);
        ru.anton_flame.afkillevents.events.secondevent.Tasks secondEventTasks = new ru.anton_flame.afkillevents.events.secondevent.Tasks(this);
        secondEventTasks.runTaskTimerAsynchronously(this, 0, 10 * 20);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders(this).register();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Плагин выключен!");

        if (FirstEvent.bossBar != null) {
            FirstEvent.bossBar.removeAll();
            FirstEvent.bossBar = null;
        }

        if (SecondEvent.bossBar != null) {
            SecondEvent.bossBar.removeAll();
            SecondEvent.bossBar = null;
        }

        InfoFile.get().set("first-event.active", false);
        InfoFile.get().set("first-event.players", "");
        InfoFile.get().set("second-event.active", false);
        InfoFile.get().set("second-event.winner-name", "");
        InfoFile.get().set("second-event.victim-name", "");
        InfoFile.save();
    }
}
