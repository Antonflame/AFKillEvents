package ru.anton_flame.afkillevents.utils;

import org.bukkit.configuration.ConfigurationSection;
import ru.anton_flame.afkillevents.AFKillEvents;

import java.util.List;

public class ConfigManager {
    public static boolean firstEventEnabled, secondEventEnabled, rewardVictimNotKilledEnabled;
    public static String firstEventStartTime, firstEventStopTime, secondEventStartTime, secondEventStopTime, reloaded, noPermission, eventAlreadyStarted, eventNotStarted,
            firstEventStarted, firstEventStopped, secondEventStarted, secondEventStopped;
    public static List<String> firstEventStartedForPlayers, firstEventStoppedHaveMembers, firstEventStoppedNoMembers, secondEventStartedForPlayers,
            secondEventStoppedVictimKilled, secondEventStoppedVictimNotKilled, help;
    public static ConfigurationSection firstEventRewards, secondEventRewardsForWinner, secondEventRewardsVictimNotKilled;

    public static void setupConfigValues(AFKillEvents plugin) {
        firstEventEnabled = plugin.getConfig().getBoolean("first-event.settings.enabled");
        secondEventEnabled = plugin.getConfig().getBoolean("second-event.settings.enabled");
        rewardVictimNotKilledEnabled = plugin.getConfig().getBoolean("second-event.settings.reward-victim-not-killed.enabled");
        firstEventStartTime = plugin.getConfig().getString("first-event.settings.start-time");
        firstEventStopTime = plugin.getConfig().getString("first-event.settings.stop-time");
        secondEventStartTime = plugin.getConfig().getString("second-event.settings.start-time");
        secondEventStopTime = plugin.getConfig().getString("second-event.settings.stop-time");
        reloaded = plugin.getConfig().getString("messages.reloaded");
        noPermission = plugin.getConfig().getString("messages.no-permission");
        eventAlreadyStarted = plugin.getConfig().getString("messages.event-already-started");
        eventNotStarted = plugin.getConfig().getString("messages.event-not-started");
        firstEventStarted = plugin.getConfig().getString("messages.first-event-started");
        firstEventStopped = plugin.getConfig().getString("messages.first-event-stopped");
        secondEventStarted = plugin.getConfig().getString("messages.second-event-started");
        secondEventStopped = plugin.getConfig().getString("messages.second-event-stopped");
        firstEventRewards = plugin.getConfig().getConfigurationSection("first-event.settings.rewards");
        firstEventStartedForPlayers = plugin.getConfig().getStringList("first-event.messages.started");
        firstEventStoppedHaveMembers = plugin.getConfig().getStringList("first-event.messages.stopped-have-members");
        firstEventStoppedNoMembers = plugin.getConfig().getStringList("first-event.messages.stopped-no-members");
        secondEventRewardsForWinner = plugin.getConfig().getConfigurationSection("second-event.settings.rewards-for-winner");
        secondEventRewardsVictimNotKilled = plugin.getConfig().getConfigurationSection("second-event.settings.reward-victim-not-killed.rewards");
        secondEventStartedForPlayers = plugin.getConfig().getStringList("second-event.messages.started");
        secondEventStoppedVictimKilled = plugin.getConfig().getStringList("second-event.messages.stopped-victim-killed");
        secondEventStoppedVictimNotKilled = plugin.getConfig().getStringList("second-event.messages.stopped-victim-not-killed");
        help = plugin.getConfig().getStringList("messages.help");
    }
}
