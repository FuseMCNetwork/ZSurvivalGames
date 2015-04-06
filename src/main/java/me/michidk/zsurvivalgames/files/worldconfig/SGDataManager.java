package me.michidk.zsurvivalgames.files.worldconfig;

import me.michidk.DKLib.FileHelper;
import me.michidk.zsurvivalgames.Main;
import net.fusemc.zcore.featureSystem.features.borderFeature.BorderSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.logging.Level;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class SGDataManager {

    private World world;

    private File file;
    private SGData data;

    public SGDataManager(World world) {
        this.world = world;

        file = new File(world.getWorldFolder(), "sgdata.json");

        loadData();
    }

    private void loadData() {
        if (!file.exists()) {
            Main.getInstance().logger.log(Level.SEVERE, "sgdata.json for world \"" + world.getName() + "\" not found");
            Bukkit.shutdown();
            return;
        } else {
            data = Main.getInstance().gson.fromJson(FileHelper.stringFromFile(file), SGData.class);
        }
    }

    //spawns 0-3    size: 4
    public Location getDeathmatchSpawn(int i) {
        if (i + 1 > data.getDeathmatchSpawns().length || i < 0) i = 0;  //if i too big or too small use the first spawn
        return data.getDeathmatchSpawns()[i].toLocation(world);
    }

    public BorderSettings.Shape getBordeShape() {
        return data.getBorderShape();
    }

    public int getBorderRadius() {
        return data.getBorderRadius();
    }

}
