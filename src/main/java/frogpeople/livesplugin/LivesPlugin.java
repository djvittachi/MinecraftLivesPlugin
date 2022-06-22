package frogpeople.livesplugin;

import org.bukkit.Bukkit;

import java.util.ArrayList;
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

        getCommand("lives").setExecutor(new LivesCommand());
        getCommand("roll").setExecutor(new RollCommand());
    }

    public class LivesCommand implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            Player commandSender = (Player) sender;
            int currentLives = config.getInt(String.valueOf(commandSender.getUniqueId()) + "Lives");

            if(currentLives == 3) {
                commandSender.playSound(commandSender.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1.0f,1.0f);
                commandSender.sendTitle(ChatColor.GREEN + "3", "" , 1, 10, 1);
            }

            else if(currentLives == 2) {
                commandSender.playSound(commandSender.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1.0f,1.0f);
                commandSender.sendTitle(ChatColor.YELLOW + "2", "" , 1, 10, 1);

            }

            else if (currentLives == 1) {
                commandSender.playSound(commandSender.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1.0f,1.0f);
                commandSender.sendTitle(ChatColor.RED+ "1", "" , 1, 10, 1);

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
                        player.sendTitle(ChatColor.GREEN+ player.getDisplayName(), "you are not the hitman", 1, 10, 1);
                    }
                });

                selectedPlayer.playSound(selectedPlayer.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 1.0f);
                selectedPlayer.sendTitle(ChatColor.RED+ selectedPlayer.getDisplayName(), "you are the hitman", 1, 10, 1);

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

        if(playerLives == 3) {
            event.getPlayer().setPlayerListName(ChatColor.GREEN + event.getPlayer().getDisplayName());
            event.getPlayer().setDisplayName(ChatColor.GREEN + event.getPlayer().getDisplayName());
        }

        else if(playerLives == 2) {
            event.getPlayer().setPlayerListName(ChatColor.YELLOW + event.getPlayer().getDisplayName());
            event.getPlayer().setDisplayName(ChatColor.YELLOW + event.getPlayer().getDisplayName());
        }

        else if(playerLives == 1){
            event.getPlayer().setPlayerListName(ChatColor.RED + event.getPlayer().getDisplayName());
            event.getPlayer().setDisplayName(ChatColor.RED + event.getPlayer().getDisplayName());
        }

        else{
            event.getPlayer().setPlayerListName(ChatColor.GRAY + event.getPlayer().getDisplayName());
            event.getPlayer().setDisplayName(ChatColor.GRAY+ event.getPlayer().getDisplayName());

        }

    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent event){

        int currentLives = config.getInt(String.valueOf(event.getEntity().getUniqueId() + "Lives"));
        currentLives -= 1;

        //Adjust For Death
        config.set(String.valueOf(event.getEntity().getUniqueId() + "Lives"),currentLives);
        saveConfig();

        //Moves Into The Yellow Zone
        if(currentLives == 2) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH,1.0f,1.0f);
                player.sendTitle(event.getEntity().getDisplayName(), ChatColor.YELLOW + "Is in the yellow-zone", 1, 10, 1);

            }
            event.getEntity().setPlayerListName(ChatColor.YELLOW + event.getEntity().getDisplayName());
            event.getEntity().setDisplayName(ChatColor.YELLOW + event.getEntity().getDisplayName());
        }

        //Moves Into The Red Zone
        else if(currentLives == 1) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH,1.0f,1.0f);
                player.sendTitle(event.getEntity().getDisplayName(), ChatColor.RED + "Is in the red-zone", 1, 10, 1);
            }
            event.getEntity().setPlayerListName(ChatColor.RED + event.getEntity().getDisplayName());
            event.getEntity().setDisplayName(ChatColor.RED + event.getEntity().getDisplayName());
        }

        //Is Eliminated
        if(currentLives == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH,1.0f,1.0f);
                player.sendTitle(event.getEntity().getDisplayName(), ChatColor.RED + "Has Been Eliminated", 1, 10, 1);
            }
            event.getEntity().setPlayerListName(ChatColor.GRAY + event.getEntity().getDisplayName());
            event.getEntity().setDisplayName(ChatColor.GRAY+ event.getEntity().getDisplayName());
            event.getEntity().setGameMode(GameMode.SPECTATOR);
        }

    }

    @EventHandler
    public void playerDamage(EntityDamageByEntityEvent event) {


        Entity aggressor = event.getDamager();
        Entity defender = event.getEntity();
        int aggressorLives = config.getInt(String.valueOf(aggressor.getUniqueId())+"Lives");
        int defenderLives = config.getInt(String.valueOf(defender.getUniqueId())+"Lives");


        //Entity receiving damage is player
        if (event.getEntity() instanceof Player) {
            //Aggressor Is Not Assassin
            if(String.valueOf(aggressor.getUniqueId()) != config.getString("Assassin")) {
                //Both Players Have More Than 2 Lives
                if (aggressorLives > 1 && defenderLives > 1) {
                    event.setCancelled(true);
                }
            }
        }
    }


    @Override
    public void onDisable() {
        saveConfig();
        // Plugin shutdown logic
    }
}
