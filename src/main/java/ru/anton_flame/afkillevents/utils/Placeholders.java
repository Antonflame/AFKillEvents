package ru.anton_flame.afkillevents.utils;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.anton_flame.afkillevents.AFKillEvents;
import ru.anton_flame.afkillevents.events.firstevent.FirstEvent;
import ru.anton_flame.afkillevents.events.secondevent.SecondEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Placeholders extends PlaceholderExpansion {

    private final AFKillEvents plugin;
    public Placeholders(AFKillEvents plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "afkillevents";
    }

    @Override
    public @NotNull String getAuthor() {
        return "anton_flame";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("first_event_start_time")) {
            return getNextStart(ConfigManager.firstEventStartTimes);
        } else if (params.equalsIgnoreCase("first_event_stop_time")) {
            return getNextStop(ConfigManager.firstEventStopTimes);
        } else if (params.equalsIgnoreCase("second_event_start_time")) {
            return getNextStart(ConfigManager.secondEventStartTimes);
        } else if (params.equalsIgnoreCase("second_event_stop_time")) {
            return getNextStop(ConfigManager.secondEventStopTimes);
        } else if (params.equalsIgnoreCase("first_event_time_remaining")) {
            if (FirstEvent.isFirstEventActive()) {
                return ConfigManager.eventAlreadyActivePlaceholder;
            } else {
                return getNextStartRemaining(ConfigManager.firstEventStartTimes);
            }
        } else if (params.equalsIgnoreCase("second_event_time_remaining")) {
            if (SecondEvent.isSecondEventActive()) {
                return ConfigManager.eventAlreadyActivePlaceholder;
            } else {
                return getNextStartRemaining(ConfigManager.secondEventStartTimes);
            }
        }

        return params;
    }

    public static String getNextStartRemaining(Map<String, List<String>> map) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        LocalTime nowTime = now.toLocalTime();
        String day = now.getDayOfWeek().toString();

        List<String> times = map.get(day);
        if (times == null || times.isEmpty()) return ConfigManager.noEventTodayPlaceholder;

        for (String time : times) {
            LocalTime eventTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
            if (eventTime.isAfter(nowTime)) {
                Duration duration = Duration.between(nowTime, eventTime);
                long totalMinutes = duration.toMinutes();
                long hours = totalMinutes / 60;
                long minutes = totalMinutes % 60;

                return ConfigManager.eventTimeRemainingTimePlaceholder
                        .replace("%hours%", String.valueOf(hours))
                        .replace("%minutes%", String.valueOf(minutes));
            }
        }

        return ConfigManager.noMoreEventsPlaceholder;
    }

    public String getNextStart(Map<String, List<String>> map) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        String timeNow = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        String day = now.getDayOfWeek().toString();

        List<String> times = map.get(day);
        if (times == null || times.isEmpty()) return ConfigManager.noEventTodayPlaceholder;

        for (String time : times) {
            if (time.compareTo(timeNow) > 0) return time;
        }
        return ConfigManager.noMoreEventsPlaceholder;
    }

    public String getNextStop(Map<String, List<String>> map) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        String timeNow = now.format(DateTimeFormatter.ofPattern("HH:mm"));
        String day = now.getDayOfWeek().toString();

        List<String> times = map.get(day);
        if (times == null || times.isEmpty()) return ConfigManager.noEventTodayPlaceholder;

        for (String time : times) {
            if (time.compareTo(timeNow) > 0) return time;
        }
        return ConfigManager.noMoreEventsPlaceholder;
    }
}
