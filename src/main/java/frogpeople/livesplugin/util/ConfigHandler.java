package frogpeople.livesplugin.util;

import frogpeople.livesplugin.main.LivesPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {

    private static final LivesPlugin instance = LivesPlugin.getInstance();
    private static final FileConfiguration config = instance.getConfig();

    public static FileConfiguration getConfig() {
        return config;
    }

    public static void saveConfig() {
        instance.saveConfig();
    }

    public static void init() {
        instance.saveDefaultConfig();
    }
}
