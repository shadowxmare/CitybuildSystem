package de.flunar.citybuildsystem.commands;

import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetSpawnCommand implements CommandExecutor {

    private String permission = "flunar.admin";

    private JavaPlugin plugin;
    private FileConfiguration config;

    public SetSpawnCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("setspawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission(permission)) {
                    player.sendMessage(Data.NO_PERMS);
                    return true;
                }

                String worldName = player.getWorld().getName();
                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();
                float yaw = player.getLocation().getYaw();
                float pitch = player.getLocation().getPitch();

                config.set("spawn.world", worldName);
                config.set("spawn.x", x);
                config.set("spawn.y", y);
                config.set("spawn.z", z);
                config.set("spawn.yaw", yaw);
                config.set("spawn.pitch", pitch);

                plugin.saveConfig(); // Save the config without handling IOException

                player.sendMessage(Data.PREFIX + ChatColor.GREEN + "Spawn-Punkt wurde gesetzt.");
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
            } else {
                sender.sendMessage(Data.PREFIX + ChatColor.RED + "Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            }
            return true;
        }
        return false;
    }
}