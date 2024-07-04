package de.flunar.citybuildsystem.commands;

import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CBHelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("cbhelp")) {
            sender.sendMessage(Data.HELP_COMMAND_PREFIX);
            sender.sendMessage(ChatColor.YELLOW + "/farmwelt " + ChatColor.GRAY + "- Bringt dich Zur Farmwelt");
            sender.sendMessage(ChatColor.YELLOW + "/spawn " + ChatColor.GRAY + "- Bringt dich Zum Spawn");
            sender.sendMessage(ChatColor.YELLOW + "/p home " + ChatColor.GRAY + "- Bring dich zu deinem Plot");
            sender.sendMessage(Data.HELP_COMMAND_PREFIX);

            // Überprüfen, ob der Absender ein Spieler ist und den Sound abspielen
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }

            return true;
        }
        return false;
    }
}
