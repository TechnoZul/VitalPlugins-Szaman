package vitalplugins.vitalplugins_szaman;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import java.text.DecimalFormat;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import vitalplugins.vitalplugins_szaman.perks.Life;
import vitalplugins.vitalplugins_szaman.utils.Chat;
import vitalplugins.vitalplugins_szaman.utils.Helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MainGui implements Listener {

    private final JavaPlugin plugin;
    private final Helper helper;
    private final Database database;
    private final Life life;

    public MainGui(JavaPlugin plugin) {
        this.plugin = plugin;
        this.helper = new Helper(plugin);
        this.database = new Database(plugin);
        this.life = new Life(plugin);
    }

    public void open(Player player) {
        UUID uuid = player.getUniqueId();

        String guiTitle = Chat.color(helper.getNpcName());
        Gui gui = Gui.gui()
                .title(Component.text(guiTitle))
                .rows(5)
                .create();

        HashMap<Integer, Material> glass = new HashMap<>() {{
            put(0,  Material.LIGHT_BLUE_STAINED_GLASS_PANE);
            put(1,  Material.BLUE_STAINED_GLASS_PANE);
            put(9,  Material.BLUE_STAINED_GLASS_PANE);

            put(2,  Material.WHITE_STAINED_GLASS_PANE);

            put(6,  Material.WHITE_STAINED_GLASS_PANE);

            put(7,  Material.BLUE_STAINED_GLASS_PANE);
            put(8,  Material.LIGHT_BLUE_STAINED_GLASS_PANE);
            put(17,  Material.BLUE_STAINED_GLASS_PANE);

            put(18,  Material.WHITE_STAINED_GLASS_PANE);
            put(26,  Material.WHITE_STAINED_GLASS_PANE);

            put(27,  Material.BLUE_STAINED_GLASS_PANE);
            put(36,  Material.LIGHT_BLUE_STAINED_GLASS_PANE);
            put(37,  Material.BLUE_STAINED_GLASS_PANE);

            put(38,  Material.WHITE_STAINED_GLASS_PANE);

            put(42,  Material.WHITE_STAINED_GLASS_PANE);

            put(35,  Material.BLUE_STAINED_GLASS_PANE);
            put(44,  Material.LIGHT_BLUE_STAINED_GLASS_PANE);
            put(43,  Material.BLUE_STAINED_GLASS_PANE);
        }};


        for (Integer key : glass.keySet()) {
            GuiItem guiItem = ItemBuilder.from(glass.get(key)).setName(" ").asGuiItem(event -> {
                event.setCancelled(true);
            });

            gui.setItem(key, guiItem);
        }

        int perkStrengthLevel = database.getValue(uuid, "perk_strength");
        GuiItem perkStrength = ItemBuilder
                .from(Material.POTION)
                .setName(Chat.color("&c&lPerk Siły"))
                .setLore(new ArrayList<>() {{
                    String unlockCheck = (perkStrengthLevel >= 0) ? String.valueOf(perkStrengthLevel) : "Nieodblokowano";
                    add(Chat.color(String.format("&8>> &7Aktualny poziom: %s%s",
                            (perkStrengthLevel > 0) ? "&a" : "&c", unlockCheck)));
                    add("");

                    for (int i = 1; i <= 4; i++) {
                        String levelColor = (perkStrengthLevel >= i) ? "&a" : "&c";

                        add(Chat.color(String.format("&8× %sZwiększa twoje obrażenia o %s⚔",
                                levelColor, i)));
                    }

                    if (perkStrengthLevel < 4) {
                        add("");
                        add(Chat.color("&8>> &7Koszt: &5&l" + Integer.parseInt(helper.getStartValueStrength()) * (perkStrengthLevel + 1) + "x " + helper.getCurrencyItemName()));
                        add("");
                        add(Chat.color("&eKliknij, aby ulepszyć"));
                    }
                }})
                .flags(ItemFlag.HIDE_POTION_EFFECTS)
                .glow(true)
                .asGuiItem(event -> {
                    buyUpgrade(player, "perk_strength", perkStrengthLevel);
                    event.setCancelled(true);
                    gui.close(player);
                });
        gui.setItem(20, perkStrength);


        int perkLifeLevel = database.getValue(uuid, "perk_life");
        DecimalFormat decimalFormat = new DecimalFormat("0.#");
        GuiItem perkLife = ItemBuilder
                .from(Material.RED_DYE)
                .setName(Chat.color("&2&lPerk Życia"))
                .setLore(new ArrayList<>() {{
                    String unlockCheck = (perkLifeLevel >= 0) ? String.valueOf(perkLifeLevel) : "Nieodblokowano";
                    add(Chat.color(String.format("&8>> &7Aktualny poziom: %s%s",
                            (perkLifeLevel > 0) ? "&a" : "&c", unlockCheck)));
                    add("");

                    for (int i = 1; i <= 4; i++) {
                        String levelColor = (perkLifeLevel >= i) ? "&a" : "&c";
                        float lifeIncrease = (float) i;

                        add(Chat.color(String.format("&8× %sDodatkowe życie %s❤",
                                levelColor, decimalFormat.format(lifeIncrease / 2))));
                    }

                    if (perkLifeLevel < 4) {
                        add("");
                        add(Chat.color("&8>> &7Koszt: &5&l" + Integer.parseInt(helper.getStartValueLife()) * (perkLifeLevel + 1) + "x " + helper.getCurrencyItemName()));
                        add("");
                        add(Chat.color("&eKliknij, aby ulepszyć"));
                    }
                }})
                .asGuiItem(event -> {
                    buyUpgrade(player, "perk_life", perkLifeLevel);
                    event.setCancelled(true);
                    gui.close(player);
                });
        gui.setItem(21, perkLife);


        int perkLifestealLevel = database.getValue(uuid, "perk_lifesteal");
        GuiItem perkLifesteal = ItemBuilder
                .from(Material.NETHERITE_SWORD)
                .setName(Chat.color("&4&lPerk Wampiryzmu"))
                .setLore(new ArrayList<>() {{
                    String unlockCheck = (perkLifestealLevel >= 0) ? String.valueOf(perkLifestealLevel) : "Nieodblokowano";
                    add(Chat.color(String.format("&8>> &7Aktualny poziom: %s%s",
                            (perkLifestealLevel > 0) ? "&a" : "&c", unlockCheck)));
                    add("");

                    for (int i = 1; i <= 4; i++) {
                        String levelColor = (perkLifestealLevel >= i) ? "&a" : "&c";

                        add(Chat.color(String.format("&8× %s%s%% szans na wyleczenie",
                                levelColor, i)));
                    }

                    if (perkLifestealLevel < 4) {
                        add("");
                        add(Chat.color("&8>> &7Koszt: &5&l" + Integer.parseInt(helper.getStartValueLifesteal()) * (perkLifestealLevel + 1) + "x " + helper.getCurrencyItemName()));
                        add("");
                        add(Chat.color("&eKliknij, aby ulepszyć"));
                    }
                }})
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .asGuiItem(event -> {
                    buyUpgrade(player, "perk_lifesteal", perkLifestealLevel);
                    event.setCancelled(true);
                    gui.close(player);
                });
        gui.setItem(22, perkLifesteal);

        int perkDropLevel = database.getValue(uuid, "perk_drop");
        GuiItem perkDrop = ItemBuilder
                .from(Material.NETHERITE_PICKAXE)
                .setName(Chat.color("&9&lPerk Dropu"))
                .setLore(new ArrayList<>() {{
                    String unlockCheck = (perkDropLevel >= 0) ? String.valueOf(perkDropLevel) : "Nieodblokowano";
                    add(Chat.color(String.format("&8>> &7Aktualny poziom: %s%s",
                            (perkDropLevel > 0) ? "&a" : "&c", unlockCheck)));
                    add("");

                    for (int i = 1; i <= 4; i++) {
                        String levelColor = (perkDropLevel >= i) ? "&a" : "&c";

                        add(Chat.color(String.format("&8× %s%s%% szansa na podwójny drop z bloku",
                                levelColor, i)));
                    }

                    if (perkDropLevel < 4) {
                        add("");
                        add(Chat.color("&8>> &7Koszt: &5&l" + Integer.parseInt(helper.getStartValueDrop()) * (perkDropLevel + 1) + "x " + helper.getCurrencyItemName()));
                        add("");
                        add(Chat.color("&eKliknij, aby ulepszyć"));
                    }
                }})
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .asGuiItem(event -> {
                    buyUpgrade(player, "perk_drop", perkDropLevel);
                    event.setCancelled(true);
                    gui.close(player);
                });
        gui.setItem(23, perkDrop);

        int perkEntrapmentLevel = database.getValue(uuid, "perk_entrapment");
        GuiItem perkEntrapment = ItemBuilder
                .from(Material.SHIELD)
                .setName(Chat.color("&5&lPerk Uwięzienia"))
                .setLore(new ArrayList<>() {{
                    add(Chat.color("&8>> &7Po &fuderzeniu &7gracza masz &f5% szans"));
                    add(Chat.color("&8>> &7na &cuniemożliwienie &7mu korzystania"));
                    add(Chat.color("&8>> &7z &ffajerwerek &7na &fkrótki czas"));
                    add("");
                    String unlockCheck = (perkEntrapmentLevel >= 0) ? String.valueOf(perkEntrapmentLevel) : "Nieodblokowano";
                    add(Chat.color(String.format("&8>> &7Aktualny poziom: %s%s",
                            (perkEntrapmentLevel > 0) ? "&a" : "&c", unlockCheck)));
                    add("");

                    for (int i = 1; i <= 4; i++) {
                        String levelColor = (perkEntrapmentLevel >= i) ? "&a" : "&c";

                        add(Chat.color(String.format("&8× %sUwięzienie na %s sekundy",
                                levelColor, i + 1)));
                    }

                    if (perkEntrapmentLevel < 4) {
                        add("");
                        add(Chat.color("&8>> &7Koszt: &5&l" + Integer.parseInt(helper.getStartValueEntrapment()) * (perkEntrapmentLevel + 1) + "x " + helper.getCurrencyItemName()));
                        add("");
                        add(Chat.color("&eKliknij, aby ulepszyć"));
                    }
                }})
                .asGuiItem(event -> {
                        buyUpgrade(player, "perk_entrapment", perkEntrapmentLevel);
                        event.setCancelled(true);
                        gui.close(player);
                    });
        gui.setItem(24, perkEntrapment);


        gui.open(player);
    }

    public void buyUpgrade(Player player, String perk, Integer currentLevel) {
        if (currentLevel >= 4) {
            player.sendTitle(Chat.color(helper.getNpcName()), Chat.color("&cNie możesz dalej ulepszać tego perka"), 10, 100, 10);
            return;
        }

        int cost = 0;

        switch (perk) {
            case "perk_strength":
                cost = Integer.parseInt(helper.getStartValueStrength()) * (currentLevel + 1);
                break;
            case "perk_life":
                cost = Integer.parseInt(helper.getStartValueLife()) * (currentLevel + 1);
                break;
            case "perk_lifesteal":
                cost = Integer.parseInt(helper.getStartValueLifesteal()) * (currentLevel + 1);
                break;
            case "perk_drop":
                cost = Integer.parseInt(helper.getStartValueDrop()) * (currentLevel + 1);
                break;
            case "perk_entrapment":
                cost = Integer.parseInt(helper.getStartValueEntrapment()) * (currentLevel + 1);
                break;
            default:
                player.sendMessage(Chat.color("&cNieznany perk."));
                return;
        }

        if (!hasCurrency(player, cost)) {
            player.sendTitle(Chat.color(helper.getNpcName()), Chat.color("&cNie masz wystarczającej ilości przedmiotów"), 10, 100, 10);
            return;
        }

        removeCurrency(player, cost);
        database.updateValue(player.getUniqueId(), perk, currentLevel + 1);

        if (perk == "perk_life") {
            life.updatePlayerHearth(player);
        }

        player.sendTitle(Chat.color(helper.getNpcName()), Chat.color("&aUlepszyłeś swój perk o poziom wyżej"), 10, 100, 10);
    }

    private void removeCurrency(Player player, int amount) {
        Material itemType = Material.valueOf(helper.getCurrencyItemType());
        String itemName = helper.getCurrencyItemName();

        int toRemove = amount;

        for (ItemStack item : player.getInventory()) {
            if (item != null && item.getType() == itemType && item.getItemMeta() != null && item.getItemMeta().getDisplayName().equals(itemName)) {
                int itemAmount = item.getAmount();
                if (itemAmount >= toRemove) {
                    item.setAmount(itemAmount - toRemove);
                    break;
                } else {
                    toRemove -= itemAmount;
                    item.setAmount(0);
                }
            }
        }
    }

    private boolean hasCurrency(Player player, int requiredAmount) {
        int count = 0;
        Material itemType = Material.valueOf(helper.getCurrencyItemType());
        String itemName = helper.getCurrencyItemName();

        for (ItemStack item : player.getInventory()) {
            if (item != null && item.getType() == itemType) {
                ItemMeta meta = item.getItemMeta();
                if (meta != null && meta.getDisplayName().equals(itemName)) {
                    count += item.getAmount();
                }
            }
        }
        return count >= requiredAmount;
    }


    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();

        if (!(entity instanceof Player))
            return;

        if (!Chat.color(helper.getNpcName()).equals(entity.getCustomName()))
            return;

        if (player.isSneaking())
            return;

        open(player);
    }

}
