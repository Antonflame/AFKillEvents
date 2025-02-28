package ru.anton_flame.afkillevents.events.secondevent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.anton_flame.afkillevents.AFKillEvents;
import ru.anton_flame.afkillevents.utils.ConfigManager;
import ru.anton_flame.afkillevents.utils.InfoFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Listeners implements Listener {

    private final AFKillEvents plugin;
    public Listeners(AFKillEvents plugin) {
        this.plugin = plugin;
    }

    private final Map<UUID, Integer> pendingTasks = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (SecondEvent.isSecondEventActive()) {
            Player killer = event.getEntity().getKiller();
            Player victim = event.getEntity();

            if (killer != null && !killer.getName().equalsIgnoreCase(victim.getName()) && victim.getName().equalsIgnoreCase(SecondEvent.victimName())) {
                InfoFile.secondEventWinnerName = killer.getName();
                InfoFile.get().set("second-event.winner-name", killer.getName());
                InfoFile.save();

                SecondEvent.stopVictimKilled(plugin);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (SecondEvent.isSecondEventActive()) {
            Player player = event.getPlayer();
            if (player.getName().equals(InfoFile.secondEventVictimName)) {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    ConfigManager.victimQuit.forEach(line -> onlinePlayer.sendMessage(line.replace("%victim%", InfoFile.secondEventVictimName)));
                }

                int delay = ConfigManager.victimTimeout * 20;
                int taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (Bukkit.getPlayer(InfoFile.secondEventVictimName) == null) {
                        String oldVictimName = InfoFile.secondEventVictimName;

                        Random random = new Random();
                        Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
                        String victimName = players[random.nextInt(players.length)].getName();

                        InfoFile.get().set("second-event.victim-name", victimName);
                        InfoFile.secondEventVictimName = victimName;
                        InfoFile.save();

                        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                            ConfigManager.newVictim.forEach(line -> onlinePlayer.sendMessage(line.replace("%old_victim%", oldVictimName).replace("%new_victim%", victimName)));
                        }

                        SecondEvent.bossBar.setTitle(ConfigManager.secondEventBossBarText.replace("%victim%", victimName));
                    }
                }, delay);

                pendingTasks.put(player.getUniqueId(), taskId);

                if (SecondEvent.bossBar != null && SecondEvent.bossBar.getPlayers().contains(player)) {
                    SecondEvent.bossBar.removePlayer(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (SecondEvent.isSecondEventActive()) {
            Player player = event.getPlayer();
            if (pendingTasks.containsKey(player.getUniqueId())) {
                int taskId = pendingTasks.remove(player.getUniqueId());
                Bukkit.getScheduler().cancelTask(taskId);

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    ConfigManager.victimRejoin.forEach(line -> onlinePlayer.sendMessage(line.replace("%victim%", InfoFile.secondEventVictimName)));
                }
            }

            if (SecondEvent.bossBar != null && !SecondEvent.bossBar.getPlayers().contains(player)) {
                SecondEvent.bossBar.addPlayer(player);
            }
        }
    }
}
