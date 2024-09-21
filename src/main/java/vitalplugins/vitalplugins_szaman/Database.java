package vitalplugins.vitalplugins_szaman;

import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import vitalplugins.vitalplugins_szaman.utils.Helper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

public class Database {

    private final DataSource dataSource;
    private final JavaPlugin plugin;
    private final Helper helper;

    public Database(JavaPlugin plugin) {
        this.plugin = plugin;
        this.helper = new Helper(plugin);
        this.dataSource = setupDataSource();
    }

    private DataSource setupDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(helper.getUrl());
        config.setUsername(helper.getUser());
        config.setPassword(helper.getPassword());
        config.setMaximumPoolSize(10);

        return new HikariDataSource(config);
    }

    private Connection openConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @SneakyThrows
    public void createDatabase() {
        String query = "CREATE TABLE IF NOT EXISTS vitalplugins_szaman (" +
                "uuid VARCHAR(36) NOT NULL, " +
                "nickname VARCHAR(64) NOT NULL, " +
                "perk_strength INT NOT NULL, " +
                "perk_life INT NOT NULL, " +
                "perk_lifesteal INT NOT NULL, " +
                "perk_drop INT NOT NULL, " +
                "perk_entrapment INT NOT NULL, " +
                "PRIMARY KEY (uuid)" +
                ");";

        try (Connection connection = openConnection(); Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(query);
        }
    }

    @SneakyThrows
    public void insert(UUID uuid, String nickname, Integer perk_strength, Integer perk_life, Integer perk_lifesteal, Integer perk_drop, Integer perk_entrapment) {
        String checkQuery = "SELECT COUNT(*) FROM vitalplugins_szaman WHERE uuid = ?";
        try (Connection connection = openConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {

            checkStmt.setString(1, String.valueOf(uuid));
            ResultSet rs = checkStmt.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {
                String insertQuery = "INSERT INTO vitalplugins_szaman (uuid, nickname, perk_strength, perk_life, perk_lifesteal, perk_drop, perk_entrapment) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setString(1, String.valueOf(uuid));
                    insertStmt.setString(2, nickname);
                    insertStmt.setInt(3, perk_strength);
                    insertStmt.setInt(4, perk_life);
                    insertStmt.setInt(5, perk_lifesteal);
                    insertStmt.setInt(6, perk_drop);
                    insertStmt.setInt(7, perk_entrapment);
                    insertStmt.executeUpdate();
                }
            } else {
                plugin.getLogger().info("Record with UUID " + uuid + " already exists. Skipping insert.");
            }
        }
    }

    @SneakyThrows
    public int getValue(UUID uuid, String value) {
        String query = "SELECT " + value + " FROM vitalplugins_szaman WHERE uuid = ?";
        try (Connection connection = openConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, String.valueOf(uuid));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        }
    }

    @SneakyThrows
    public void updateValue(UUID uuid, String column, int newValue) {
        String query = "UPDATE vitalplugins_szaman SET " + column + " = ? WHERE uuid = ?";
        try (Connection connection = openConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, newValue);
            stmt.setString(2, String.valueOf(uuid));
            stmt.executeUpdate();
        }
    }
}
