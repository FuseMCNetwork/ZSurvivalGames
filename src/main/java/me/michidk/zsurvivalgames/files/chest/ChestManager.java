package me.michidk.zsurvivalgames.files.chest;

import me.michidk.DKLib.FileHelper;
import me.michidk.DKLib.RandomExt;
import me.michidk.zsurvivalgames.GameSettings;
import me.michidk.zsurvivalgames.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

/**
 * Copyright by michidk
 * Created: 14.08.2014.
 */
public class ChestManager {

    public final File normalChest;
    public final File opChest;
    public final File furnanceChest;

    public ChestItem[] normalItems;
    public ChestItem[] opItems;
    public ChestItem[] furnanceItems;

    private static RandomExt random = new RandomExt(new Random());
    private HashMap<Location, Inventory> chestList = new HashMap<>();

    public ChestManager() {
        Main.getInstance().getDataFolder().mkdirs();

        normalChest = new File(Main.getInstance().getDataFolder(), "chest.json");
        opChest = new File(Main.getInstance().getDataFolder(), "opchest.json");
        furnanceChest = new File(Main.getInstance().getDataFolder(), "furnace.json");

        readData();
    }

    private void readData() {
        //check normal chest
        if (!normalChest.exists()) {
            Main.getInstance().logger.log(Level.SEVERE, "no chest.json found");
            Bukkit.shutdown();
            return;
        } else {
            normalItems = Main.getInstance().gson.fromJson(FileHelper.stringFromFile(normalChest), ChestItem[].class);
        }
        //check opchest
        if (!opChest.exists()) {
            Main.getInstance().logger.log(Level.WARNING, "no opchest.json found, using normal items");
            opItems = normalItems;
        } else {
            opItems = Main.getInstance().gson.fromJson(FileHelper.stringFromFile(opChest), ChestItem[].class);
        }
        //check furnancechest
        if (!furnanceChest.exists()) {
            Main.getInstance().logger.log(Level.WARNING, "no furnace.json found, using normal items");
            furnanceItems = normalItems;
        } else {
            furnanceItems = Main.getInstance().gson.fromJson(FileHelper.stringFromFile(furnanceChest), ChestItem[].class);
        }
    }

    public Inventory getNormalChest(Location loc) {
        if (chestList.containsKey(loc)) {
            return chestList.get(loc);
        }

        Inventory inv = Bukkit.createInventory(null, 27, GameSettings.CHESTNAME);

        //max itemstacks per chestSystem
        int max = random.randInt(2, 5);
        for (int c = 0; c < max; c++) {
            int r = random.randInt(0, 26);
            inv.setItem(r, getRandomItem(normalItems));
        }

        chestList.put(loc, inv);
        return inv;
    }

    public Inventory getOpChest(Location loc) {
        if (chestList.containsKey(loc)) {
            return chestList.get(loc);
        }

        Inventory inv = Bukkit.createInventory(null, 27, GameSettings.OPCHESTNAME);

        //max itemstacks per chestSystem
        int max = random.randInt(1, 9);
        for (int c = 0; c < max; c++) {
            int r = random.randInt(0, 26);
            inv.setItem(r, getRandomItem(opItems));
        }

        chestList.put(loc, inv);
        return inv;
    }

    public Inventory getFurnance(Location loc) {
        if (!(loc.getBlock().getType().equals(Material.FURNACE) || loc.getBlock().getType().equals(Material.BURNING_FURNACE))) return null;
        Block b = loc.getBlock();
        Furnace furnace = (Furnace) b.getState();

        if (chestList.containsKey(loc)) {
            return furnace.getInventory();
        }

        //max itemstacks per chestSystem
        for (int c = 0; c < furnace.getInventory().getSize(); c++) {
            if (random.percentChance(0.3)) {
                furnace.getInventory().setItem(c, getRandomItem(furnanceItems));
            }
        }

        chestList.put(loc, furnace.getInventory());
        return furnace.getInventory();
    }

    public static ItemStack getRandomItem(ChestItem[] items) {
        double completeWeight = 0.0;
        for (ChestItem item : items) {
            completeWeight += item.getChance();
        }
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (ChestItem item : items) {
            countWeight += item.getChance();
            if (countWeight >= r) {
                ItemStack i = new ItemStack(item.getMaterial());
                i.setData(new MaterialData(item.getMaterial(), (byte) item.getSubid()));
                if (!(item.getMinDamage() == 0 && item.getMaxDamage() == 0)) {
                    //random number between min damage and maxdamage
                    i.setDurability(randomShort(item.getMinDamage(), item.getMaxDamage()));
                }
                i.setAmount(random.randInt(1, item.getMaxStackSize()));
                return i;
            }
        }
        return null;
    }

    public static int randomInt(int min, int max) {
        return random.randInt(min, max);
    }

    public static short randomShort(int min, int max) {
        return (short) randomInt(min, max);
    }

}
