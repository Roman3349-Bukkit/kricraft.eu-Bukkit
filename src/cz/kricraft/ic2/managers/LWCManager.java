package cz.kricraft.ic2.managers;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.griefcraft.lwc.LWC;

import cz.kricraft.ic2.utils.ModUtils;

public class LWCManager {

    public LWC lwc;

    public LWCManager(LWC lwc) throws NullPointerException {
        if (lwc == null) {
            throw new NullPointerException("LWC nenalezeno!");
        }
        this.lwc = lwc;
    }

    public Boolean hasAccess(Player player, Block block) {
        if (lwc.findProtection(block) != null) {
            return lwc.canAccessProtection(player, block);
        }
        return true;
    }

    public Boolean isLockedNextTo(Block block, Player player) {
        boolean blocked = false;
        Block[] blocksNextTo = ModUtils.getBlocksNextTo(block);
        for (Block nextTo : blocksNextTo) {
            if (!hasAccess(player, nextTo)) {
                blocked = true;
                break;
            }
        }
        return blocked;
    }
}
