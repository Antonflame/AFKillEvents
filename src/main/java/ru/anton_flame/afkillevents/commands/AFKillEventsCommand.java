package ru.anton_flame.afkillevents.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.anton_flame.afkillevents.AFKillEvents;
import ru.anton_flame.afkillevents.events.firstevent.FirstEvent;
import ru.anton_flame.afkillevents.events.secondevent.SecondEvent;
import ru.anton_flame.afkillevents.utils.ConfigManager;
import ru.anton_flame.afkillevents.utils.Hex;
import ru.anton_flame.afkillevents.utils.InfoFile;

public class AFKillEventsCommand implements CommandExecutor {

    private final AFKillEvents plugin;
    public AFKillEventsCommand(AFKillEvents plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length != 0) {
            switch (strings[0].toLowerCase()) {
                case "reload":
                    if (commandSender.hasPermission("afkillevents.reload")) {
                        plugin.reloadConfig();
                        ConfigManager.setupConfigValues(plugin);
                        InfoFile.reload(plugin);
                        InfoFile.setupConfigValues();

                        commandSender.sendMessage(Hex.color(ConfigManager.reloaded));
                    } else {
                        commandSender.sendMessage(Hex.color(ConfigManager.noPermission));
                    }
                    break;

                case "start":
                    if (strings.length == 2) {
                        switch (strings[1].toLowerCase()) {
                            case "first":
                                if (commandSender.hasPermission("afkillevents.start")) {
                                    if (!FirstEvent.isFirstEventActive()) {
                                        FirstEvent.start();

                                        commandSender.sendMessage(Hex.color(ConfigManager.firstEventStarted));
                                    } else {
                                        commandSender.sendMessage(Hex.color(ConfigManager.eventAlreadyStarted));
                                    }
                                } else {
                                    commandSender.sendMessage(Hex.color(ConfigManager.noPermission));
                                }
                                break;
                            case "second":
                                if (commandSender.hasPermission("afkillevents.start")) {
                                    if (!SecondEvent.isSecondEventActive()) {
                                        SecondEvent.start();

                                        commandSender.sendMessage(Hex.color(ConfigManager.secondEventStarted));
                                    } else {
                                        commandSender.sendMessage(Hex.color(ConfigManager.eventAlreadyStarted));
                                    }
                                } else {
                                    commandSender.sendMessage(Hex.color(ConfigManager.noPermission));
                                }
                                break;
                        }
                    }
                    break;

                case "stop":
                    if (strings.length == 2) {
                        switch (strings[1].toLowerCase()) {
                            case "first":
                                if (commandSender.hasPermission("afkillevents.stop")) {
                                    if (FirstEvent.isFirstEventActive()) {
                                        FirstEvent.stop();

                                        commandSender.sendMessage(Hex.color(ConfigManager.firstEventStopped));
                                    } else {
                                        commandSender.sendMessage(Hex.color(ConfigManager.eventNotStarted));
                                    }
                                } else {
                                    commandSender.sendMessage(Hex.color(ConfigManager.noPermission));
                                }
                                break;
                            case "second":
                                if (commandSender.hasPermission("afkillevents.stop")) {
                                    if (SecondEvent.isSecondEventActive()) {
                                        SecondEvent.stopVictimNotKilled();

                                        commandSender.sendMessage(Hex.color(ConfigManager.secondEventStopped));
                                    } else {
                                        commandSender.sendMessage(Hex.color(ConfigManager.eventNotStarted));
                                    }
                                } else {
                                    commandSender.sendMessage(Hex.color(ConfigManager.noPermission));
                                }
                                break;
                        }
                    }
                    break;

                default:
                    for (String help : ConfigManager.help) {
                        commandSender.sendMessage(Hex.color(help));
                    }
            }

        } else {
            for (String help : ConfigManager.help) {
                commandSender.sendMessage(Hex.color(help));
            }
        }
        return true;
    }
}
