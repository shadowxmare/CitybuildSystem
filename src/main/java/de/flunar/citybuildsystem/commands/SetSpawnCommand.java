package de.flunar.citybuildsystem.commands;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.managers.MySQLManager;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            sender.sendMessage(Data.PREFIX + ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("flunar.admin")) {
            player.sendMessage(Data.NO_PERMS);
            return true;
        }

        // Check if the command is to delete spawns
        if (args.length >= 1 && args[0].equalsIgnoreCase("delete")) {
            if (args.length != 2 || !args[1].matches("\\d+")) {
                player.sendMessage(Data.PREFIX + ChatColor.RED + "Usage: /setspawn delete <number>");
                return true;
            }

            int spawnsToDelete = Integer.parseInt(args[1]);
            deleteSpawns(player, spawnsToDelete);
            return true;
        }

        // Get current player location
        String world = player.getWorld().getName();
        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();

        // Use MySQLManager to get connection
        MySQLManager mySQLManager = plugin.getMySQLManager();
        if (mySQLManager == null) {
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Failed to connect to database.");
            return true;
        }

        try (Connection connection = mySQLManager.getConnection()) {
            if (connection == null || connection.isClosed()) {
                player.sendMessage(Data.PREFIX + ChatColor.RED + "Failed to connect to database.");
                return true;
            }

            // Get current count of spawns
            int currentSpawns = getCurrentSpawnCount(connection);

            // Insert player location into spawns_data table
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO spawns_data (spawns, world, x, y, z, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?);"
            );
            statement.setInt(1, currentSpawns + 1); // Increment spawns by 1
            statement.setString(2, world);
            statement.setDouble(3, x);
            statement.setDouble(4, y);
            statement.setDouble(5, z);
            statement.setFloat(6, yaw);
            statement.setFloat(7, pitch);
            statement.executeUpdate();

            player.sendMessage(Data.PREFIX + ChatColor.GREEN + "Spawn location Gesetzt.");

        } catch (SQLException e) {
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Error Beim setzten einer Spawn location");
            e.printStackTrace();
        }

        return true;
    }

    private int getCurrentSpawnCount(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) AS total_spawns FROM spawns_data;"
        )) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("total_spawns");
            } else {
                return 0;
            }
        }
    }

    private void deleteSpawns(Player player, int spawnsToDelete) {
        MySQLManager mySQLManager = plugin.getMySQLManager();
        if (mySQLManager == null) {
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Failed to connect to database.");
            return;
        }

        try (Connection connection = mySQLManager.getConnection()) {
            if (connection == null || connection.isClosed()) {
                player.sendMessage(Data.PREFIX + ChatColor.RED + "Failed to connect to database.");
                return;
            }

            // Execute SQL to delete specified number of spawns
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM spawns_data WHERE spawns = ? LIMIT ?;"
            );
            statement.setInt(1, spawnsToDelete);
            statement.setInt(2, spawnsToDelete);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                player.sendMessage(Data.PREFIX + ChatColor.GREEN + "Gelöscht " + rowsAffected + " spawn location mit ID " + spawnsToDelete + ".");
            } else {
                player.sendMessage(Data.PREFIX + ChatColor.RED + "Keine Location gefunden mit der ID " + spawnsToDelete + ".");
            }

        } catch (SQLException e) {
            player.sendMessage(Data.PREFIX + ChatColor.RED + "Error Code: deleteSpawns");
            e.printStackTrace();
        }
    }
}
