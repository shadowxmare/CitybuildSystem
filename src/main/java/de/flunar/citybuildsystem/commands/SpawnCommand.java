package de.flunar.citybuildsystem.commands;


import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnCommand implements CommandExecutor {

    private JavaPlugin plugin;
    private FileConfiguration config;

    public SpawnCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                String worldName = config.getString("spawn.world");
                if (worldName == null) {
                    player.sendMessage(Data.PREFIX + ChatColor.RED + "Der Spawn-Punkt wurde noch nicht gesetzt.");
                    return true;
                }

                double x = config.getDouble("spawn.x");
                double y = config.getDouble("spawn.y");
                double z = config.getDouble("spawn.z");
                float yaw = (float) config.getDouble("spawn.yaw");
                float pitch = (float) config.getDouble("spawn.pitch");

                Location spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
                player.teleport(spawnLocation);
                player.sendMessage(Data.PREFIX + ChatColor.GREEN + "Du wurdest zum Spawn teleportiert.");
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            } else {
                sender.sendMessage(Data.PREFIX + ChatColor.RED + "Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            }
            return true;
        }
        return false;
    }
}
