package vitalplugins.vitalplugins_szaman.utils;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Helper {

    private JavaPlugin plugin;
    private FileConfiguration pluginConfig;

    public Helper(JavaPlugin plugin) {
        this.plugin = plugin;
        this.pluginConfig = plugin.getConfig();
    }

    public String getHost() {
        return pluginConfig.getString("mysql.host");
    }

    public String getPort() {
        return pluginConfig.getString("mysql.port");
    }

    public String getName() {
        return pluginConfig.getString("mysql.name");
    }

    public String getUser() {
        return pluginConfig.getString("mysql.user");
    }

    public String getPassword() {
        return pluginConfig.getString("mysql.password");
    }

    public String getUrl() {
        return "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getName();
    }

    public String getNpcName() {
        return pluginConfig.getString("npc-name");
    }

    public String getCurrencyItemType() {
        return pluginConfig.getString("currency.item-type");
    }

    public String getCurrencyItemName() {
        return pluginConfig.getString("currency.item-name");
    }

    public String getCurrencyItemLore() {
        return pluginConfig.getString("currency.item-lore");
    }

    public String getStartValueStrength() {
        return pluginConfig.getString("start-value.strength");
    }

    public String getStartValueLife() {
        return pluginConfig.getString("start-value.life");
    }

    public String getStartValueLifesteal() {
        return pluginConfig.getString("start-value.lifesteal");
    }

    public String getStartValueDrop() {
        return pluginConfig.getString("start-value.drop");
    }

    public String getStartValueEntrapment() {
        return pluginConfig.getString("start-value.entrapment");
    }

    public String getLifestealMessage() {
        return pluginConfig.getString("messages.lifesteal");
    }

    public String getDropMessage() {
        return pluginConfig.getString("messages.drop");
    }

    public String getEntrapmentAttackerMessage() {
        return pluginConfig.getString("messages.entrapment.attacker");
    }

    public String getEntrapmentVictimMessage() {
        return pluginConfig.getString("messages.entrapment.victim");
    }


}
