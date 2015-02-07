package cz.kricraft.ic2.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import cz.kricraft.ic2.Main;
import cz.kricraft.ic2.utils.MaterialData;
import cz.kricraft.ic2.utils.ModUtils;

public class InventoryListener implements Listener {

    private final Main plugin;

    public InventoryListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack inHand = player.getItemInHand();
        MaterialData md = new MaterialData(inHand);
        boolean blocked = ModUtils.cropAnalyzer.compare(md);
        if (blocked) {
            player.sendMessage(ChatColor.RED + "Nemuzes vyhazovat itemy, pokud mas v ruce cropanalyzer!");
            Main.info("Hrac " + player.getName() + " se pokusil vyhodit item z cropanalyzeru!");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onCraftItem(CraftItemEvent event) {
        ItemStack item = event.getCurrentItem();
        MaterialData md = new MaterialData(item);
        boolean blocked = ModUtils.isBlockedInCrafting(md);
        if (blocked) {
            Player player = (Player) event.getWhoClicked();
            player.sendMessage(ChatColor.RED + "Tuto vec nelze vycraftit.");
            Main.info(player.getName() + " se pokusil vycraftit " + md.getId() + ":" + md.getDurability()
                    + " (" + item.getType().name() + ")");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.isShiftClick()) {
            Player player = (Player) event.getWhoClicked();
            ItemStack inHand = player.getItemInHand();
            MaterialData md = new MaterialData(inHand);
            boolean blocked = ModUtils.cropAnalyzer.compare(md);
            if (blocked) {
                player.sendMessage(ChatColor.RED + "Nemuzes pouzivat shift v cropanalyzeru!");
                Main.info("Hrac " + player.getName() + " se pokusil pouzit shift v croanalyzeru!");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerPickupItem(PlayerPickupItemEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getItem().getItemStack().getTypeId() == 2092) {
            e.getItem().remove();
            e.setCancelled(true);
        }
    }
}
