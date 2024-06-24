package de.flunar.citybuildsystem.managers;

import org.bukkit.plugin.Plugin;

public class DatabaseConfig {
    private Plugin plugin;
    private ConfigManager configManager;

    private String databaseHost;
    private int databasePort;
    private String databaseName;
    private String databaseUsername;
    private String databasePassword;

    public DatabaseConfig(Plugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;

        loadDatabaseConfig();
    }

    public void loadDatabaseConfig() {
        configManager.reloadConfig();

        databaseHost = configManager.getConfig().getString("database.host");
        databasePort = configManager.getConfig().getInt("database.port");
        databaseName = configManager.getConfig().getString("database.name");
        databaseUsername = configManager.getConfig().getString("database.username");
        databasePassword = configManager.getConfig().getString("database.password");
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public int getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }
}