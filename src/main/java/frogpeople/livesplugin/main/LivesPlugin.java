package frogpeople.livesplugin.main;

import frogpeople.livesplugin.commands.LivesCommand;
import frogpeople.livesplugin.commands.RestartCommand;
import frogpeople.livesplugin.commands.RollCommand;
import frogpeople.livesplugin.listener.DamageHandler;
import frogpeople.livesplugin.listener.DeathHandler;
import frogpeople.livesplugin.listener.JoinHandler;
import frogpeople.livesplugin.util.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class LivesPlugin extends JavaPlugin {

    final PluginManager pm = Bukkit.getPluginManager();

    private static LivesPlugin instance;

    public static LivesPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        //Save The Default Config
        ConfigHandler.init();
        registerEvents();
        loadCommands();
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    private void loadCommands() {
        Objects.requireNonNull(getCommand("lives")).setExecutor(new LivesCommand());
        Objects.requireNonNull(getCommand("roll")).setExecutor(new RollCommand());
        Objects.requireNonNull(getCommand("restart")).setExecutor(new RestartCommand());
    }

    private void registerEvents() {
        this.pm.registerEvents(new DamageHandler(), this);
        this.pm.registerEvents(new DeathHandler(), this);
        this.pm.registerEvents(new JoinHandler(), this);
    }
}
