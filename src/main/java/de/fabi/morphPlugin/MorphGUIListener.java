package de.fabi.morphPlugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public class MorphGUIListener implements Listener {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final MorphPlugin plugin;
    private final String MORPH_TAG = "morphed";
    private final String TEAM_NAME = "morphTeam";

    public MorphGUIListener(MorphPlugin plugin) {
        this.plugin = plugin;
        setupTeam();
    }

    private void setupTeam() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(TEAM_NAME);
        if (team == null) {
            team = scoreboard.registerNewTeam(TEAM_NAME);
        }
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Deserialize the title for comparison
        String title = miniMessage.serialize(event.getView().title()).replaceAll("<.*?>", "").trim();

        if (title.equals("Morph Menu")) {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            if (event.getCurrentItem() == null || !event.getCurrentItem().hasItemMeta()) {
                return;
            }

            // Deserialize the display name for comparison
            String displayName = miniMessage.serialize(event.getCurrentItem().getItemMeta().displayName()).replaceAll("<.*?>", "").trim();
            switch (displayName) {
                case "Schwein":
                    morphToAnimal(player, EntityType.PIG, "morph.schwein");
                    break;
                case "Huhn":
                    morphToAnimal(player, EntityType.CHICKEN, "morph.huhn");
                    break;
                case "Kuh":
                    morphToAnimal(player, EntityType.COW, "morph.kuh");
                    break;
                case "Wolf":
                    morphToAnimal(player, EntityType.WOLF, "morph.wolf");
                    break;
                case "Katze":
                    morphToAnimal(player, EntityType.CAT, "morph.katze");
                    break;
                case "Skelett":
                    morphToAnimal(player, EntityType.SKELETON, "morph.skelett");
                    break;
                case "Zombie":
                    morphToAnimal(player, EntityType.ZOMBIE, "morph.zombie");
                    break;
                case "Spinne":
                    morphToAnimal(player, EntityType.SPIDER, "morph.spinne");
                    break;
                case "Creeper":
                    morphToAnimal(player, EntityType.CREEPER, "morph.creeper");
                    break;
                case "Schaf":
                    morphToAnimal(player, EntityType.SHEEP, "morph.schaf");
                    break;
                case "Husk":
                    morphToAnimal(player, EntityType.HUSK, "morph.husk");
                    break;
                case "Eisengolem":
                    morphToAnimal(player, EntityType.IRON_GOLEM, "morph.eisengolem");
                    break;
                case "Enderman":
                    morphToAnimal(player, EntityType.ENDERMAN, "morph.enderman");
                    break;
                case "Hexe":
                    morphToAnimal(player, EntityType.WITCH, "morph.hexe");
                    break;
                case "Rückverwandeln":
                    demorph(player);
                    break;
                default:
                    return;
            }
            player.closeInventory();
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity().getScoreboardTags().contains(MORPH_TAG)) {
            event.setCancelled(true); // Verhindert Schaden an verwandelten Tieren
        }
    }

    private void morphToAnimal(Player player, EntityType entityType, String permission) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(miniMessage.deserialize("<red>Du hast keine Berechtigung, dich in ein " + entityType.name().toLowerCase() + " zu verwandeln!</red>"));
            return;
        }

        // Entferne bereits vorhandene Morphs
        demorph(player);

        // Verwandlungseffekte hinzufügen
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
        player.sendMessage(miniMessage.deserialize("<green>Du hast dich in ein " + entityType.name().toLowerCase() + " verwandelt!</green>"));

        // Tier-Entität erzeugen und dauerhaft zum Spieler teleportieren
        Entity entity = player.getWorld().spawnEntity(player.getLocation(), entityType);
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setAI(false); // Deaktiviert die KI, damit das Tier nicht herumlaufen kann
            livingEntity.setInvulnerable(true); // Macht das Tier unsterblich
            livingEntity.setSilent(true); // Macht das Tier stumm
            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, Integer.MAX_VALUE, 255, false, false)); // Maximale Verlangsamung

            // Füge die Entität und den Spieler dem Team hinzu
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            Team team = scoreboard.getTeam(TEAM_NAME);
            if (team != null) {
                team.addEntry(player.getName());
                team.addEntry(livingEntity.getUniqueId().toString());
            }
        }
        entity.addScoreboardTag(MORPH_TAG); // Tag hinzufügen
        entity.setCustomName(player.getUniqueId().toString());
        entity.setPersistent(true);
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            if (entity.isValid()) {
                // Berechne die Position 0,2 Blöcke hinter dem Spieler
                Location playerLocation = player.getLocation();
                Vector direction = playerLocation.getDirection().normalize();
                Location newLocation = playerLocation.subtract(direction.multiply(0.5));
                entity.teleport(newLocation);
            }
        }, 0L, 1L); // Teleportiere das Tier jede 1 Ticks (1/20 Sekunde) zum Spieler
    }

    private void demorph(Player player) {
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.sendMessage(miniMessage.deserialize(MorphPlugin.Prefix()+"<green>Du hast dich zurückverwandelt!</green>"));

        // Entferne das Tier, das zum Spieler teleportiert wurde
        player.getWorld().getEntities().stream()
                .filter(entity -> entity.getCustomName() != null && entity.getCustomName().equals(player.getUniqueId().toString()))
                .forEach(Entity::remove);

        // Entferne den Spieler aus dem Team
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam(TEAM_NAME);
        if (team != null) {
            team.removeEntry(player.getName());
        }
    }
}