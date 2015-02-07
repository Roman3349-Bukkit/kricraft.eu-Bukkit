package cz.kricraft.ic2.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import cz.kricraft.ic2.Main;
import cz.kricraft.ic2.utils.MaterialData;
import cz.kricraft.ic2.utils.ModUtils;

public class InteractListener implements Listener {

    private final Main plugin;

    public InteractListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityExplode(EntityExplodeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() instanceof TNTPrimed) {
            e.setCancelled(true);
            Main.info("Vybuch byl zrusen na " + e.getLocation().toString());
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        Player player = e.getPlayer();
        Block block = e.getBlock();
        int id = block.getTypeId();
        if (id == 762) {
            Location loc = block.getLocation();
            e.setCancelled(true);
            block.setTypeId(0);
            block.getWorld().dropItemNaturally(loc, new ItemStack(id));
        } else if (block.getType() == Material.MOB_SPAWNER) {
            if (!player.isOp()) {
                e.setCancelled(true);
                player.kickPlayer("Nemuzes nicit mobspawnery!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack inHand = player.getItemInHand();
            int id = inHand.getTypeId();
            MaterialData md = new MaterialData(inHand);
            if (id == 30208) {
                Location loc = player.getLocation();
                if (plugin.wgManager.isLaserProtected(loc)) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "Zde nemuzes pouzivat laser!");
                } else {
                    double y = loc.getY();
                    if (y > 30) {
                        String playerName = player.getName();
                        if (!player.isOp()) {
                            player.sendMessage(ChatColor.RED + "Nelze pouzit Mining Laser nad y: 30.");
                            Main.info(playerName + " se pokusil pouzit Mining Laser na povrchu.");
                            event.setCancelled(true);
                        } else {
                            Main.info("OP " + playerName + " pouzil Mining Laser na povrchu.");
                        }
                    }
                }
            } else if (inHand.getType() == Material.EGG) {
                Location eyeLoc = player.getEyeLocation();
                if (!plugin.wgManager.canBuild(player, eyeLoc)) {
                    player.sendMessage(ChatColor.RED + "Nemuzes hazet vejce na cizim ostrovu!");
                    event.setCancelled(true);
                }
            } else if (id == 30237) {
                Location loc = action == Action.RIGHT_CLICK_BLOCK ? event
                        .getClickedBlock().getLocation() : player.getLocation();
                if (!plugin.wgManager.canBuild(player, loc)) {
                    player.sendMessage(ChatColor.RED + "Tento item nemuzes pouzivat na cizim pozemku!");
                    event.setCancelled(true);
                }
            } else if (ModUtils.isItemBanned(plugin.storage, md)) {
                String loc = player.getLocation().getBlock().getLocation().toString();
                String playerName = player.getName();
                short dur = inHand.getDurability();
                if (!player.isOp()) {
                    player.sendMessage(ChatColor.RED + "Omlouvame se, ale tento item je zakazany.");
                    Main.info(playerName + " se pokusil pouzit zakazany item (" + id + ":" + dur + ") na " + loc);
                    event.setCancelled(true);
                } else {
                    Main.info("OP " + playerName + " pouzil dynamit na " + loc);
                }
            } else if (action == Action.RIGHT_CLICK_BLOCK) {
                Block block = event.getClickedBlock();
                int bId = block.getTypeId();
                byte bData = block.getData();
                if (bId == 2091 && bData == 12) { // Popelnice
                    event.setCancelled(true);
                    player.closeInventory();
                    player.kickPlayer("Popelnice se nesmeji pouzivat!");
                } else if (block.getType() == Material.OBSIDIAN) {
                    if (ModUtils.canLightPortals(id)) {
                        String loc = block.getLocation().toString();
                        String playerName = player.getName();
                        if (!player.isOp()) {
                            player.sendMessage(ChatColor.RED + "ZAKAZNO! Pouzij nether portal na spawnu!");
                            Main.info(playerName + " se pokusil zapalit nether portal na " + loc);
                            event.setCancelled(true);
                        } else {
                            Main.info("OP " + playerName + " zapalil nether portal na " + loc);
                        }
                    }

                } else if (id == 2092) { // Bath? TODO Presunout do BlockPlaceEvent
                    Location clickedLoc = block.getRelative(event.getBlockFace()).getLocation();
                    if (!plugin.wgManager.canBuild(player, clickedLoc)) {
                        String playerName = player.getName();
                        player.sendMessage(ChatColor.RED + "Na toto nemas opravneni!");
                        Main.info(playerName + " se pokusil polozit bath v protectu na " + clickedLoc.toString());
                        event.setCancelled(true);
                    }
                } else if (bId == 5272 || bId == 5271) {
                    Location clickedLoc = block.getRelative(
                            event.getBlockFace()).getLocation();
                    if (!plugin.wgManager.canBuild(player, clickedLoc)) {
                        String playerName = player.getName();
                        player.sendMessage(ChatColor.RED + "Na toto nemas opravneni!");
                        Main.info(playerName + " se pokusil pouzit bucket v protectu na " + clickedLoc.toString());
                        event.setCancelled(true);
                    }
                } else if (bId == 975 && bData == 5) {
                    String loc = block.getLocation().toString();
                    String playerName = player.getName();
                    block.breakNaturally();
                    player.sendMessage(ChatColor.RED + "Crystal chest byla na serveru zakazana, behem tydne");
                    player.sendMessage(ChatColor.RED + "je smazeme kompletne ze serveru. dekujeme za pochopeni.");
                    Main.info("Hrac " + playerName + " zkusil otevrit crystal chest na " + loc);

                } else if (ModUtils.isBlockLockable(plugin.storage,
                        new MaterialData(bId, bData))) {
                    if (!player.isOp()) {
                        if (!plugin.wgManager.hasContainerAccess(player, block)) {
                            player.sendMessage(ChatColor.RED + "Na toto nemas v tomto regionu opravneni!");
                            Location l = block.getLocation();
                            Main.info("Hrac " + player.getName() + " se pokusil otevrit zamceny item regionem ("
                                    + bId + ":" + bData + ") na (" + l.getWorld().getName() + "; " + l.getX() + "; " + l.getY()
                                    + "; " + l.getZ() + ")");
                            event.setCancelled(true);
                            player.damage(1);
                            player.closeInventory();

                        } else if (!plugin.lwcManager.hasAccess(player, block)) {

                            player.sendMessage(ChatColor.RED + "Na toto nemas opravneni!");
                            Location l = block.getLocation();
                            Main.info("Hrac " + player.getName() + " se pokusil otevrit zamceny item ("
                                    + bId + ":" + bData + ") na (" + l.getWorld().getName() + "; " + l.getX()
                                    + "; " + l.getY() + "; " + l.getZ() + ")");
                            event.setCancelled(true);
                            player.damage(1);
                            player.closeInventory();

                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        Block block = event.getBlock();
        int id = block.getTypeId();
        byte data = block.getData();
        MaterialData md = new MaterialData(id, data);
        if (ModUtils.isBlockBanned(plugin.storage, md)) {
            String loc = block.getLocation().toString();
            String playerName = player.getName();
            if (player.isOp()) {
                Main.info("OP " + playerName + " polozil zakazanou kostku (" + id + ":" + data + ") na " + loc);
                return;
            }
            event.setCancelled(true);
            if (ModUtils.isReactor(md)) {
                player.sendMessage(ChatColor.RED + "Reaktory jsou zakazany. Pro vice informaci navstivte web.");
                Main.info("Hrac " + playerName + " se pokusil polozit reaktor na " + loc);
            } else {
                player.sendMessage(ChatColor.RED + "Tato vec je zakazana.");
                Main.info("Hrac " + playerName + " se pokusil polozit zakazanou kostku (" + id + ":" + data + ") na " + loc);
            }
        } else if (!ModUtils.canBePlacedNearLock(md)) {
            if (plugin.lwcManager.isLockedNextTo(block, player)) {
                String loc = block.getLocation().toString();
                String playerName = player.getName();
                if (player.isOp()) {
                    Main.info("OP " + playerName + " polozil kostku vedle nepristupneho bloku (" + id + ":"
                            + data + ") na " + loc);
                    return;
                }
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "Tuto kostku nelze polozit vedle nepristupneho bloku. (LWC)");
                Main.info("Hrac " + playerName + " se pokusil polozit kostku (" + id + ":" + data
                        + ") vedle nepristupneho bloku (LWC) na " + loc);
            }
        }
    }
}
