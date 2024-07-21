package de.flunar.citybuildsystem.managers;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArmorStandRemover {

    private final CitybuildSystem plugin;
    private final MySQLManager mysqlManager;

    public ArmorStandRemover(CitybuildSystem plugin, MySQLManager mysqlManager) {
        this.plugin = plugin;
        this.mysqlManager = mysqlManager;
    }

    public void removeArmorStandsNearSpawn() {
        new BukkitRunnable() {
            @Override
            public void run() {
                int spawnId = 1; // ID der Spawn-Location
                Location spawnLocation = mysqlManager.getSpawnLocation(spawnId);
                if (spawnLocation != null) {
                    int radius = 10;
                    for (Entity entity : spawnLocation.getWorld().getEntities()) {
                        if (entity.getType() == EntityType.ARMOR_STAND && entity.getLocation().distance(spawnLocation) <= radius) {
                            entity.remove();
                        }
                    }
                    Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.GREEN + "All armor stands within " + radius + " blocks of the spawn have been removed.");
                } else {
                    Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.RED + "Failed to retrieve the spawn location. Armor stands not removed.");
                }
            }
        }.runTask(plugin);
    }
}
