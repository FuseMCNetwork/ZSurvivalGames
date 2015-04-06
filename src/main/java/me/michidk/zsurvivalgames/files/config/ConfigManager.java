package me.michidk.zsurvivalgames.files.config;

import me.michidk.DKLib.FileHelper;
import me.michidk.zsurvivalgames.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class ConfigManager {
    public final File file;

    public List<Material> destroyable;
    public List<Material> placeable;


    public ConfigManager() {
        Main.getInstance().getDataFolder().mkdirs();

        file = new File(Main.getInstance().getDataFolder(), "config.json");

        readData();
    }

    private void readData() {
        if (!file.exists()) {
            Main.getInstance().logger.log(Level.SEVERE, "no config.json found");
            Bukkit.shutdown();
            return;
        } else {
            ConfigData data = Main.getInstance().gson.fromJson(FileHelper.stringFromFile(file), ConfigData.class);
            destroyable = Arrays.asList(data.getDestroyable());
            placeable = Arrays.asList(data.getPlaceable());
        }
    }
}
