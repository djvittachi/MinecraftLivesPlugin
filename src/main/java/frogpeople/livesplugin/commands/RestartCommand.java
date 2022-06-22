package frogpeople.livesplugin.commands;

import frogpeople.livesplugin.util.ConfigHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class RestartCommand implements CommandExecutor {

    // --Commented out by Inspection (22.06.2022 14:37):FileConfiguration config = ConfigHandler.getConfig();

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        /*
        config.set(String.valueOf(event.getPlayer().getUniqueId() + "Lives"), 3);
        saveConfig();
        */
        return false;
    }
}
