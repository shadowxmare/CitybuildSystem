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

public class GrundstückCommand implements CommandExecutor, Listener {

    private final CitybuildSystem plugin;
    private final String AUTO_PLOT_COMMAND = "plot auto";

    public GrundstückCommand(CitybuildSystem plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Data.PREFIX + ChatColor.RED + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        // Create and open the GUI
        Inventory gui = Bukkit.createInventory(player, 45, Data.GS_COMMAND_GUI);

        gui.setItem(10, createGuiItem(Material.GOLD_BLOCK, ChatColor.GREEN + "§lAuto Plot"));

        // Fill the rest of the slots with blue glass
        ItemStack fillerItem = createGuiItem(Material.BLUE_STAINED_GLASS_PANE, " ");
        for (int i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, fillerItem);
            }
        }
        player.openInventory(gui);
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
        if (event.getView().getTitle().equals(Data.GS_COMMAND_GUI)) {
            event.setCancelled(true); // Prevents item movement

            Player player = (Player) event.getWhoClicked();
            ItemStack clickedItem = event.getCurrentItem();

            if (clickedItem != null && clickedItem.getType() == Material.GOLD_BLOCK) {
                player.closeInventory();
                player.performCommand(AUTO_PLOT_COMMAND);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            }
        }
    }
}
