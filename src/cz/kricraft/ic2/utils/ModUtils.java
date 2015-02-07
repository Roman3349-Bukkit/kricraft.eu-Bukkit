package cz.kricraft.ic2.utils;

import org.bukkit.block.Block;

import cz.kricraft.ic2.DataStorage;

public class ModUtils {

    public static final String[] laserProtectedRegions = new String[]{"vipzona-eri", "dul", "spawn"};

    public static final MaterialData cropAnalyzer = new MaterialData(30122);

    @Deprecated
    public static final MaterialData[] blockedCrafting = new MaterialData[]{
        new MaterialData(237), new MaterialData(19762), new MaterialData(30214), new MaterialData(30215),
        new MaterialData(250, 15), new MaterialData(754, 4), new MaterialData(1808, 2), new MaterialData(1808, 3),
        new MaterialData(1808, 4), new MaterialData(1808, 6), new MaterialData(1808, 7), new MaterialData(1808, 8),
        new MaterialData(975, 5), new MaterialData(9268), new MaterialData(19762)};

    public static final MaterialData[] lockableBlocks = new MaterialData[]{
        new MaterialData(2094), new MaterialData(2091), new MaterialData(2090, 4), new MaterialData(2095, 1),
        new MaterialData(2089), new MaterialData(1808, 1), new MaterialData(4062), new MaterialData(3099),
        new MaterialData(975), new MaterialData(4059), new MaterialData(138), new MaterialData(192)};

    public static final MaterialData[] cantBePlacedNearLockedBlocks = new MaterialData[]{
        new MaterialData(150, 3), new MaterialData(150, 2), new MaterialData(150, 5), new MaterialData(763, 1)};

    public static final int[] ableToLightPortals = new int[]{59, 51, 385};

    public static final int[][] nextTo = new int[][]{new int[]{1, 0, 0}, new int[]{-1, 0, 0}, new int[]{0, 1, 0},
    new int[]{0, -1, 0}, new int[]{0, 0, 1}, new int[]{0, 0, -1},};

    public static boolean isRegionLaserProtected(String regionName) {
        for (String s : laserProtectedRegions) {
            if (s.equals(regionName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlockedInCrafting(MaterialData md) {
        for (MaterialData cur : blockedCrafting) {
            if (cur.compare(md)) {
                return true;
            }
        }
        return false;
    }

    public static boolean canLightPortals(int id) {
        for (int i : ableToLightPortals) {
            if (i == id) {
                return true;
            }
        }
        return false;

    }

    public static boolean isBlockLockable(DataStorage storage, MaterialData md) {
        for (MaterialData cur : storage.lockableBlocks) {
            if (cur.compare(md)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBlockBanned(DataStorage storage, MaterialData md) {
        for (MaterialData cur : storage.bannedBlocks) {
            if (cur.compare(md)) {
                return true;
            }
        }
        return false;

    }

    public static boolean isItemBanned(DataStorage storage, MaterialData md) {
        for (MaterialData cur : storage.bannedItems) {
            if (cur.compare(md)) {
                return true;
            }
        }
        return false;

    }

    public static Block[] getBlocksNextTo(Block block) {
        Block[] result = new Block[nextTo.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = block.getRelative(nextTo[i][0], nextTo[i][1], nextTo[i][2]);
        }
        return result;
    }

    public static boolean isReactor(MaterialData md) {
        return md.compare(new MaterialData(246, 5)) || md.compare(new MaterialData(233, 0));
    }

    public static boolean canBePlacedNearLock(MaterialData md) {
        for (MaterialData cur : cantBePlacedNearLockedBlocks) {
            if (cur.compare(md)) {
                return false;
            }
        }
        return true;
    }
}
