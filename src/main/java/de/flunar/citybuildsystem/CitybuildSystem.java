package de.flunar.citybuildsystem;

import de.flunar.citybuildsystem.commands.FarmweltCommand;
import de.flunar.citybuildsystem.commands.SetSpawnCommand;

import de.flunar.citybuildsystem.commands.SpawnCommand;
import de.flunar.citybuildsystem.listeners.ChatListener;
import de.flunar.citybuildsystem.listeners.PlayerDeathListener;
import de.flunar.citybuildsystem.listeners.PlayerJoinListener;
import de.flunar.citybuildsystem.listeners.PlayerQuitListener;
import de.flunar.citybuildsystem.managers.ConfigManager;
import de.flunar.citybuildsystem.managers.DatabaseConfig;
import de.flunar.citybuildsystem.managers.MySQLManager;
import de.flunar.citybuildsystem.managers.ProtectionManager;
import de.flunar.citybuildsystem.utils.Data;
import de.flunar.citybuildsystem.utils.DiscordWebhookSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.sql.*;

import static jdk.xml.internal.SecuritySupport.getResourceAsStream;

public final class CitybuildSystem extends JavaPlugin {

    private static CitybuildSystem instance;
    private ConfigManager configManager;
    private Location spawnLocation;

    private DatabaseConfig databaseConfig;
    private MySQLManager mysqlManager;
    private DiscordWebhookSender discordWebhookSender;


    @Override
    public void onEnable() {


        // Initialisiere den DiscordWebhookSender mit deiner Webhook-URL
        discordWebhookSender = new DiscordWebhookSender("https://discord.com/api/webhooks/1255104579486486610/2kWkbZ7WozKgYhrZsma0wZiBtBHaGIUu-dRa8h-BiV8HLyjaDZwlz0myWCL6oPK6ooCH");

        instance = this;

        configManager = new ConfigManager(this);
        databaseConfig = new DatabaseConfig(this, configManager);

        String host = databaseConfig.getDatabaseHost();
        int port = databaseConfig.getDatabasePort();
        String dbName = databaseConfig.getDatabaseName();
        String username = databaseConfig.getDatabaseUsername();
        String password = databaseConfig.getDatabasePassword();

        createDefaultConfig();
        // Verbindung zur Datenbank herstellen und Tabelle erstellen, falls nicht vorhanden
        connectToDatabase(host, port, dbName, username, password);

        // MySQLManager erstellen
        mysqlManager = new MySQLManager(host, port, dbName, username, password);
        mysqlManager.connect();


        Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.GREEN + "Das Citybuild System wurde gestartet!");
        Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.RED + "Code By Iownme_ / Shadowxmare");

        new PlayerJoinListener(mysqlManager, this);
        new PlayerQuitListener(this);
        new PlayerDeathListener(mysqlManager,this);
        getServer().getPluginManager().registerEvents(new ChatListener(this, discordWebhookSender), this);

        //Farmwelt
        new BukkitRunnable() {
            @Override
            public void run() {
                // Beispiel: Schutzbereich um den Spawnpunkt in der Welt "Farmwelt"
                Location farmweltSpawn = mysqlManager.getSpawnLocation(2); // Beispiel, hole die Spawn-Position aus der Datenbank
                if (farmweltSpawn != null) {
                    if ("Farmwelt".equals(farmweltSpawn.getWorld().getName())) {
                        new ProtectionManager(CitybuildSystem.this, farmweltSpawn, 100);
                    }
                } else {
                    getLogger().severe(Data.PREFIX + "Konnte den Spawn-Punkt nicht laden oder Welt existiert nicht.");
                }
            }
        }.runTaskLater(this, 100L); // 100 Ticks (5 Sekunden) warten

        //COMMANDS
        getCommand("setspawn").setExecutor(new SetSpawnCommand(this));

        new SpawnCommand(this, mysqlManager);
        new FarmweltCommand(this, mysqlManager);


    }

    @Override
    public void onDisable() {
        configManager.saveConfig();
        mysqlManager.disconnect();
    }

    public static CitybuildSystem getInstance() {
        return instance;
    }


    private void connectToDatabase(String host, int port, String dbName, String username, String password) {
        Connection connection = null;
        try {
            // Verbindung zur MySQL-Datenbank herstellen
            String jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/mysql?useSSL=false";
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Überprüfen, ob die Datenbank existiert, sonst erstellen
            if (!databaseExists(connection, dbName)) {
                createDatabase(connection, dbName);
            }

            // Verbindung zur spezifischen Datenbank herstellen
            jdbcUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false";
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Überprüfen, ob die Verbindung erfolgreich hergestellt wurde
            if (connection != null) {
                Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.GREEN + "Verbindung zur Datenbank hergestellt!");

                // Hier Tabelle erstellen, wenn sie nicht existiert
                createTableIfNotExists(connection);

                connection.close();
            } else {
                Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.RED + "Verbindung zur Datenbank fehlgeschlagen!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.RED + "Fehler beim Herstellen der Verbindung zur Datenbank: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private boolean databaseExists(Connection connection, String dbName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?");
        statement.setString(1, dbName);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    private void createDatabase(Connection connection, String dbName) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("CREATE DATABASE " + dbName);
        statement.executeUpdate();
        Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.GREEN + "Datenbank " + dbName + " erstellt!");
    }

    private void createTableIfNotExists(Connection connection) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS spawns_data (" +
                            "spawns INT NOT NULL," +  // Neue Spalte hinzugefügt
                            "world VARCHAR(255) NOT NULL," +
                            "x DOUBLE NOT NULL," +
                            "y DOUBLE NOT NULL," +
                            "z DOUBLE NOT NULL," +
                            "yaw FLOAT NOT NULL," +
                            "pitch FLOAT NOT NULL" +
                            ");"
            );
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void createDefaultConfig() {
        File configFile = new File(getDataFolder(), "config.yml");

        // Überprüfen, ob die Konfigurationsdatei bereits existiert
        if (!configFile.exists()) {
            if (!getDataFolder().exists()) {
                getDataFolder().mkdir();
            }

            try (InputStream is = getResourceAsStream("config.yml");
                 OutputStream os = new FileOutputStream(configFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public MySQLManager getMySQLManager() {
        return mysqlManager;
    }

}
