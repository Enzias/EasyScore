package fr.enzias.easyscore.util;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceHolderAPIHook {

    private static boolean placeholderAPI;
    public PlaceHolderAPIHook(){
    }

    public void setupPlugin() {
        Plugin placeholderPlugin = Bukkit.getPluginManager().getPlugin("PlaceholderAPI");
        if (placeholderPlugin == null) {
            Bukkit.getServer().getLogger().info("PlaceholderAPI plugin was not found!");
            placeholderAPI = false;
            return;
        }
        placeholderAPI = true;
    }

    public static boolean hasValidPlugin() {
        return placeholderAPI;
    }

    public static boolean hasPlaceholders(String message) {
        if(hasValidPlugin())
            return PlaceholderAPI.containsPlaceholders(message);
        return false;
    }

    public static String setPlaceholders(String message, Player executor) {
        if (!hasValidPlugin()) {
            return message;
        }

        return PlaceholderAPI.setPlaceholders(executor, message);
    }
}
