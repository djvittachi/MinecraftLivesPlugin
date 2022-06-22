package frogpeople.livesplugin.listener;

import frogpeople.livesplugin.main.LivesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinHandler implements Listener {

    final FileConfiguration config = LivesPlugin.getInstance().getConfig();

    @EventHandler
    public void playerHasJoined(PlayerJoinEvent event) {

        if (!(config.contains(event.getPlayer().getUniqueId() + "Lives"))) {
            //Set Player Lives To Three
            config.set(event.getPlayer().getUniqueId() + "Lives", 3);
            LivesPlugin.getInstance().saveConfig();
        }

        //Change their tab color name
        int playerLives = config.getInt(event.getPlayer().getUniqueId() + "Lives");

        ChatColor[] colors = {
                ChatColor.GRAY,
                ChatColor.RED,
                ChatColor.YELLOW,
                ChatColor.GREEN
        };

        if (playerLives >= 0 && playerLives < colors.length) {
            event.getPlayer().setPlayerListName(colors[playerLives] + event.getPlayer().getName());
            event.getPlayer().setDisplayName(colors[playerLives] + event.getPlayer().getName());
        }
    }
}
