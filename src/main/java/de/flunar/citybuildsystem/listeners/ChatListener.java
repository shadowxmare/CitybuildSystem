package de.flunar.citybuildsystem.listeners;

import de.flunar.citybuildsystem.utils.Data;
import de.flunar.citybuildsystem.utils.DiscordWebhookSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatListener implements Listener {

    private final DiscordWebhookSender discordWebhookSender;

    public ChatListener(JavaPlugin plugin, DiscordWebhookSender discordWebhookSender) {
        this.discordWebhookSender = discordWebhookSender;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getName();
        String message = event.getMessage();

        // Send message to Discord Webhook
        String discordMessage = "**" + playerName + "**: " + message;
        discordWebhookSender.sendMessage(discordMessage);
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        String playerName = event.getPlayer().getName();
        String command = event.getMessage();

        // Send command to Discord Webhook
        String discordMessage = "**" + playerName + "** executed command: " + command;
        discordWebhookSender.sendMessage(discordMessage);
    }
}
