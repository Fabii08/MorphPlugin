/*
Copyright © 2025 https://github.com/Fabii08?tab=repositories  
All rights reserved.  
*/
package de.fabi.morphPlugin;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

public class MorphResetListener implements Listener {

    private final MorphPlugin plugin;

    public MorphResetListener(MorphPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        demorph(player);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        demorph(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        demorph(player);
    }

    private void demorph(Player player) {
        player.removePotionEffect(PotionEffectType.INVISIBILITY);

        // Entferne das Tier, das zum Spieler teleportiert wurde
        player.getWorld().getEntities().stream()
                .filter(entity -> entity.getCustomName() != null && entity.getCustomName().equals(player.getUniqueId().toString()))
                .forEach(Entity::remove);
    }
}
