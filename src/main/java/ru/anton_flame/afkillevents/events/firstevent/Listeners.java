package ru.anton_flame.afkillevents.events.firstevent;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.anton_flame.afkillevents.utils.InfoFile;

public class Listeners implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (FirstEvent.isFirstEventActive()) {
            Player killer = event.getEntity().getKiller();

            if (killer != null) {
                ConfigurationSection playersSection = InfoFile.players;
                int kills = 0;

                if (playersSection.getString(killer.getName()) != null) {
                    kills = playersSection.getInt(killer.getName());
                }

                playersSection.set(killer.getName(), kills + 1);
                InfoFile.save();
            }
        }
    }
}
