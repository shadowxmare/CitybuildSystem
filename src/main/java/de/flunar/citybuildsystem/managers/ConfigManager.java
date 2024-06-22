package de.flunar.citybuildsystem.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = null;
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            // Generiere die Konfigurationsdatei, falls sie nicht existiert
            plugin.saveResource("config.yml", false);
        }

        // Lade die Konfigurationsdatei
        config = YamlConfiguration.loadConfiguration(configFile);

        // Überprüfe auf Updates der Konfigurationsdatei
        checkConfigUpdates();
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }

    public void saveConfig() {
        if (config != null) {
            try {
                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkConfigUpdates() {


        File defaultConfigFile = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultConfigFile);
        int defaultConfigVersion = defaultConfig.getInt("config-version", 1);
        int currentConfigVersion = config.getInt("config-version", 0);

        if (currentConfigVersion < defaultConfigVersion) {

            saveConfig();
        }
    }
}