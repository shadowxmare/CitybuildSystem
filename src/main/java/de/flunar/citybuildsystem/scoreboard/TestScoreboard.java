package de.flunar.citybuildsystem.scoreboard;

import com.earth2me.essentials.Essentials;
import de.flunar.citybuildsystem.CitybuildSystem;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;

public class TestScoreboard extends ScoreboardBuilder {

    private int socialId;
    private LuckPerms luckPerms;
    private Essentials essentials;
    private User user;

    public TestScoreboard(Player player) {
        super(player, ChatColor.WHITE.toString() + ChatColor.BOLD + "  Flunar.de  ");
        this.socialId = 0;
        this.luckPerms = LuckPermsProvider.get();
        this.essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        this.user = luckPerms.getUserManager().getUser(player.getUniqueId());

        createScoreboard();
        run();
    }

    @Override
    public void createScoreboard() {
        setScore(" ", 15);
        setScore(ChatColor.DARK_GRAY + "§l• " + ChatColor.GRAY + "Profil:", 14);
        setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.GREEN + player.getName(), 13);
        setScore("  ", 12);
        setScore(ChatColor.DARK_GRAY + "§l• " + ChatColor.GRAY + "Rang:", 11);

        if (user != null) {
            String groupDisplayName = user.getPrimaryGroup();
            if (!groupDisplayName.isEmpty()) {
                char firstChar = Character.toUpperCase(groupDisplayName.charAt(0));
                String formattedGroupName = firstChar + groupDisplayName.substring(1);
                setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.YELLOW + formattedGroupName, 10);
            }
        }

        setScore("   ", 9);
        setScore(ChatColor.DARK_GRAY + "§l• " + ChatColor.GRAY + "Geld:", 8);
        setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.RED + getFormattedBalance(), 7);
        setScore("       ", 6);
        setScore(ChatColor.DARK_GRAY + "§l• " + ChatColor.GRAY + "CB-Spieler:", 5);
        setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.GOLD + getOnlinePlayerCount() + ChatColor.GRAY + "/" + ChatColor.GOLD+ getMaxPlayers(), 4);
        setScore("     ", 3);
        setScore(ChatColor.DARK_GRAY + "§l• " + ChatColor.GRAY + "Discord:", 2);
        setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.AQUA + "dc.flunar.de", 1);
        setScore("      ", 0);
    }

    @Override
    public void update() {
        setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.RED + getFormattedBalance(), 7);
        setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.GOLD + getOnlinePlayerCount() + ChatColor.GRAY + "/" + ChatColor.GOLD + getMaxPlayers(), 4);
    }

    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                update();

                switch (socialId) {
                    case 0:
                        if (user != null) {
                            String groupDisplayName = user.getPrimaryGroup();
                            if (!groupDisplayName.isEmpty()) {
                                char firstChar = Character.toUpperCase(groupDisplayName.charAt(0));
                                String formattedGroupName = firstChar + groupDisplayName.substring(1);
                                setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.YELLOW + formattedGroupName, 10);
                            }
                        }
                        break;
                    case 1:
                        setScore(ChatColor.DARK_GRAY + "§l• " + ChatColor.GRAY + "Twitter:", 2);
                        setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.AQUA + "@Flunar_de", 1);
                        break;
                    case 2:
                        break;
                    case 3:
                        setScore(ChatColor.DARK_GRAY + "§l• " + ChatColor.GRAY + "Discord:", 2);
                        setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.BLUE + "dc.flunar.de", 1);
                        break;
                }

                socialId++;
                if (socialId >= 5) {
                    socialId = 0;
                }
            }
        }.runTaskTimer(CitybuildSystem.getInstance(), 0, 90);
    }

    private String getFormattedBalance() {
        if (essentials != null) {
            BigDecimal balance = essentials.getUser(player).getMoney();
            return "$" + balance.toString();
        }
        return "Error";
    }

    private String getOnlinePlayerCount() {
        int onlinePlayers = Bukkit.getOnlinePlayers().size();
        return String.valueOf(onlinePlayers);
    }

    private String getMaxPlayers() {
        return String.valueOf(Bukkit.getMaxPlayers());
    }
}
