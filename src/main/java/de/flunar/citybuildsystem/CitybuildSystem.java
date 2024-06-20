package de.flunar.citybuildsystem;

import de.flunar.citybuildsystem.listeners.PlayerJoinListener;
import de.flunar.citybuildsystem.listeners.PlayerQuitListener;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CitybuildSystem extends JavaPlugin {

    private static int taskId;

    private static CitybuildSystem instance;

    @Override
    public void onEnable() {

        instance = this;

        Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.GREEN + "Das Citybuild System wurde gestartet!");
        Bukkit.getConsoleSender().sendMessage(Data.PREFIX + ChatColor.RED + "Code By Iownme_ / Shadowxmare");



        new PlayerJoinListener(this);
        new PlayerQuitListener(this);




    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CitybuildSystem getInstance() {
        return instance;
    }

    public void startEventScheduler() {
        // TaskId wird zurückgegeben, um den Task später zu stoppen, falls erforderlich
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    // Aktualisiere das Scoreboard für jeden Online-Spieler
                }
            }
        }, 0L, 100L); // 100L = 5 Sekunden (20 Ticks pro Sekunde)
    }
}
