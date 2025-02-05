package ru.anton_flame.afkillevents.events.secondevent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ru.anton_flame.afkillevents.utils.InfoFile;

public class Listeners implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (SecondEvent.isSecondEventActive()) {
            Player killer = event.getEntity().getKiller();
            Player victim = event.getEntity();

            if (killer != null && !killer.getName().equalsIgnoreCase(victim.getName()) && victim.getName().equalsIgnoreCase(SecondEvent.victimName())) {
                InfoFile.secondEventWinnerName = killer.getName();
                InfoFile.get().set("second-event.winner-name", killer.getName());
                InfoFile.save();

                SecondEvent.stopVictimKilled();
            }
        }
    }
}
