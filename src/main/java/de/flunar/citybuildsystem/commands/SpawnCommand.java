package de.flunar.citybuildsystem.commands;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.managers.MySQLManager;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final CitybuildSystem plugin;
    private final MySQLManager mysqlManager;

    public SpawnCommand(CitybuildSystem plugin, MySQLManager mysqlManager) {
        this.plugin = plugin;
        this.mysqlManager = mysqlManager;
        plugin.getCommand("spawn").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Data.PREFIX + ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        int spawnId = 1; // ID der Spawn-Location
        Location spawnLocation = mysqlManager.getSpawnLocation(spawnId);
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
            player.sendMessage(Data.PREFIX + ChatColor.GREEN + "Du wurdest zum Spawn Teleportiert");
            player.playSound(spawnLocation, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
        } else {
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Ein fehler ist aufgetreten melde dies bitte im Support");
        }

        return true;
    }
}
