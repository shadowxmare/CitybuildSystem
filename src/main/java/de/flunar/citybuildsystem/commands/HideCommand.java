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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HideCommand implements CommandExecutor {

    private final CitybuildSystem plugin;
    private final Set<UUID> hiddenPlayers = new HashSet<>();

    public HideCommand(CitybuildSystem plugin) {
        this.plugin = plugin;
    }

    public Set<UUID> getHiddenPlayers() {
        return hiddenPlayers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Data.PREFIX + "Dieser Befehl kann nur von einem Spieler verwendet werden.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("flunar.team")) {
            player.sendMessage(Data.NO_PERMS);
            return true;
        }

        if (hiddenPlayers.contains(player.getUniqueId())) {
            // Spieler wieder sichtbar machen
            hiddenPlayers.remove(player.getUniqueId());
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(plugin, player);
            }
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Du bist jetzt wieder sichtbar.");
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.0f);
        } else {
            // Spieler unsichtbar machen
            hiddenPlayers.add(player.getUniqueId());
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.hasPermission("flunar.team")) {
                    onlinePlayer.hidePlayer(plugin, player);
                }
            }
            player.sendMessage(Data.PREFIX + ChatColor.GREEN + "Du bist jetzt unsichtbar.");
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
        }

        return true;
    }
}
