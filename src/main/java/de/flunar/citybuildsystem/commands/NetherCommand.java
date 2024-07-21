package de.flunar.citybuildsystem.commands;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.managers.ProtectionManager;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NetherCommand implements CommandExecutor {

    private final CitybuildSystem plugin;

    public NetherCommand(CitybuildSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Data.PREFIX + ChatColor.RED + "Only players can execute this command!");
            return true;
        }

        Player player = (Player) sender;
        World netherWorld = Bukkit.getWorld("Nether");  // Ensure this matches the actual Nether world name

        if (netherWorld == null) {
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Error-01");
            return true;
        }

        Location netherSpawn = netherWorld.getSpawnLocation();
        player.teleport(netherSpawn);
        player.sendMessage(Data.PREFIX + ChatColor.GREEN + "Du wurdest in denn Nether Teleportiert");
        player.sendMessage(Data.PREFIX + ChatColor.RED + "ACHTUNG! PvP ist in dieser Welt Aktiviert");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

        // Create ProtectionManager for a 100-block radius around the Nether spawn
        new ProtectionManager(plugin, netherSpawn, 100);

        return true;
    }
}
