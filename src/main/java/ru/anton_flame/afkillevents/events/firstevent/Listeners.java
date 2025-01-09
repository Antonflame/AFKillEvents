package ru.anton_flame.afkillevents.events.firstevent;

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
                int kills = 0;

                if (InfoFile.get().getString("first-event.players." + killer.getName()) != null) {
                    kills = InfoFile.get().getInt("first-event.players." + killer.getName());
                }

                InfoFile.get().set("first-event.players." + killer.getName(), kills + 1);
                InfoFile.save();
            }
        }
    }
}
