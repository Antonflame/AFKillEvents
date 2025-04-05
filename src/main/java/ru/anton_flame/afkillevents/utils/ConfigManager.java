package ru.anton_flame.afkillevents.utils;

import org.bukkit.configuration.ConfigurationSection;
import ru.anton_flame.afkillevents.AFKillEvents;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    public static boolean firstEventEnabled, secondEventEnabled, rewardVictimNotKilledEnabled, firstEventBossBarEnabled, secondEventBossBarEnabled;
    public static String reloaded, noPermission, eventAlreadyStarted, eventNotStarted,
            firstEventStarted, firstEventStopped, secondEventStarted, secondEventStopped, incorrectEvent, maxKillsMessage, firstEventBossBarText,
            secondEventBossBarText, firstEventBossBarColor, secondEventBossBarColor, firstEventBossBarStyle, secondEventBossBarStyle,
            eventTimeRemainingTimePlaceholder, eventAlreadyActivePlaceholder, disabledCommand, noEventTodayPlaceholder, noMoreEventsPlaceholder;
    public static List<String> firstEventStartedForPlayers, firstEventStoppedHaveMembers, firstEventStoppedNoMembers, secondEventStartedForPlayers,
            secondEventStoppedVictimKilled, secondEventStoppedVictimNotKilled, help, victimQuit, victimRejoin, newVictim, victimWorlds, disabledVictimCommands;
    public static ConfigurationSection firstEventRewards, secondEventRewardsForWinner, secondEventRewardsVictimNotKilled, firstEventSchedule, secondEventSchedule;
    public static int maxKills, victimTimeout;

    public static Map<String, List<String>> firstEventStartTimes = new HashMap<>();
    public static Map<String, List<String>> firstEventStopTimes = new HashMap<>();
    public static Map<String, List<String>> secondEventStartTimes = new HashMap<>();
    public static Map<String, List<String>> secondEventStopTimes = new HashMap<>();

    public static void setupConfigValues(AFKillEvents plugin) {
        firstEventEnabled = plugin.getConfig().getBoolean("first-event.settings.enabled");
        secondEventEnabled = plugin.getConfig().getBoolean("second-event.settings.enabled");
        rewardVictimNotKilledEnabled = plugin.getConfig().getBoolean("second-event.settings.reward-victim-not-killed.enabled");
        firstEventBossBarEnabled = plugin.getConfig().getBoolean("first-event.settings.bossbar.enabled");
        secondEventBossBarEnabled = plugin.getConfig().getBoolean("second-event.settings.bossbar.enabled");

        reloaded = Hex.color(plugin.getConfig().getString("messages.reloaded"));
        noPermission = Hex.color(plugin.getConfig().getString("messages.no-permission"));
        eventAlreadyStarted = Hex.color(plugin.getConfig().getString("messages.event-already-started"));
        eventNotStarted = Hex.color(plugin.getConfig().getString("messages.event-not-started"));
        firstEventStarted = Hex.color(plugin.getConfig().getString("messages.first-event-started"));
        firstEventStopped = Hex.color(plugin.getConfig().getString("messages.first-event-stopped"));
        secondEventStarted = Hex.color(plugin.getConfig().getString("messages.second-event-started"));
        secondEventStopped = Hex.color(plugin.getConfig().getString("messages.second-event-stopped"));
        incorrectEvent = Hex.color(plugin.getConfig().getString("messages.incorrect-event"));
        firstEventBossBarText = Hex.color(plugin.getConfig().getString("first-event.settings.bossbar.text"));
        secondEventBossBarText = Hex.color(plugin.getConfig().getString("second-event.settings.bossbar.text"));
        firstEventBossBarColor = Hex.color(plugin.getConfig().getString("first-event.settings.bossbar.color"));
        secondEventBossBarColor = Hex.color(plugin.getConfig().getString("second-event.settings.bossbar.color"));
        firstEventBossBarStyle = Hex.color(plugin.getConfig().getString("first-event.settings.bossbar.style"));
        secondEventBossBarStyle = Hex.color(plugin.getConfig().getString("second-event.settings.bossbar.style"));
        eventTimeRemainingTimePlaceholder = Hex.color(plugin.getConfig().getString("placeholders.event_time_remaining.time"));
        eventAlreadyActivePlaceholder = Hex.color(plugin.getConfig().getString("placeholders.event_time_remaining.event_already_active"));
        disabledCommand = Hex.color(plugin.getConfig().getString("second-event.messages.disabled_command"));
        maxKillsMessage = Hex.color(plugin.getConfig().getString("first-event.messages.max-kills"));
        noEventTodayPlaceholder = Hex.color(plugin.getConfig().getString("placeholders.event_start_time.no_event_today"));
        noMoreEventsPlaceholder = Hex.color(plugin.getConfig().getString("placeholders.no_more_events"));

        firstEventRewards = plugin.getConfig().getConfigurationSection("first-event.settings.rewards");
        secondEventRewardsForWinner = plugin.getConfig().getConfigurationSection("second-event.settings.rewards-for-winner");
        secondEventRewardsVictimNotKilled = plugin.getConfig().getConfigurationSection("second-event.settings.reward-victim-not-killed.rewards");
        firstEventSchedule = plugin.getConfig().getConfigurationSection("first-event.settings.schedule");
        secondEventSchedule = plugin.getConfig().getConfigurationSection("second-event.settings.schedule");

        firstEventStartedForPlayers = Hex.color(plugin.getConfig().getStringList("first-event.messages.started"));
        firstEventStoppedHaveMembers = Hex.color(plugin.getConfig().getStringList("first-event.messages.stopped-have-members"));
        firstEventStoppedNoMembers = Hex.color(plugin.getConfig().getStringList("first-event.messages.stopped-no-members"));
        secondEventStartedForPlayers = Hex.color(plugin.getConfig().getStringList("second-event.messages.started"));
        secondEventStoppedVictimKilled = Hex.color(plugin.getConfig().getStringList("second-event.messages.stopped-victim-killed"));
        secondEventStoppedVictimNotKilled = Hex.color(plugin.getConfig().getStringList("second-event.messages.stopped-victim-not-killed"));
        help = Hex.color(plugin.getConfig().getStringList("messages.help"));
        victimQuit = Hex.color(plugin.getConfig().getStringList("second-event.messages.victim-quit"));
        victimRejoin = Hex.color(plugin.getConfig().getStringList("second-event.messages.victim-rejoin"));
        newVictim = Hex.color(plugin.getConfig().getStringList("second-event.messages.new-victim"));
        victimWorlds = plugin.getConfig().getStringList("second-event.settings.victim_worlds");
        disabledVictimCommands = plugin.getConfig().getStringList("second-event.settings.disabled_victim_commands");

        maxKills = plugin.getConfig().getInt("first-event.settings.max-kills");
        victimTimeout = plugin.getConfig().getInt("second-event.settings.victim-timeout");

        if (firstEventSchedule != null) {
            for (String day : firstEventSchedule.getKeys(false)) {
                List<String> starts = firstEventSchedule.getStringList(day + ".start-times");
                List<String> stops = firstEventSchedule.getStringList(day + ".stop-times");
                firstEventStartTimes.put(day.toUpperCase(), starts);
                firstEventStopTimes.put(day.toUpperCase(), stops);
            }
        }

        if (secondEventSchedule != null) {
            for (String day : secondEventSchedule.getKeys(false)) {
                List<String> starts = secondEventSchedule.getStringList(day + ".start-times");
                List<String> stops = secondEventSchedule.getStringList(day + ".stop-times");
                secondEventStartTimes.put(day.toUpperCase(), starts);
                secondEventStopTimes.put(day.toUpperCase(), stops);
            }
        }
    }
}
