package de.flunar.citybuildsystem.commands;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class TPSMonitorCommand implements CommandExecutor {

    private Set<Player> monitoringPlayers = new HashSet<>();
    private CitybuildSystem plugin;

    public TPSMonitorCommand(CitybuildSystem plugin) {
        this.plugin = plugin;
        startTPSUpdater();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Data.PREFIX + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("flunar.admin")) {
            player.sendMessage(Data.NO_PERMS);
            return true;
        }

        if (monitoringPlayers.contains(player)) {
            monitoringPlayers.remove(player);
            player.sendMessage(Data.PREFIX + ChatColor.RED + "TPS Monitor Deaktiviert.");
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.0f);
        } else {
            monitoringPlayers.add(player);
            player.sendMessage(Data.PREFIX + ChatColor.GREEN + "TPS Monitor Aktuviert.");
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
        }

        return true;
    }

    private void startTPSUpdater() {
        new BukkitRunnable() {
            @Override
            public void run() {
                double tps = Bukkit.getTPS()[0];  // Get the TPS value
                String tpsMessage = ChatColor.GREEN + "Server TPS: " + ChatColor.YELLOW + String.format("%.2f", tps);

                for (Player player : monitoringPlayers) {
                    player.sendActionBar(tpsMessage);
                }
            }
        }.runTaskTimer(plugin, 0, 5); // Run every second (20 ticks)
    }
}