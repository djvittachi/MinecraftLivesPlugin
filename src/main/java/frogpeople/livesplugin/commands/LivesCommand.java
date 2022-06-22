package frogpeople.livesplugin.commands;

import frogpeople.livesplugin.util.ConfigHandler;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LivesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player commandSender = (Player) sender;
        FileConfiguration config = ConfigHandler.getConfig();
        int playerLives = config.getInt(commandSender.getUniqueId() + "Lives");

        String[] messages = {ChatColor.RED + "1", ChatColor.YELLOW + "2", ChatColor.GREEN + "3"};

        if (playerLives > 0 && playerLives <= messages.length) {
            commandSender.playSound(commandSender.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
            commandSender.sendTitle(messages[playerLives - 1], "Your current lives are: " + playerLives, 20, 100, 20);
        }

        return true;
    }

}