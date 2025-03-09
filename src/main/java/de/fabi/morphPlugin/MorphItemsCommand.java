/*
Copyright © 2025 https://github.com/Fabii08?tab=repositories  
All rights reserved.  
*/
package de.fabi.morphPlugin;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class MorphItemsCommand implements CommandExecutor {

    private final Map<Component, Material> morphItems;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public MorphItemsCommand(Map<Component, Material> morphItems) {
        this.morphItems = morphItems;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cDieser Befehl kann nur von einem Spieler ausgeführt werden!");
            return true;
        }

        Player player = (Player) sender;

        for (Map.Entry<Component, Material> entry : morphItems.entrySet()) {
            ItemStack item = new ItemStack(entry.getValue());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.displayName(entry.getKey());
                item.setItemMeta(meta);
            }
            player.getInventory().addItem(item);
        }

        player.sendMessage(miniMessage.deserialize("<green>Alle Morph-Items wurden deinem Inventar hinzugefügt!"));
        return true;
    }
}
