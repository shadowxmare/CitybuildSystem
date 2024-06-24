package de.flunar.citybuildsystem.commands;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.managers.MySQLManager;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetSpawnCommand implements CommandExecutor {

    private final CitybuildSystem plugin;

    public SetSpawnCommand(CitybuildSystem plugin) {
        this.plugin = plugin;
        plugin.getCommand("setspawn").setExecutor(this);
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

        // Zugriff auf MySQLManager aus CitybuildSystem
        MySQLManager mysqlManager = plugin.getMySQLManager();
        if (mysqlManager == null || !mysqlManager.isConnected()) {
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Failed to connect to MySQL database.");
            return true;
        }

        // Beispiel: SQL-Befehl ausführen
        try {
            double x = player.getLocation().getX();
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ();
            String playerName = player.getName();

            String sql = "INSERT INTO spawns (player_name, x, y, z) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = mysqlManager.getConnection().prepareStatement(sql)) {
                statement.setString(1, playerName);
                statement.setDouble(2, x);
                statement.setDouble(3, y);
                statement.setDouble(4, z);
                statement.executeUpdate();
                player.sendMessage(Data.PREFIX + ChatColor.GREEN + "Spawn successfully set.");
            }
        } catch (SQLException e) {
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Failed to set spawn: " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }
}
