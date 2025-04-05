package ru.anton_flame.afkillevents.events.secondevent;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import ru.anton_flame.afkillevents.AFKillEvents;
import ru.anton_flame.afkillevents.utils.ConfigManager;
import ru.anton_flame.afkillevents.utils.InfoFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecondEvent {

    public static boolean isSecondEventActive() {
        return InfoFile.secondEventActive;
    }

    public static String winnerName() {
        return InfoFile.secondEventWinnerName;
    }

    public static String victimName() {
        return InfoFile.secondEventVictimName;
    }

    public static Player eventVictim = null;
    public static BossBar bossBar;
    private static final Random random = new Random();

    public static void start() {
        if (!ConfigManager.secondEventEnabled || isSecondEventActive() || Bukkit.getOnlinePlayers().isEmpty()) return;

        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
        for (int i = 0; i < players.length; i++) {
            Player candidate = players[random.nextInt(players.length)];
            if (ConfigManager.victimWorlds.contains(candidate.getWorld().getName())) {
                eventVictim = candidate;
                break;
            }
        }

        if (eventVictim == null) {
            Bukkit.getLogger().warning("Второй ивент не может быть начат, так как подходящая жертва не была найдена!");
            return;
        }

        String victimName = eventVictim.getName();
        InfoFile.get().set("second-event.victim-name", victimName);
        InfoFile.secondEventVictimName = victimName;

        for (Player player : Bukkit.getOnlinePlayers()) {
            for (String message : ConfigManager.secondEventStartedForPlayers) {
                player.sendMessage(message.replace("%player%", victimName));
            }
        }

        if (ConfigManager.secondEventBossBarEnabled) {
            createAndUpdateBossBar();
        }

        InfoFile.secondEventActive = true;
        InfoFile.get().set("second-event.active", true);
        InfoFile.save();
    }

    private static void createAndUpdateBossBar() {
        String victimName = eventVictim.getName();
        bossBar = Bukkit.createBossBar(
                formatBossBarText(victimName),
                BarColor.valueOf(ConfigManager.secondEventBossBarColor),
                BarStyle.valueOf(ConfigManager.secondEventBossBarStyle)
        );

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(onlinePlayer);
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(
                AFKillEvents.getPlugin(AFKillEvents.class),
                () -> {
                    if (bossBar != null && eventVictim != null) {
                        bossBar.setTitle(formatBossBarText(victimName));
                    }
                },
                20L, 20L
        );
    }

    private static String formatBossBarText(String victimName) {
        return ConfigManager.secondEventBossBarText
                .replace("%victim%", victimName)
                .replace("%x%", String.valueOf(eventVictim.getLocation().getBlockX()))
                .replace("%y%", String.valueOf(eventVictim.getLocation().getBlockY()))
                .replace("%z%", String.valueOf(eventVictim.getLocation().getBlockZ()));
    }

    public static void stopVictimKilled(Plugin plugin) {
        if (!isSecondEventActive()) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            for (String message : ConfigManager.secondEventStoppedVictimKilled) {
                player.sendMessage(message.replace("%player%", winnerName()));
            }
        }

        dispatchRewardsCommand(plugin, ConfigManager.secondEventRewardsForWinner, "%player%", winnerName());
        cleanupEvent();
    }

    public static void stopVictimNotKilled(Plugin plugin) {
        if (!isSecondEventActive()) return;

        for (Player player : Bukkit.getOnlinePlayers()) {
            for (String message : ConfigManager.secondEventStoppedVictimNotKilled) {
                player.sendMessage(message);
            }
        }

        if (ConfigManager.rewardVictimNotKilledEnabled) {
            dispatchRewardsCommand(plugin, ConfigManager.secondEventRewardsVictimNotKilled, "%player%", victimName());
        }

        cleanupEvent();
    }

    private static void cleanupEvent() {
        eventVictim = null;

        InfoFile.secondEventActive = false;
        InfoFile.get().set("second-event.active", false);
        InfoFile.get().set("second-event.winner-name", "");
        InfoFile.get().set("second-event.victim-name", "");
        InfoFile.save();

        if (bossBar != null) {
            bossBar.removeAll();
            bossBar = null;
        }
    }

    public static void dispatchRewardsCommand(Plugin plugin, ConfigurationSection rewardsSection, String target, String playerName) {
        List<String> rewards = new ArrayList<>(rewardsSection.getKeys(false));
        if (rewards.isEmpty()) return;

        String reward = rewards.get(random.nextInt(rewards.size()));
        List<String> commands = rewardsSection.getStringList(reward);

        for (String command : commands) {
            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace(target, playerName)));
        }
    }
}
