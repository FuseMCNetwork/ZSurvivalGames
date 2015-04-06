package me.michidk.zsurvivalgames.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class SGHelper {

    public static boolean isChest(Block b) {
        return b.getType() == Material.ENDER_CHEST;
    }

    public static boolean isOpChest(Block b) {
        return isChest(b) && b.getData() == 10;
    }
}
