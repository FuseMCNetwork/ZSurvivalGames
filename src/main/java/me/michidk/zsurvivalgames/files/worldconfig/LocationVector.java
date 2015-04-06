package me.michidk.zsurvivalgames.files.worldconfig;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class LocationVector extends Vector {

    private float yaw;
    private float pitch;

    public LocationVector() {
        super();
    }

    @Override
    public Location toLocation(World world) {
        return this.toLocation(world, yaw, pitch);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
