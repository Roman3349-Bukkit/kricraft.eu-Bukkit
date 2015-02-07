package cz.kricraft.ic2;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import cz.kricraft.ic2.listeners.InteractListener;
import cz.kricraft.ic2.listeners.InventoryListener;
import cz.kricraft.ic2.listeners.WorldListener;
import cz.kricraft.ic2.managers.LWCManager;
import cz.kricraft.ic2.managers.WGManager;

public class Main extends JavaPlugin {

    public static final String prefix = "[CraftujIC2]";
    public DataStorage storage;
    public LWCManager lwcManager;
    public WGManager wgManager;

    @Override
    public void onEnable() {
        info("Nacitam");
        storage = new DataStorage();
        storage.loadConfig();
        boolean success = loadPlugins();
        if (!success) {
            return;
        }
        registerListeners();
        redirectCommands();
        PluginDescriptionFile desc = getDescription();
        info("CraftujIC2 v" + desc.getVersion() + " by " + desc.getAuthors()+ " nacten!");
    }

    @Override
    public void onDisable() {
        info("Vypinam");
        PluginDescriptionFile desc = getDescription();
        info("CraftujIC2 v" + desc.getVersion() + " by " + desc.getAuthors()+ " vypnut!");
    }

    public boolean loadPlugins() {
        try {
            LWC lwc = loadLWC();
            lwcManager = new LWCManager(lwc);
            WorldGuardPlugin wg = loadWorldGuard();
            wgManager = new WGManager(wg);
        } catch (Exception e) {
            warn("Error pri nacitani pluginu.");
            return false;
        }
        return true;
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new InteractListener(this), this);
        pm.registerEvents(new InventoryListener(this), this);
        pm.registerEvents(new WorldListener(this), this);
    }

    public void redirectCommands() {
        getServer().getPluginCommand("cic2").setExecutor(new CIC2Executor(this));
    }

    public static LWC loadLWC() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("LWC");
        if (plugin != null) {
            if (plugin instanceof LWCPlugin) {
                LWCPlugin lwcPlugin = (LWCPlugin) plugin;
                return lwcPlugin.getLWC();
            }
        }
        warn("Nelze nacist LWC.");
        return null;
    }

    public static WorldGuardPlugin loadWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (plugin != null) {
            if (plugin instanceof WorldGuardPlugin) {
                WorldGuardPlugin wg = (WorldGuardPlugin) plugin;
                return wg;
            }
        }
        warn("Nelze nacist WorldGuard.");
        return null;
    }

    public static void info(String string) {
        System.out.println(prefix + " " + string);
    }

    public static void warn(String string) {
        System.out.println(prefix + " [WARNING] " + string);
        for (OfflinePlayer op : Bukkit.getOperators()) {
            if (!op.isOnline()) {
                continue;
            }
            Player oop = (Player) op;
            oop.sendMessage(ChatColor.GRAY + prefix + " [WARNING]: " + ChatColor.RED + string);
        }

    }

}
