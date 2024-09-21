package vitalplugins.vitalplugins_szaman.perks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import vitalplugins.vitalplugins_szaman.Database;
import vitalplugins.vitalplugins_szaman.utils.Chat;
import vitalplugins.vitalplugins_szaman.utils.Helper;

public class Lifesteal implements Listener {
    private final JavaPlugin plugin;
    private final Database database;
    private final Helper helper;

    public Lifesteal(JavaPlugin plugin) {
        this.plugin = plugin;
        this.database = new Database(plugin);
        this.helper = new Helper(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();

        if (!(attacker instanceof Player)) {
            return;
        }

        double lifestealLevel = database.getValue(attacker.getUniqueId(), "perk_lifesteal");

        if (lifestealLevel <= 0) {
            return;
        }

        if (Math.random() < (lifestealLevel / 100)) {
            Player attackerGetPlayer = Bukkit.getPlayer(attacker.getUniqueId());
            double maxHealth = attackerGetPlayer.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
            double currentHealth = attackerGetPlayer.getHealth();
            if (currentHealth < maxHealth) {
                double newHealth = Math.min(currentHealth + 2.0, maxHealth);
                attackerGetPlayer.setHealth(newHealth);
                attacker.sendMessage(Chat.color(helper.getLifestealMessage()));
            }
        }
    }
}
