package vitalplugins.vitalplugins_szaman;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import vitalplugins.vitalplugins_szaman.perks.Drop;
import vitalplugins.vitalplugins_szaman.perks.Entrapment;
import vitalplugins.vitalplugins_szaman.perks.Lifesteal;
import vitalplugins.vitalplugins_szaman.perks.Strength;

import java.util.UUID;

public final class Main extends JavaPlugin implements Listener {

    private Database database;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        database = new Database(this);
        database.createDatabase();

        getCommand("szaman").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new MainGui(this), this);
        getServer().getPluginManager().registerEvents(new EditGui(this), this);
        getServer().getPluginManager().registerEvents(new Strength(this), this);
        getServer().getPluginManager().registerEvents(new Lifesteal(this), this);
        getServer().getPluginManager().registerEvents(new Drop(this), this);
        getServer().getPluginManager().registerEvents(new Entrapment(this), this);
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        String nickname = player.getName();
        database.insert(uuid, nickname, 0, 0, 0, 0, 0);
        player.sendMessage(String.valueOf(database.getValue(uuid, "perk_life")));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        MainGui mainGui = new MainGui(this);
        mainGui.open((Player) sender);
        return true;
    }
}
