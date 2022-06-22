package frogpeople.livesplugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class DamageHandler implements Listener {

    @EventHandler
    public void playerDamage(EntityDamageByEntityEvent event) {

        /*
        Entity aggressor = event.getDamager();
        Entity defender = event.getEntity();
        int aggressorLives = config.getInt(String.valueOf(aggressor.getUniqueId()) + "Lives");
        int defenderLives = config.getInt(String.valueOf(defender.getUniqueId()) + "Lives");

        //Entity receiving damage is player
        if (event.getEntity() instanceof Player) {
            //Aggressor Is Not Assassin
            if (String.valueOf(aggressor.getUniqueId()) != config.getString("Assassin")) {
                //Both Players Have More Than 2 Lives
                if (aggressorLives > 1 && defenderLives > 1) {
                    event.setCancelled(true);
                }
            }
        }

        */
    }
}
