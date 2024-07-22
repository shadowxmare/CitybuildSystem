package de.flunar.citybuildsystem.commands;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.managers.ProtectionManager;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndCommand implements CommandExecutor {

    private final CitybuildSystem plugin;

    public EndCommand(CitybuildSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Data.PREFIX + ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Player player = (Player) sender;
        World endWorld = Bukkit.getWorld("End");  // Ensure this matches the actual End world name

        if (endWorld == null) {
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Error-02");
            return true;
        }

        Location endSpawn = endWorld.getSpawnLocation();
        player.teleport(endSpawn);
        player.sendMessage(Data.PREFIX + ChatColor.GREEN + "Du wurdest in das Ende teleportiert");
        player.sendMessage(Data.PREFIX + ChatColor.RED + "ACHTUNG! PvP ist in dieser Welt aktiviert");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        // Create ProtectionManager for a 100-block radius around the End spawn
        new ProtectionManager(plugin, endSpawn, 100);

        return true;
    }
}
