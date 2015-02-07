package cz.kricraft.ic2.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import cz.kricraft.ic2.Main;

public class WorldListener implements Listener {

    private final Main plugin;

    public WorldListener(Main plugin) {
        this.plugin = plugin;
    }

    public static final int[] spawnLocation = new int[]{0, 0, 0};
    public static final int portalAllowedRadius = 32;

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        Block block = event.getBlocks().get(0);
        Location loc = block.getLocation();
        if (loc.distance(new Location(loc.getWorld(), spawnLocation[0], spawnLocation[1], spawnLocation[2])) > 32) {
            event.setCancelled(true);
        }
    }
}
