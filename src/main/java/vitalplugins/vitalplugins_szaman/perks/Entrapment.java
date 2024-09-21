package vitalplugins.vitalplugins_szaman.perks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import vitalplugins.vitalplugins_szaman.Database;
import vitalplugins.vitalplugins_szaman.utils.Chat;
import vitalplugins.vitalplugins_szaman.utils.Helper;

import java.util.*;

public class Entrapment implements Listener {
    private final List<UUID> entrapmentPlayer = new ArrayList<>();

    private final JavaPlugin plugin;
    private final Database database;
    private final Helper helper;

    public Entrapment(JavaPlugin plugin) {
        this.plugin = plugin;
        this.database = new Database(plugin);
        this.helper = new Helper(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Entity attacker = event.getEntity();
        Entity victim = event.getEntity();

        if (!(attacker instanceof Player) || !(victim instanceof Player)) {
            return;
        }

        double entrapmentLevel = database.getValue(attacker.getUniqueId(), "perk_entrapment");

        if (entrapmentLevel <= 0) {
            return;
        }

        if (Math.random() < (entrapmentLevel / 100)) {
            startEntrapment((Player) victim);
            attacker.sendMessage(Chat.color(helper.getEntrapmentAttackerMessage().replace("{PLAYER}", victim.getName())));
            victim.sendMessage(Chat.color(helper.getEntrapmentVictimMessage()));
        }
    }

    private void startEntrapment(Player player) {
        UUID uuid = player.getUniqueId();
        if (entrapmentPlayer.contains(uuid)) {
            entrapmentPlayer.remove(uuid);
        }
        entrapmentPlayer.add(uuid);
        new BukkitRunnable() {
            int countdown = 5;

            @Override
            public void run() {
                if (countdown <= 0) {
                    entrapmentPlayer.remove(uuid);
                    cancel();
                    return;
                }
                countdown--;
            }
        }.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (entrapmentPlayer.contains(player.getUniqueId())) {
            if (event.getItem() != null && event.getItem().getType() == org.bukkit.Material.FIREWORK_ROCKET) {
                event.setCancelled(true);
                player.sendMessage(Chat.color(helper.getEntrapmentVictimMessage()));
            }
        }
    }
}
