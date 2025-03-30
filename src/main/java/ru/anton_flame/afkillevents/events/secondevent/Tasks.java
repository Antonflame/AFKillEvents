package ru.anton_flame.afkillevents.events.secondevent;

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

        if (!SecondEvent.isSecondEventActive()) {
            if (time.equals(SecondEvent.startTime)) {
                if (Bukkit.getOnlinePlayers().size() > 1) {
                    SecondEvent.start();
                } else {
                    Bukkit.getLogger().warning("Второй ивент не может быть начат, так как онлайн сервера меньше 1");
                }
            }
        } else {
            if (time.equals(SecondEvent.stopTime)) {
                SecondEvent.stopVictimNotKilled(plugin);
            }
        }
    }
}
