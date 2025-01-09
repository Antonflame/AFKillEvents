package ru.anton_flame.afkillevents.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hex {

    private static final Pattern pattern = Pattern.compile("&#[a-fA-F0-9]{6}");

    public static String color(String text) {
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            String hexCode = text.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
            StringBuilder builder = new StringBuilder();

            replaceSharp.chars().forEach(c -> builder.append("&").append((char) c));

            text = text.replace(hexCode, builder.toString());
            matcher = pattern.matcher(text);
        }

        return ChatColor.translateAlternateColorCodes('&', text).replace("&", "");

    }
}
