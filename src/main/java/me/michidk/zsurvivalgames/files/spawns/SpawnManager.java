package me.michidk.zsurvivalgames.files.spawns;

import com.google.common.collect.Lists;
import me.michidk.DKLib.FileHelper;
import me.michidk.zsurvivalgames.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class SpawnManager {

    private World world;

    private File file;
    private Spawn[] spawns;

    public SpawnManager(World world) {
        this.world = world;

        file = new File(world.getWorldFolder(), "spawns.json");

        loadSpawns();
    }

    private void loadSpawns() {
        if (!file.exists()) {
            Main.getInstance().logger.log(Level.SEVERE, "spawns.json for world \"" + world.getName() + "\" not found");
            Bukkit.shutdown();
            return;
        } else {
            spawns = Main.getInstance().gson.fromJson(FileHelper.stringFromFile(file), Spawn[].class);
        }
    }

    public Spawn getSpawnByPlayer(Player p) {
        for (Spawn spawn : spawns) {
            if (!spawn.hasPlayer()) continue;
            if (spawn.getPlayer().equals(p)) {
                return spawn;
            }
        }
        return null;
    }

    public Spawn getFreeSpawn() {
        List<Spawn> list = Lists.newArrayList(spawns.clone());
        Collections.shuffle(list);
        for (Spawn spawn : list) {
            if (!spawn.isOccupied() && !spawn.hasPlayer()) {
                return spawn;
            }
        }
        return null;
    }

}
