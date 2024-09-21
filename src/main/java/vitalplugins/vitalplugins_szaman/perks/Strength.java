package vitalplugins.vitalplugins_szaman.perks;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import vitalplugins.vitalplugins_szaman.Database;

public class Strength implements Listener {
    private final JavaPlugin plugin;
    private final Database database;

    public Strength(JavaPlugin plugin) {
        this.plugin = plugin;
        this.database = new Database(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();

        if (!(attacker instanceof Player)) {
            return;
        }

        int strengthLevel = database.getValue(attacker.getUniqueId(), "perk_strength");

        if (strengthLevel <= 0) {
            return;
        }

        double damage = event.getDamage();
        double increaseDamage = damage + strengthLevel;
        event.setDamage(increaseDamage);
    }
}
