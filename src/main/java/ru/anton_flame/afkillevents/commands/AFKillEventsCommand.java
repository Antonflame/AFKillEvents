package ru.anton_flame.afkillevents.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.anton_flame.afkillevents.AFKillEvents;
import ru.anton_flame.afkillevents.events.firstevent.FirstEvent;
import ru.anton_flame.afkillevents.events.secondevent.SecondEvent;
import ru.anton_flame.afkillevents.utils.ConfigManager;
import ru.anton_flame.afkillevents.utils.InfoFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AFKillEventsCommand implements CommandExecutor, TabCompleter {

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

                        commandSender.sendMessage(ConfigManager.reloaded);
                    } else {
                        commandSender.sendMessage(ConfigManager.noPermission);
                    }
                    break;

                case "start":
                    if (strings.length == 2) {
                        switch (strings[1].toLowerCase()) {
                            case "first":
                                if (commandSender.hasPermission("afkillevents.start")) {
                                    if (!FirstEvent.isFirstEventActive()) {
                                        FirstEvent.start();

                                        commandSender.sendMessage(ConfigManager.firstEventStarted);
                                    } else {
                                        commandSender.sendMessage(ConfigManager.eventAlreadyStarted);
                                    }
                                } else {
                                    commandSender.sendMessage(ConfigManager.noPermission);
                                }
                                break;
                            case "second":
                                if (commandSender.hasPermission("afkillevents.start")) {
                                    if (!SecondEvent.isSecondEventActive()) {
                                        SecondEvent.start();

                                        commandSender.sendMessage(ConfigManager.secondEventStarted);
                                    } else {
                                        commandSender.sendMessage(ConfigManager.eventAlreadyStarted);
                                    }
                                } else {
                                    commandSender.sendMessage(ConfigManager.noPermission);
                                }
                                break;
                            default:
                                commandSender.sendMessage(ConfigManager.incorrectEvent);
                                break;
                        }
                    } else {
                        for (String help : ConfigManager.help) {
                            commandSender.sendMessage(help);
                        }
                    }
                    break;

                case "stop":
                    if (strings.length == 2) {
                        switch (strings[1].toLowerCase()) {
                            case "first":
                                if (commandSender.hasPermission("afkillevents.stop")) {
                                    if (FirstEvent.isFirstEventActive()) {
                                        FirstEvent.stop(plugin);

                                        commandSender.sendMessage(ConfigManager.firstEventStopped);
                                    } else {
                                        commandSender.sendMessage(ConfigManager.eventNotStarted);
                                    }
                                } else {
                                    commandSender.sendMessage(ConfigManager.noPermission);
                                }
                                break;
                            case "second":
                                if (commandSender.hasPermission("afkillevents.stop")) {
                                    if (SecondEvent.isSecondEventActive()) {
                                        SecondEvent.stopVictimNotKilled(plugin);

                                        commandSender.sendMessage(ConfigManager.secondEventStopped);
                                    } else {
                                        commandSender.sendMessage(ConfigManager.eventNotStarted);
                                    }
                                } else {
                                    commandSender.sendMessage(ConfigManager.noPermission);
                                }
                                break;
                            default:
                                commandSender.sendMessage(ConfigManager.incorrectEvent);
                                break;
                        }
                    } else {
                        for (String help : ConfigManager.help) {
                            commandSender.sendMessage(help);
                        }
                    }
                    break;

                default:
                    for (String help : ConfigManager.help) {
                        commandSender.sendMessage(help);
                    }
            }

        } else {
            for (String help : ConfigManager.help) {
                commandSender.sendMessage(help);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            return Arrays.asList("reload", "start", "stop");
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("start")) {
            return Arrays.asList("first", "second");
        }

        if (strings.length == 2 && strings[0].equalsIgnoreCase("stop")) {
            return Arrays.asList("first", "second");
        }

        return Collections.emptyList();
    }
}
