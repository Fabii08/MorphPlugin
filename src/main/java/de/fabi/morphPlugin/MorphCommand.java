package de.fabi.morphPlugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MorphCommand implements CommandExecutor {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final MorphPlugin plugin;

    public MorphCommand(MorphPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.length == 0) {
                openMorphGUI(player);
                return true;
            }

            player.sendMessage(miniMessage.deserialize("<red>Ungültiges Argument. Verwende einfach /morph, um die GUI zu öffnen.</red>"));
            return false;
        }
        return false;
    }

    private void openMorphGUI(Player player) {
        Inventory morphMenu = Bukkit.createInventory(null, 18, miniMessage.deserialize("<dark_purple>Morph Menu</dark_purple>"));

        addItemToMorphMenu(morphMenu, Material.PIG_SPAWN_EGG, "<gold>Schwein</gold>", "morph.schwein", player);
        addItemToMorphMenu(morphMenu, Material.CHICKEN_SPAWN_EGG, "<gold>Huhn</gold>", "morph.huhn", player);
        addItemToMorphMenu(morphMenu, Material.COW_SPAWN_EGG, "<gold>Kuh</gold>", "morph.kuh", player);
        addItemToMorphMenu(morphMenu, Material.WOLF_SPAWN_EGG, "<gold>Wolf</gold>", "morph.wolf", player);
        addItemToMorphMenu(morphMenu, Material.CAT_SPAWN_EGG, "<gold>Katze</gold>", "morph.katze", player);
        addItemToMorphMenu(morphMenu, Material.SKELETON_SPAWN_EGG, "<gold>Skelett</gold>", "morph.skelett", player);
        addItemToMorphMenu(morphMenu, Material.ZOMBIE_SPAWN_EGG, "<gold>Zombie</gold>", "morph.zombie", player);
        addItemToMorphMenu(morphMenu, Material.SPIDER_SPAWN_EGG, "<gold>Spinne</gold>", "morph.spinne", player);
        addItemToMorphMenu(morphMenu, Material.CREEPER_SPAWN_EGG, "<gold>Creeper</gold>", "morph.creeper", player);
        addItemToMorphMenu(morphMenu, Material.SHEEP_SPAWN_EGG, "<gold>Schaf</gold>", "morph.schaf", player);
        addItemToMorphMenu(morphMenu, Material.HUSK_SPAWN_EGG, "<gold>Husk</gold>", "morph.husk", player);
        addItemToMorphMenu(morphMenu, Material.IRON_GOLEM_SPAWN_EGG, "<gold>Eisengolem</gold>", "morph.eisengolem", player);
        addItemToMorphMenu(morphMenu, Material.ENDERMAN_SPAWN_EGG, "<gold>Enderman</gold>", "morph.enderman", player);
        addItemToMorphMenu(morphMenu, Material.WITCH_SPAWN_EGG, "<gold>Hexe</gold>", "morph.hexe", player);

        // Barriere für Rückverwandlung hinzufügen
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.displayName(miniMessage.deserialize("<red>Rückverwandeln</red>"));
        barrier.setItemMeta(barrierMeta);
        morphMenu.setItem(17, barrier); // Die letzte Position im Inventar

        // Fülle leere Slots mit grauen Glasscheiben
        ItemStack grayGlassPane = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta grayGlassPaneMeta = grayGlassPane.getItemMeta();
        grayGlassPaneMeta.setDisplayName(" ");
        grayGlassPane.setItemMeta(grayGlassPaneMeta);

        for (int i = 0; i < morphMenu.getSize(); i++) {
            if (morphMenu.getItem(i) == null) {
                morphMenu.setItem(i, grayGlassPane);
            }
        }

        player.openInventory(morphMenu);
    }

    private void addItemToMorphMenu(Inventory morphMenu, Material material, String displayName, String permission, Player player) {
        if (player.hasPermission(permission)) {
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(miniMessage.deserialize(displayName));
            item.setItemMeta(meta);
            morphMenu.addItem(item);
        }
    }
}
