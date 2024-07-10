package de.flunar.citybuildsystem.managers;

import de.flunar.citybuildsystem.CitybuildSystem;
import de.flunar.citybuildsystem.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.stream.Collectors;

public class ProtectionManager implements Listener {

    private final CitybuildSystem plugin;
    private final Location protectedLocation;
    private final int radius;

    public ProtectionManager(CitybuildSystem plugin, Location protectedLocation, int radius) {
        this.plugin = plugin;
        this.protectedLocation = protectedLocation;
        this.radius = radius;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isInProtectedArea(event.getBlock().getLocation()) && !event.getPlayer().hasPermission("flunar.admin")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Data.FARMWELT_BUILD_MESSAGE);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isInProtectedArea(event.getBlock().getLocation()) && !event.getPlayer().hasPermission("flunar.admin")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Data.FARMWELT_BUILD_MESSAGE);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.hasBlock() && isInProtectedArea(event.getClickedBlock().getLocation()) && !event.getPlayer().hasPermission("flunar.admin")) {
            if (event.getClickedBlock().getType() != Material.AIR) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Data.FARMWELT_BUILD_MESSAGE);
            }
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (isInProtectedArea(event.getBlock().getLocation()) && event.getEntityType() != EntityType.PLAYER) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        List<Block> blocksToRemove = event.blockList().stream()
                .filter(block -> isInProtectedArea(block.getLocation()))
                .collect(Collectors.toList());

        blocksToRemove.forEach(block -> event.blockList().remove(block));
    }

    private boolean isInProtectedArea(Location location) {
        if (location.getWorld() != null && location.getWorld().equals(protectedLocation.getWorld())) {
            double distance = location.distance(protectedLocation);
            return distance <= radius;
        }
        return false;
    }
}
