package me.michidk.zsurvivalgames.files.chest;

import org.bukkit.Material;

/**
 * Copyright by michidk
 * Created: 14.08.2014.
 */
public class ChestItem {

    private Material material;
    private int subid;
    private int maxStackSize;
    private float chance;
    private int minDamage;
    private int maxDamage;

    public ChestItem() {
    }

    public ChestItem(Material material, int subid, int maxStackSize, float chance, int minDamage, int maxDamage) {
        this.material = material;
        this.subid = subid;
        this.maxStackSize = maxStackSize;
        this.chance = chance;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

    public Material getMaterial() {
        return material;
    }

    public int getSubid() {
        return subid;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public float getChance() {
        return chance;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
    }
}
