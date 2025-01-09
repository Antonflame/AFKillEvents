package ru.anton_flame.afkillevents.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import ru.anton_flame.afkillevents.AFKillEvents;
import ru.anton_flame.afkillevents.events.firstevent.FirstEvent;
import ru.anton_flame.afkillevents.events.secondevent.SecondEvent;
import ru.anton_flame.afkillevents.utils.InfoFile;
import ru.anton_flame.afkillevents.utils.Utils;

public class AFKillEventsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length != 0) {
            switch (strings[0].toLowerCase()) {
                case "reload":
                    if (commandSender.hasPermission("afkillevents.reload")) {
                        AFKillEvents.getPlugin().reloadConfig();
                        InfoFile.reload(AFKillEvents.getPlugin());

                        Utils.sendMessageFromConfig(commandSender, "messages.reloaded", null);
                    } else {
                        Utils.sendMessageFromConfig(commandSender, "messages.no-permission", null);
                    }
                    break;

                case "start":
                    if (strings.length == 2) {
                        switch (strings[1].toLowerCase()) {
                            case "first":
                                if (commandSender.hasPermission("afkillevents.start")) {
                                    if (!FirstEvent.isFirstEventActive()) {
                                        FirstEvent.start();

                                        Utils.sendMessageFromConfig(commandSender, "messages.first-event-started", null);
                                    } else {
                                        Utils.sendMessageFromConfig(commandSender, "messages.event-already-started", null);
                                    }
                                } else {
                                    Utils.sendMessageFromConfig(commandSender, "messages.no-permission", null);
                                }
                                break;
                            case "second":
                                if (commandSender.hasPermission("afkillevents.start")) {
                                    if (!SecondEvent.isSecondEventActive()) {
                                        SecondEvent.start();

                                        Utils.sendMessageFromConfig(commandSender, "messages.second-event-started", null);
                                    } else {
                                        Utils.sendMessageFromConfig(commandSender, "messages.event-already-started", null);
                                    }
                                } else {
                                    Utils.sendMessageFromConfig(commandSender, "messages.no-permission", null);
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

                                        Utils.sendMessageFromConfig(commandSender, "messages.first-event-stopped", null);
                                    } else {
                                        Utils.sendMessageFromConfig(commandSender, "messages.event-not-started", null);
                                    }
                                } else {
                                    Utils.sendMessageFromConfig(commandSender, "messages.no-permission", null);
                                }
                                break;
                            case "second":
                                if (commandSender.hasPermission("afkillevents.stop")) {
                                    if (SecondEvent.isSecondEventActive()) {
                                        SecondEvent.stopVictimNotKilled();

                                        Utils.sendMessageFromConfig(commandSender, "messages.second-event-stopped", null);
                                    } else {
                                        Utils.sendMessageFromConfig(commandSender, "messages.event-not-started", null);
                                    }
                                } else {
                                    Utils.sendMessageFromConfig(commandSender, "messages.no-permission", null);
                                }
                                break;
                        }
                    }
                    break;

                default:
                    Utils.sendMessageFromConfig(commandSender, null, "messages.help");
            }

        } else {
            Utils.sendMessageFromConfig(commandSender, null, "messages.help");
        }
        return true;
    }
}
