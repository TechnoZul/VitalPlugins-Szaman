package vitalplugins.vitalplugins_szaman;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import vitalplugins.vitalplugins_szaman.utils.Chat;
import vitalplugins.vitalplugins_szaman.utils.Helper;

import java.util.ArrayList;

public class EditGui implements Listener {

    private final JavaPlugin plugin;
    private final Helper helper;
    private final Database database;

    public EditGui(JavaPlugin plugin) {
        this.plugin = plugin;
        this.helper = new Helper(plugin);
        this.database = new Database(plugin);
    }

    private void open(Player player) {
        String guiTitle = Chat.color(helper.getNpcName() + " &8- edit");
        Gui gui = Gui.gui()
                .title(Component.text(guiTitle))
                .rows(1)
                .create();

        for (int i = 1; i <= 6; i++) {
            GuiItem perkEntrapment = ItemBuilder
                    .from(Material.BLACK_STAINED_GLASS_PANE)
                    .setName(" ")
                    .asGuiItem(event -> event.setCancelled(true));
            gui.setItem(i, perkEntrapment);
        }

        GuiItem info = ItemBuilder
                .from(Material.OAK_SIGN)
                .setName(Chat.color("&e&lInformacja"))
                .setLore(new ArrayList<>() {{
                    add(Chat.color("&fWsadź item, który ma być walutą w pusty slot"));
                    add(Chat.color("&fpotem kliknij przycisk &a&lZapisz"));
                }})
                .asGuiItem(event -> event.setCancelled(true));
        gui.setItem(7, info);

        GuiItem apply = ItemBuilder
                .from(Material.LIME_DYE)
                .setName(Chat.color("&a&lZapisz"))
                .asGuiItem(event -> {
                    try {
                        setCurrencyItem(gui.getInventory().getItem(0));
                        player.sendTitle(Chat.color(helper.getNpcName()), Chat.color("&aZapisano item jako walute"), 10, 100, 10);
                    } catch (Exception e) {
                        player.sendTitle(Chat.color(helper.getNpcName()), Chat.color("&cNie udało się zapisac itemu"), 10, 100, 10);
                    }
                    event.setCancelled(true);
                    gui.close(player);
                });
        gui.setItem(8, apply);

        gui.open(player);
    }

    public void setCurrencyItem(ItemStack item) {
        plugin.getConfig().set("currency.item-type", item.getType().name());
        plugin.getConfig().set("currency.item-name", item.getItemMeta().getDisplayName());
        plugin.saveConfig();
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (!(entity instanceof Player))
            return;

        if (!Chat.color(helper.getNpcName()).equals(entity.getCustomName()))
            return;

        if (!player.isSneaking())
            return;

        if (!player.isOp())
            return;

        open(player);
    }

}
