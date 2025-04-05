package ru.anton_flame.afkillevents.events.secondevent;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import ru.anton_flame.afkillevents.AFKillEvents;
import ru.anton_flame.afkillevents.events.firstevent.FirstEvent;
import ru.anton_flame.afkillevents.utils.ConfigManager;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class Tasks extends BukkitRunnable {

    private final AFKillEvents plugin;
    public Tasks(AFKillEvents plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        String today = now.getDayOfWeek().name();

        if (ConfigManager.secondEventEnabled) {
            List<String> startTimes = ConfigManager.secondEventStartTimes.getOrDefault(today, Collections.singletonList(""));
            List<String> stopTimes = ConfigManager.secondEventStopTimes.getOrDefault(today, Collections.singletonList(""));

            if (!SecondEvent.isSecondEventActive() && startTimes.contains(time)) {
                if (Bukkit.getOnlinePlayers().size() > 1) {
                    SecondEvent.start();
                } else {
                    Bukkit.getLogger().warning("Второй ивент не может быть начат, так как онлайн сервера меньше 1");
                }
            } else if (SecondEvent.isSecondEventActive() && stopTimes.contains(time)) {
                SecondEvent.stopVictimNotKilled(plugin);
            }
        }
    }
}
