package de.flunar.citybuildsystem.listeners;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.managers.MySQLManager;
import de.flunar.citybuildsystem.scoreboard.TestScoreboard;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class PlayerJoinListener implements Listener {

    private final MySQLManager mysqlManager;
    private final CitybuildSystem plugin;
    private final Set<String> notifiedPlayers = new HashSet<>();

    public PlayerJoinListener(MySQLManager mysqlManager, CitybuildSystem plugin) {
        this.mysqlManager = mysqlManager;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
        String playerUUID = player.getUniqueId().toString();

        player.sendTitle(ChatColor.WHITE + "" + ChatColor.BOLD + "Citybuild", ChatColor.AQUA + "Willkommen " +  ChatColor.YELLOW + playerName, 10, 70, 20);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        event.setJoinMessage(ChatColor.GRAY + playerName + ChatColor.GRAY + ": " + ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "]");
        player.sendMessage(Data.PREFIX + ChatColor.RED + "Nutze " + ChatColor.YELLOW + "/cbhelp" + ChatColor.RED + " um Hilfe zu bekommen!");

        // Überprüfen, ob der Spieler bereits in der Datenbank vorhanden ist
        try {
            if (!mysqlManager.checkIfPlayerExists(playerUUID)) {
                // Spieler ist nicht in der Datenbank, also teleportieren und hinzufügen
                int spawnId = 1; // Hier die ID des gewünschten Spawn-Punktes verwenden
                Location spawnLocation = mysqlManager.getSpawnLocation(spawnId);
                if (spawnLocation != null) {
                    player.teleport(spawnLocation);
                } else {
                    player.sendMessage(Data.PREFIX + ChatColor.RED + "Spawn-Punkt mit der ID " + spawnId + " konnte nicht gefunden werden.");
                }

                // Spieler zur Datenbank hinzufügen
                mysqlManager.addPlayer(playerUUID, playerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Es gab ein Problem beim Überprüfen oder Hinzufügen deines Spielerdatensatzes.");
        }

        if (!notifiedPlayers.contains(playerName)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                }
            }.runTaskLater(plugin, 50L);
            notifiedPlayers.add(playerName);
        }

        new TestScoreboard(player);
    }
}
