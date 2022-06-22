package frogpeople.livesplugin.listener;

import frogpeople.livesplugin.main.LivesPlugin;
import frogpeople.livesplugin.util.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathHandler implements Listener {

    FileConfiguration config = ConfigHandler.getConfig();

    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {

        int playerLives = config.getInt(event.getEntity().getUniqueId() + "Lives");
        playerLives -= 1;

        //Adjust For Death
        config.set(event.getEntity().getUniqueId() + "Lives", playerLives);
        ConfigHandler.saveConfig();

        ChatColor[] colors = {
                ChatColor.GRAY,
                ChatColor.RED,
                ChatColor.YELLOW,
                ChatColor.GREEN
        };

        String[] messages = {
                ChatColor.YELLOW + "Is in the yellow-zone",
                ChatColor.RED + "Is in the red-zone",
                ChatColor.RED + "Has Been Eliminated"
        };

        // update the player's color before we sent a title
        if (playerLives >= 0 && playerLives < colors.length) {
            event.getEntity().setPlayerListName(colors[playerLives] + event.getEntity().getName());
            event.getEntity().setDisplayName(colors[playerLives] + event.getEntity().getName());
        }

        if (playerLives > 0 && playerLives < messages.length) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
                player.sendTitle(event.getEntity().getDisplayName(), messages[playerLives - 1], 20, 100, 20);
            }
        }

        if (playerLives == 0) {
            event.getEntity().setGameMode(GameMode.SPECTATOR);
        }
    }
}
