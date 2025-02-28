package ru.anton_flame.afkillevents.utils;

import org.bukkit.configuration.ConfigurationSection;
import ru.anton_flame.afkillevents.AFKillEvents;

import java.util.List;

public class ConfigManager {
    public static boolean firstEventEnabled, secondEventEnabled, rewardVictimNotKilledEnabled, firstEventBossBarEnabled, secondEventBossBarEnabled;
    public static String firstEventStartTime, firstEventStopTime, secondEventStartTime, secondEventStopTime, reloaded, noPermission, eventAlreadyStarted, eventNotStarted,
            firstEventStarted, firstEventStopped, secondEventStarted, secondEventStopped, incorrectEvent, maxKillsMessage, firstEventBossBarText, secondEventBossBarText, firstEventBossBarColor, secondEventBossBarColor, firstEventBossBarStyle, secondEventBossBarStyle;
    public static List<String> firstEventStartedForPlayers, firstEventStoppedHaveMembers, firstEventStoppedNoMembers, secondEventStartedForPlayers,
            secondEventStoppedVictimKilled, secondEventStoppedVictimNotKilled, help, victimQuit, victimRejoin, newVictim;
    public static ConfigurationSection firstEventRewards, secondEventRewardsForWinner, secondEventRewardsVictimNotKilled;
    public static int maxKills, victimTimeout;

    public static void setupConfigValues(AFKillEvents plugin) {
        firstEventEnabled = plugin.getConfig().getBoolean("first-event.settings.enabled");
        secondEventEnabled = plugin.getConfig().getBoolean("second-event.settings.enabled");
        rewardVictimNotKilledEnabled = plugin.getConfig().getBoolean("second-event.settings.reward-victim-not-killed.enabled");
        firstEventBossBarEnabled = plugin.getConfig().getBoolean("first-event.settings.bossbar.enabled");
        secondEventBossBarEnabled = plugin.getConfig().getBoolean("second-event.settings.bossbar.enabled");
        firstEventStartTime = plugin.getConfig().getString("first-event.settings.start-time");
        firstEventStopTime = plugin.getConfig().getString("first-event.settings.stop-time");
        secondEventStartTime = plugin.getConfig().getString("second-event.settings.start-time");
        secondEventStopTime = plugin.getConfig().getString("second-event.settings.stop-time");
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
        firstEventRewards = plugin.getConfig().getConfigurationSection("first-event.settings.rewards");
        firstEventStartedForPlayers = Hex.color(plugin.getConfig().getStringList("first-event.messages.started"));
        firstEventStoppedHaveMembers = Hex.color(plugin.getConfig().getStringList("first-event.messages.stopped-have-members"));
        firstEventStoppedNoMembers = Hex.color(plugin.getConfig().getStringList("first-event.messages.stopped-no-members"));
        secondEventRewardsForWinner = plugin.getConfig().getConfigurationSection("second-event.settings.rewards-for-winner");
        secondEventRewardsVictimNotKilled = plugin.getConfig().getConfigurationSection("second-event.settings.reward-victim-not-killed.rewards");
        secondEventStartedForPlayers = Hex.color(plugin.getConfig().getStringList("second-event.messages.started"));
        secondEventStoppedVictimKilled = Hex.color(plugin.getConfig().getStringList("second-event.messages.stopped-victim-killed"));
        secondEventStoppedVictimNotKilled = Hex.color(plugin.getConfig().getStringList("second-event.messages.stopped-victim-not-killed"));
        help = Hex.color(plugin.getConfig().getStringList("messages.help"));
        maxKills = plugin.getConfig().getInt("first-event.settings.max-kills");
        maxKillsMessage = Hex.color(plugin.getConfig().getString("first-event.messages.max-kills"));
        victimTimeout = plugin.getConfig().getInt("second-event.settings.victim-timeout");
        victimQuit = Hex.color(plugin.getConfig().getStringList("second-event.messages.victim-quit"));
        victimRejoin = Hex.color(plugin.getConfig().getStringList("second-event.messages.victim-rejoin"));
        newVictim = Hex.color(plugin.getConfig().getStringList("second-event.messages.new-victim"));
    }
}
