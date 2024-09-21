package vitalplugins.vitalplugins_szaman.perks;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import vitalplugins.vitalplugins_szaman.Database;
import vitalplugins.vitalplugins_szaman.utils.Chat;
import vitalplugins.vitalplugins_szaman.utils.Helper;

public class Drop implements Listener {
    private final JavaPlugin plugin;
    private final Database database;
    private final Helper helper;

    public Drop(JavaPlugin plugin) {
        this.plugin = plugin;
        this.database = new Database(plugin);
        this.helper = new Helper(plugin);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMining(BlockBreakEvent event) {
        Player player = event.getPlayer();
        double dropLevel = database.getValue(player.getUniqueId(), "perk_drop");

        if (dropLevel <= 0) {
            return;
        }

        if (Math.random() < (dropLevel / 100)) {
            Material blockType = event.getBlock().getType();
            Material[] blockItem = {
                    Material.SHULKER_BOX
            };

            for (Material ore : blockItem) {
                if (blockType != ore) {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), event.getBlock().getDrops().iterator().next());
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), event.getBlock().getDrops().iterator().next());
                    event.setDropItems(false);
                    player.sendMessage(Chat.color(helper.getDropMessage()));
                    return;
                }
            }
        }

    }
}
