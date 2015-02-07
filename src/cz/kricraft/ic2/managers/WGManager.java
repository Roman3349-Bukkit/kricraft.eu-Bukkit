package cz.kricraft.ic2.managers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import cz.kricraft.ic2.utils.ModUtils;

public class WGManager {

    public WorldGuardPlugin wg;

    public WGManager(WorldGuardPlugin wg) throws NullPointerException {
        if (wg == null) {
            throw new NullPointerException("WorldGuard nenalezen!");
        }
        this.wg = wg;
    }

    public boolean canBuild(Player player, Location loc) {
        return wg.canBuild(player, loc);
    }

    public boolean hasContainerAccess(Player player, Block block) {
        Location loc = block.getLocation();
        return hasContainerAccess(player, loc);
    }

    public boolean hasContainerAccess(Player player, Location loc) {
        String name = player.getName();
        ApplicableRegionSet regions = getRegions(loc);
        boolean isMember = true;
        for (ProtectedRegion region : regions) {
            if (!(region.isMember(name) || region.isOwner(name))) {
                isMember = false;
                break;
            }
        }
        return isMember || regions.allows(DefaultFlag.CHEST_ACCESS);
    }

    public boolean isLaserProtected(Location loc) {
        ApplicableRegionSet regions = getRegions(loc);
        for (ProtectedRegion region : regions) {
            if (ModUtils.isRegionLaserProtected(region.getId())) {
                return true;
            }
        }
        return false;
    }

    public ApplicableRegionSet getRegions(Location loc) {
        World world = loc.getWorld();
        RegionManager r = wg.getRegionManager(world);
        return r.getApplicableRegions(loc);
    }
}
