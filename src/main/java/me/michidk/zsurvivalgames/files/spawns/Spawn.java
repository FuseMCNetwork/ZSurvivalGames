package me.michidk.zsurvivalgames.files.spawns;

import me.michidk.zsurvivalgames.utils.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class Spawn extends Vector {

    private transient boolean occupied = false;
    private transient String player = null;

    public Spawn() {
        super();
    }

    @Override
    public Location toLocation(World world) {
        Location source = new Location(world, x, y, z);
        Location target = source.getWorld().getSpawnLocation();

        Vector direction = target.toVector().subtract(source.toVector()).normalize().multiply(-1);

        double x = direction.getX();
        double y = direction.getY();
        double z = direction.getZ();

        // now change the angle
        Location changed = source.clone();
        changed.setYaw(180 - MathHelper.toDegree(Math.atan2(x, z)));
        changed.setPitch(90 - MathHelper.toDegree(Math.acos(y)));
        return changed;
    }

    public void setPlayer(String player) {
        this.player = player;
        this.occupied = true;
    }

    public void setPlayer(Player player) {
        setPlayer(player.getName());
    }

    public void removePlayer() {
        this.player = null;
        this.occupied = false;
    }

    public boolean hasPlayer() {
        if (player == null) {
            return false;
        } else {
            return true;
        }
    }

    public Player getPlayer() {
        return Bukkit.getPlayerExact(player);
    }

    public boolean isOccupied() {
        return occupied;
    }

    public String getPlayerName() {
        return player;
    }
}
