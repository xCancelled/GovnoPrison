package ru.nightwill.prison.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.Collection;
import java.util.stream.Collectors;

@UtilityClass
public class TextUtils {

    public String format(String text){
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public Collection<String> format(Collection<String> collection){
        return collection.stream().map(TextUtils::format).collect(Collectors.toList());
    }
}
