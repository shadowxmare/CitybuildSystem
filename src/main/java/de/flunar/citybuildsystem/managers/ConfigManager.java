package de.flunar.citybuildsystem.managers;

import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigManager {
    private final Plugin plugin;
    private final File configFile;
    private FileConfiguration config;
    private final Object configLock = new Object();

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            createDefaultConfig();
        }

        loadConfig();
    }

    public void loadConfig() {
        synchronized (configLock) {
            config = YamlConfiguration.loadConfiguration(configFile);
        }
    }

    public FileConfiguration getConfig() {
        synchronized (configLock) {
            if (config == null) {
                loadConfig();
            }
            return config;
        }
    }

    public void saveConfig() {
        synchronized (configLock) {
            try {
                getConfig().save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reloadConfig() {
        loadConfig();
    }

    private void createDefaultConfig() {
        try {
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdir();
            }

            // Copy default config file from the JAR to the config folder if it doesn't exist
            try (InputStream defaultConfigStream = plugin.getResource("config.yml")) {
                if (defaultConfigStream != null) {
                    Files.copy(defaultConfigStream, configFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } else {

                    Bukkit.getConsoleSender().sendMessage(Data.PREFIX + "Default config.yml not found in the JAR.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}