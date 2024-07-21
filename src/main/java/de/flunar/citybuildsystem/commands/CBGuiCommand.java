package de.flunar.citybuildsystem.commands;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CBGuiCommand implements CommandExecutor, Listener {

    private final CitybuildSystem plugin;

    public CBGuiCommand(CitybuildSystem plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Data.PREFIX + ChatColor.RED + "Nur Spieler können diesen Befehl verwenden.");
            return true;
        }

        Player player = (Player) sender;
        Inventory gui = Bukkit.createInventory(null, 45, Data.CBGUI_COMMAND_GUI);

        // Füge die spezifischen Items hinzu
        gui.setItem(10, createGuiItem(Material.GRASS_BLOCK, ChatColor.GREEN + "§lFarmwelt"));
        gui.setItem(13, createGuiItem(Material.NETHERRACK, ChatColor.RED + "§lNether"));
        gui.setItem(16, createGuiItem(Material.END_STONE, ChatColor.YELLOW + "§lEnd (soon)"));
        gui.setItem(31, createGuiItem(Material.SPAWNER, ChatColor.LIGHT_PURPLE + "§lRewards"));
        gui.setItem(29, createGuiItem(Material.BEACON, ChatColor.AQUA + "§lBooster"));

        // Fülle die restlichen Slots mit blauem Glas
        ItemStack fillerItem = createGuiItem(Material.BLUE_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, fillerItem);
            }
        }

        player.openInventory(gui);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        return true;
    }

    private ItemStack createGuiItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(Data.CBGUI_COMMAND_GUI)) {
            event.setCancelled(true); // Verhindert das Bewegen von Items

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() == Material.SPAWNER) {
                player.closeInventory();
                player.performCommand("rewards");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

            }

            if (clickedItem != null && clickedItem.getType() == Material.GRASS_BLOCK) {
                player.closeInventory();
                player.performCommand("farmwelt");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }

            if (clickedItem != null && clickedItem.getType() == Material.BEACON) {
                player.closeInventory();
                player.performCommand("booster");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }

            if (clickedItem != null && clickedItem.getType() == Material.NETHERRACK) {
                player.closeInventory();
                player.performCommand("nether");
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }

    }
}
