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
            return ConfigManager.firstEventStartTime;
        } else if (params.equalsIgnoreCase("first_event_stop_time")) {
            return ConfigManager.firstEventStopTime;
        } else if (params.equalsIgnoreCase("second_event_start_time")) {
            return ConfigManager.secondEventStartTime;
        } else if (params.equalsIgnoreCase("second_event_stop_time")) {
            return ConfigManager.secondEventStopTime;
        } else if (params.equalsIgnoreCase("first_event_time_remaining")) {
            if (FirstEvent.isFirstEventActive()) {
                return ConfigManager.eventAlreadyActivePlaceholder;
            } else {
                return getTimeRemaining(ConfigManager.firstEventStartTime);
            }
        } else if (params.equalsIgnoreCase("second_event_time_remaining")) {
            if (SecondEvent.isSecondEventActive()) {
                return ConfigManager.eventAlreadyActivePlaceholder;
            } else {
                return getTimeRemaining(ConfigManager.secondEventStartTime);
            }
        }

        return params;
    }

    private String getTimeRemaining(String eventTimeString) {
        LocalTime eventTime = LocalTime.parse(eventTimeString, DateTimeFormatter.ofPattern("HH:mm"));
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Moscow"));
        LocalDateTime eventDateTime = LocalDateTime.of(now.toLocalDate(), eventTime);

        if (now.isAfter(eventDateTime)) {
            eventDateTime = eventDateTime.plusDays(1);
        }

        Duration duration = Duration.between(now, eventDateTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return ConfigManager.eventTimeRemainingTimePlaceholder.replace("%hours%", String.valueOf(hours)).replace("%minutes%", String.valueOf(minutes));
    }
}
