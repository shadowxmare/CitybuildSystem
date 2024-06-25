package de.flunar.citybuildsystem.listeners;


import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.managers.MySQLManager;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {

    private final MySQLManager mysqlManager;
    private final CitybuildSystem plugin;

    public PlayerDeathListener(MySQLManager mysqlManager, CitybuildSystem plugin) {
        this.mysqlManager = mysqlManager;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        player.sendMessage(Data.PREFIX + ChatColor.RED + "Du bist gestorben! Du wirst beim Wiederbeleben zum Spawn teleportiert.");
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        int spawnId = 1; // ID der Spawn-Location
        Location spawnLocation = mysqlManager.getSpawnLocation(spawnId);
        if (spawnLocation != null) {
            event.setRespawnLocation(spawnLocation);
            player.sendMessage(Data.PREFIX + ChatColor.GREEN + "Du wurdest zum Spawn teleportiert.");
        } else {
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Fehler beim Teleportieren zum Spawn. Bitte kontaktiere einen Administrator.");
        }
    }
}
