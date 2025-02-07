package ru.anton_flame.afkillevents.events.firstevent;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import ru.anton_flame.afkillevents.AFKillEvents;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Tasks extends BukkitRunnable {

    private final AFKillEvents plugin;
    public Tasks(AFKillEvents plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String time = now.format(formatter);

        if (!FirstEvent.isFirstEventActive()) {
            if (time.equalsIgnoreCase(FirstEvent.startTime)) {
                if (Bukkit.getOnlinePlayers().size() > 1) {
                    FirstEvent.start();
                }
            }
        } else {
            if (time.equalsIgnoreCase(FirstEvent.stopTime)) {
                FirstEvent.stop(plugin);
            }
        }
    }
}
