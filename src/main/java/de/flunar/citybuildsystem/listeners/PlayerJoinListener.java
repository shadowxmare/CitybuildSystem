package de.flunar.citybuildsystem.listeners;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.scoreboard.TestScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final CitybuildSystem plugin;

    public PlayerJoinListener(CitybuildSystem plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String playerName = event.getPlayer().getName();


        event.setJoinMessage(null);
        event.setJoinMessage(ChatColor.GRAY + playerName + ChatColor.GRAY + ": " + ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "]");
        new TestScoreboard(player);
    }
}
