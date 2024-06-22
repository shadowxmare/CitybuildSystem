package de.flunar.citybuildsystem;

import de.flunar.citybuildsystem.commands.SetSpawnCommand;
import de.flunar.citybuildsystem.commands.SpawnCommand;
import de.flunar.citybuildsystem.listeners.PlayerJoinListener;
import de.flunar.citybuildsystem.listeners.PlayerQuitListener;
import de.flunar.citybuildsystem.managers.ConfigManager;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CitybuildSystem extends JavaPlugin {

    private static int taskId;

    private static CitybuildSystem instance;

    private ConfigManager configManager;
    private Location spawnLocation;

    @Override
    public void onEnable() {

        instance = this;

        configManager = new ConfigManager(this);
        configManager.loadConfig();
        FileConfiguration config = configManager.getConfig();

        // Load spawn location from config
        if (config.contains("spawn")) {
            spawnLocation = config.getLocation("spawn");
        } else {
            // Set default spawn location if not present in config
            spawnLocation = getServer().getWorlds().get(0).getSpawnLocation();
            config.set("spawn", spawnLocation);
            configManager.saveConfig();
        }

        Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.GREEN + "Das Citybuild System wurde gestartet!");
        Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.RED + "Code By Iownme_ / Shadowxmare");



        new PlayerJoinListener(this);
        new PlayerQuitListener(this);

        this.getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        this.getCommand("spawn").setExecutor(new SpawnCommand(this));




    }

    @Override
    public void onDisable() {
        configManager.saveConfig();
    }

    public static CitybuildSystem getInstance() {
        return instance;
    }

    public void startEventScheduler() {
        // TaskId wird zurückgegeben, um den Task später zu stoppen, falls erforderlich
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // Aktualisiere das Scoreboard für jeden Online-Spieler
                }
            }
        }, 0L, 100L); // 100L = 5 Sekunden (20 Ticks pro Sekunde)
    }

    public Location getSpawnLocation() {
        FileConfiguration config = getConfig();
        String worldName = config.getString("spawn.world");
        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");
        float yaw = (float) config.getDouble("spawn.yaw");
        float pitch = (float) config.getDouble("spawn.pitch");

        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            return new Location(world, x, y, z, yaw, pitch);
        } else {
            return null;
        }
    }

    public void setSpawnLocation(Location location) {
        spawnLocation = location;
        // Save spawn location to config
        configManager.getConfig().set("spawn", location);
        configManager.saveConfig();
    }

}
