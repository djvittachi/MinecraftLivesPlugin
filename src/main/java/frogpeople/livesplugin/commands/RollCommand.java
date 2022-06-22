package frogpeople.livesplugin.commands;

import frogpeople.livesplugin.main.LivesPlugin;
import frogpeople.livesplugin.util.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class RollCommand implements CommandExecutor {

    private final FileConfiguration config = ConfigHandler.getConfig();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        ArrayList<Player> eligiblePlayers = new ArrayList<>();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (config.getInt(player.getUniqueId() + "Lives") > 1) {
                eligiblePlayers.add(player);
            }

        });

        if (eligiblePlayers.size() > 0) {


            int selectedIndex = ThreadLocalRandom.current().nextInt(0, eligiblePlayers.size());

            Player selectedPlayer = eligiblePlayers.get(selectedIndex);

            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player != selectedPlayer) {
                    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 1.0f);
                    player.sendTitle(ChatColor.GREEN + player.getName(), "you are not the hit man", 20, 100, 20);
                }
            });

            selectedPlayer.playSound(selectedPlayer.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 1.0f);
            selectedPlayer.sendTitle(ChatColor.RED + selectedPlayer.getName(), "you are the hit man", 20, 100, 20);

            config.set("Assassin", String.valueOf(selectedPlayer.getUniqueId()));
            ConfigHandler.saveConfig();
        }

        return false;
    }

}