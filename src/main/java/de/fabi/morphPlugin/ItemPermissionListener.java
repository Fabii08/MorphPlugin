/*
Copyright © 2025 https://github.com/Fabii08?tab=repositories  
All rights reserved.  
*/
package de.fabi.morphPlugin;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.PermissionAttachment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemPermissionListener implements Listener {

    private final Map<Component, String> itemPermissions = new HashMap<>();
    private final Map<Component, Material> morphItems = new HashMap<>();
    private final Map<UUID, PermissionAttachment> permissionsMap = new HashMap<>();
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ItemPermissionListener() {
        addItem(Material.PIG_SPAWN_EGG, "<gold>Schwein Verwandlung</gold>", "morph.schwein");
        addItem(Material.CHICKEN_SPAWN_EGG, "<gold>Huhn Verwandlung</gold>", "morph.huhn");
        addItem(Material.COW_SPAWN_EGG, "<gold>Kuh Verwandlung</gold>", "morph.kuh");
        addItem(Material.WOLF_SPAWN_EGG, "<gold>Wolf Verwandlung</gold>", "morph.wolf");
        addItem(Material.CAT_SPAWN_EGG, "<gold>Katze Verwandlung</gold>", "morph.katze");
        addItem(Material.SKELETON_SPAWN_EGG, "<gold>Skelett Verwandlung</gold>", "morph.skelett");
        addItem(Material.ZOMBIE_SPAWN_EGG, "<gold>Zombie Verwandlung</gold>", "morph.zombie");
        addItem(Material.SPIDER_SPAWN_EGG, "<gold>Spinne Verwandlung</gold>", "morph.spinne");
        addItem(Material.CREEPER_SPAWN_EGG, "<gold>Creeper Verwandlung</gold>", "morph.creeper");
        addItem(Material.SHEEP_SPAWN_EGG, "<gold>Schaf Verwandlung</gold>", "morph.schaf");
        addItem(Material.HUSK_SPAWN_EGG, "<gold>Husk Verwandlung</gold>", "morph.husk");
        addItem(Material.IRON_GOLEM_SPAWN_EGG, "<gold>Eisengolem Verwandlung</gold>", "morph.eisengolem");
        addItem(Material.ENDERMAN_SPAWN_EGG, "<gold>Enderman Verwandlung</gold>", "morph.enderman");
        addItem(Material.WITCH_SPAWN_EGG, "<gold>Hexen Verwandlung</gold>", "morph.hexe");
    }

    private void addItem(Material material, String displayName, String permission) {
        Component nameComponent = miniMessage.deserialize(displayName);
        itemPermissions.put(nameComponent, permission);
        morphItems.put(nameComponent, material);
    }

    public Map<Component, Material> getMorphItems() {
        return morphItems;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) return;

        Component itemName = meta.displayName();

        if (itemPermissions.containsKey(itemName)) {
            event.setCancelled(true);

            String permission = itemPermissions.get(itemName);

            // Prüfen, ob der Spieler die Permission bereits hat
            if (player.hasPermission(permission)) {
                player.sendMessage(miniMessage.deserialize(MorphPlugin.Prefix() + "<red>Du hast diese Verwandlung bereits freigeschaltet!"));
                return;
            }

            // Permission vergeben
            givePermission(player, permission);

            // Permission dauerhaft über LuckPerms setzen
            setPermanentPermission(player, permission);

            // Item entfernen
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().removeItem(item);
            }

            // Nachricht mit Verwandlungsname senden
            String transformationName = miniMessage.serialize(itemName);
            player.sendMessage(miniMessage.deserialize(MorphPlugin.Prefix() + "<green>Du Hast Die <yellow>" + transformationName + "</yellow> erhalten!"));
        }
    }

    private void givePermission(Player player, String permission) {
        PermissionAttachment attachment = permissionsMap.getOrDefault(player.getUniqueId(), player.addAttachment(Bukkit.getPluginManager().getPlugin("MorphPlugin")));
        attachment.setPermission(permission, true);
        permissionsMap.put(player.getUniqueId(), attachment);
    }

    private void setPermanentPermission(Player player, String permission) {
        // LuckPerms-Befehl ausführen, um die Permission dauerhaft zu setzen
        String command = "lp user " + player.getName() + " permission set " + permission + " true";
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
