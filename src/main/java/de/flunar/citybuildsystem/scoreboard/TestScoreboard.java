package de.flunar.citybuildsystem.scoreboard;

import de.flunar.citybuildsystem.CitybuildSystem;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TestScoreboard extends ScoreboardBuilder {

    private int socialId;

    LuckPerms luckPerms;

    User user = luckPerms.getUserManager().getUser(player.getUniqueId());

    public TestScoreboard(Player player) {
        super(player, ChatColor.WHITE.toString() + ChatColor.BOLD + "  Flunar.de  ");
        socialId = 0;

        run();
    }

    @Override
    public void createScoreboard() {
        setScore(" ", 12);
        setScore(ChatColor.DARK_GRAY + "§l• " + ChatColor.GRAY + "Profil:", 11);
        setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.GREEN + player.getName(), 10);
        setScore("  ", 9);
        setScore(ChatColor.DARK_GRAY + "§l• " + ChatColor.GRAY + "Rang:", 8);
        luckPerms = LuckPermsProvider.get();

        if (user != null) {
            // Rufe die aktuelle Ranggruppe des Spielers ab
            String groupDisplayName = user.getPrimaryGroup();

            // Überprüfe, ob groupDisplayName nicht leer ist
            if (!groupDisplayName.isEmpty()) {
                // Extrahiere den ersten Buchstaben und mache ihn groß
                char firstChar = Character.toUpperCase(groupDisplayName.charAt(0));

                // Kombiniere den ersten Buchstaben mit dem Rest des Gruppennamens
                String formattedGroupName = firstChar + groupDisplayName.substring(1);

                // Füge den formatierten Gruppennamen dem Scoreboard hinzu
                setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.YELLOW + formattedGroupName, 7);
            }
        }

        setScore("   ", 6);
        setScore(ChatColor.DARK_GRAY + "§l• " + ChatColor.GRAY + "Geld:", 5);
        setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.RED + "Soon", 4);
        setScore("    ", 3);
        setScore(ChatColor.DARK_GRAY + "§l• " + ChatColor.GRAY + "Discord:", 2);
        setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.AQUA + "dc.flunar.de", 1);
        setScore("      ", 0);
    }

    @Override
    public void update() {

    }

    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {

                switch (socialId) {
                    case 0:
                        if (user != null) {
                            // Rufe die aktuelle Ranggruppe des Spielers ab
                            String groupDisplayName = user.getPrimaryGroup();

                            // Überprüfe, ob groupDisplayName nicht leer ist
                            if (!groupDisplayName.isEmpty()) {
                                // Extrahiere den ersten Buchstaben und mache ihn groß
                                char firstChar = Character.toUpperCase(groupDisplayName.charAt(0));

                                // Kombiniere den ersten Buchstaben mit dem Rest des Gruppennamens
                                String formattedGroupName = firstChar + groupDisplayName.substring(1);

                                // Füge den formatierten Gruppennamen dem Scoreboard hinzu
                                setScore(ChatColor.DARK_GRAY + "§l➥ " + ChatColor.YELLOW + formattedGroupName, 7);
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

}