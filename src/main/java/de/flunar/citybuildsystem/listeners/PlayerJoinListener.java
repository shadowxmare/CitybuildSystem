package de.flunar.citybuildsystem.listeners;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.managers.MySQLManager;
import de.flunar.citybuildsystem.scoreboard.TestScoreboard;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    private final MySQLManager mysqlManager;
    private final CitybuildSystem plugin;

    public PlayerJoinListener(MySQLManager mysqlManager, CitybuildSystem plugin) {
        this.mysqlManager = mysqlManager;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();

        player.sendTitle(ChatColor.WHITE + "" + ChatColor.BOLD + "Citybuild", ChatColor.YELLOW + playerName, 10, 70, 20);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        event.setJoinMessage(ChatColor.GRAY + playerName + ChatColor.GRAY + ": " + ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "]");


        new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(Data.PREFIX + ChatColor.RED + "Nutze " + ChatColor.YELLOW + "/cbhelp" + ChatColor.RED + " um Hilfe zu bekommen!");
            }
        }.runTaskLater(plugin, 50L);

        new TestScoreboard(player);

        // Beispiel für die Verwendung von MySQLManager
        int spawnId = 1; // Hier die ID einfügen, die du verwenden möchtest
        if (mysqlManager != null) {
            // Beispiel für das Abrufen der Spawn-Location und Teleportation
            player.teleport(mysqlManager.getSpawnLocation(spawnId));
        }
    }
}
