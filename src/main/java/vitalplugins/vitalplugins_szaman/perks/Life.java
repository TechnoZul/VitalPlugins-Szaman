package vitalplugins.vitalplugins_szaman.perks;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import vitalplugins.vitalplugins_szaman.Database;

public class Life {
    private final JavaPlugin plugin;
    private final Database database;

    public Life(JavaPlugin plugin) {
        this.plugin = plugin;
        this.database = new Database(plugin);
    }

    public void updatePlayerHearth(Player player) {
        int lifeLevel = database.getValue(player.getUniqueId(), "perk_life");

        if (lifeLevel <= 0) {
            return;
        }

        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth + lifeLevel);
    }
}
