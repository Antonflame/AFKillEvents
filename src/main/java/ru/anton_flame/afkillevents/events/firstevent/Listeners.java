package ru.anton_flame.afkillevents.events.firstevent;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.anton_flame.afkillevents.utils.ConfigManager;
import ru.anton_flame.afkillevents.utils.InfoFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Listeners implements Listener {

    private final Map<UUID, Map<UUID, Integer>> playerKills = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (FirstEvent.isFirstEventActive()) {
            Player player = event.getEntity();
            Player killer = player.getKiller();
            if (killer != null) {
                UUID playerUUID = player.getUniqueId();
                UUID killerUUID = killer.getUniqueId();

                playerKills.computeIfAbsent(playerUUID, k -> new HashMap<>());

                int currentKills = playerKills.get(playerUUID).getOrDefault(killerUUID, 0);

                if (currentKills >= ConfigManager.maxKills) {
                    killer.sendMessage(ConfigManager.maxKillsMessage.replace("%player%", player.getName()));
                    return;
                }

                playerKills.get(playerUUID).put(killerUUID, currentKills + 1);

                ConfigurationSection playersSection = InfoFile.players;
                int totalKills = playersSection.getInt(killer.getName(), 0);
                playersSection.set(killer.getName(), totalKills + 1);
                InfoFile.save();
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (FirstEvent.isFirstEventActive()) {
            Player player = event.getPlayer();
            if (FirstEvent.bossBar != null && !FirstEvent.bossBar.getPlayers().contains(player)) {
                FirstEvent.bossBar.addPlayer(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (FirstEvent.isFirstEventActive()) {
            Player player = event.getPlayer();
            if (FirstEvent.bossBar != null && FirstEvent.bossBar.getPlayers().contains(player)) {
                FirstEvent.bossBar.removePlayer(player);
            }
        }
    }
}
