package frogpeople.livesplugin;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class LivesPlugin extends JavaPlugin implements Listener {
    FileConfiguration config;

    @Override
    public void onEnable() {
        //Save The Default Config
        this.saveDefaultConfig();
        //Register Events
        getServer().getPluginManager().registerEvents(this,this);

        config = this.getConfig();

        Objects.requireNonNull(getCommand("lives")).setExecutor(new LivesCommand());
        Objects.requireNonNull(getCommand("roll")).setExecutor(new RollCommand());
    }

    public class LivesCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if(sender instanceof Player) {
                Player commandSender = (Player) sender;
                int playerLives = config.getInt(String.valueOf(commandSender.getUniqueId()) + "Lives");

                String[] messages = {
                        ChatColor.RED + "1",
                        ChatColor.YELLOW + "2",
                        ChatColor.GREEN + "3"
                };

                if(playerLives > 0 && playerLives <= messages.length) {
                    commandSender.playSound(commandSender.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                    commandSender.sendTitle(messages[playerLives - 1], "Your current lives are: " + playerLives, 20, 100, 20);
                }
            }

            return true;
        }

    }


    public class RollCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            Object[] playerList = Bukkit.getOnlinePlayers().toArray();
            ArrayList eligiblePlayers = new ArrayList();

            Bukkit.getOnlinePlayers().forEach(player -> {
                if(config.getInt(player.getUniqueId() + "Lives") > 1 ) {
                    eligiblePlayers.add(player);
                }

            });

            if(eligiblePlayers.size() > 0) {


                int selectedIndex = ThreadLocalRandom.current().nextInt(0, eligiblePlayers.size());

                Player selectedPlayer = (Player) eligiblePlayers.get(selectedIndex);

                Bukkit.getOnlinePlayers().forEach(player -> {
                    if(player != selectedPlayer) {
                        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 1.0f);
                        player.sendTitle(ChatColor.GREEN + player.getName(), "you are not the hit man", 20, 100, 20);
                    }
                });

                selectedPlayer.playSound(selectedPlayer.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 1.0f);
                selectedPlayer.sendTitle(ChatColor.RED + selectedPlayer.getName(), "you are the hit man", 20, 100, 20);

                config.set("Assassin", String.valueOf(selectedPlayer.getUniqueId()));
                saveConfig();
            }

            return false;
        }

    }

    @EventHandler
    public void playerHasJoined(PlayerJoinEvent event) {

        if(!(config.contains(event.getPlayer().getUniqueId() + "Lives"))){
            //Set Player Lives To Three
            config.set(String.valueOf(event.getPlayer().getUniqueId() + "Lives"),3);
            saveConfig();
        }

        //Change their tab color name
        int playerLives = config.getInt(event.getPlayer().getUniqueId() + "Lives");

        ChatColor[] colors = {
                ChatColor.GRAY,
                ChatColor.RED,
                ChatColor.YELLOW,
                ChatColor.GREEN
        };

        if(playerLives >= 0 && playerLives < colors.length) {
            event.getPlayer().setPlayerListName(colors[playerLives] + event.getPlayer().getName());
            event.getPlayer().setDisplayName(colors[playerLives] + event.getPlayer().getName());
        }
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {

        int playerLives = config.getInt(String.valueOf(event.getEntity().getUniqueId() + "Lives"));
        playerLives -= 1;

        //Adjust For Death
        config.set(String.valueOf(event.getEntity().getUniqueId() + "Lives"), playerLives);
        saveConfig();

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
        if(playerLives >= 0 && playerLives < colors.length) {
            event.getEntity().setPlayerListName(colors[playerLives] + event.getEntity().getName());
            event.getEntity().setDisplayName(colors[playerLives] + event.getEntity().getName() + ChatColor.WHITE);
        }

        if(playerLives > 0 && playerLives < messages.length) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 1.0f);
                player.sendTitle(event.getEntity().getDisplayName(), messages[playerLives - 1], 20, 100, 20);
            }
        }

        if (playerLives == 0) {
            event.getEntity().setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void playerDamage(EntityDamageByEntityEvent event) {

//        Entity aggressor = event.getDamager();
//        Entity defender = event.getEntity();
//        int aggressorLives = config.getInt(String.valueOf(aggressor.getUniqueId())+"Lives");
//        int defenderLives = config.getInt(String.valueOf(defender.getUniqueId())+"Lives");
//
//        //Entity receiving damage is player
//        if (event.getEntity() instanceof Player) {
//            //Aggressor Is Not Assassin
//            if(String.valueOf(aggressor.getUniqueId()) != config.getString("Assassin")) {
//                //Both Players Have More Than 2 Lives
//                if (aggressorLives > 1 && defenderLives > 1) {
//                    event.setCancelled(true);
//                }
//            }
//        }
    }


    @Override
    public void onDisable() {
        saveConfig();
        // Plugin shutdown logic
    }
}
