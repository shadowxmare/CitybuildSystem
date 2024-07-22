package de.flunar.citybuildsystem.utils;

import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AutoBroadcast {

    private final JavaPlugin plugin;
    private final List<String> messages;
    private final Random random;

    public AutoBroadcast(JavaPlugin plugin) {
        this.plugin = plugin;
        this.messages = Arrays.asList(
                "Benutze /cbhelp für weitere Hilfe"
        );
        this.random = new Random();
        startBroadcastTask();
    }

    private void startBroadcastTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String message = getRandomMessage();
                Bukkit.broadcastMessage(Data.PREFIX + ChatColor.AQUA + message);
            }
        }.runTaskTimer(plugin, 0L, 6000L); // 6000 ticks = 5 minutes
    }

    private String getRandomMessage() {
        int index = random.nextInt(messages.size());
        return messages.get(index);
    }
}
