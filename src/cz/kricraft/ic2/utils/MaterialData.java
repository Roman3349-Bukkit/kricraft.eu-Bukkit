package cz.kricraft.ic2.utils;

import org.bukkit.inventory.ItemStack;

public class MaterialData {

    private int id;
    private short durability;
    private final boolean strict;

    public MaterialData(ItemStack item) {
        id = item.getTypeId();
        durability = item.getDurability();
        strict = true;
    }

    public MaterialData(ItemStack item, boolean strict) {
        id = item.getTypeId();
        durability = item.getDurability();
        this.strict = strict;
    }

    public MaterialData(int id) {
        this.id = id;
        durability = 0;
        strict = false;
    }

    public MaterialData(int id, int durability) {
        this.id = id;
        this.durability = (short) durability;
        strict = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getDurability() {
        return durability;
    }

    public void setDurability(short durability) {
        this.durability = durability;
    }

    public boolean isStrict() {
        return strict;
    }

    public boolean compare(MaterialData md) {
        if (md.getId() == getId()) {
            if (!md.isStrict() || !isStrict()) {
                return true;
            } else {
                return md.getDurability() == getDurability();
            }
        }
        return false;
    }
}
