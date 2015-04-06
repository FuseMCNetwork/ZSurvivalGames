package me.michidk.zsurvivalgames.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ml
 * Date: 03.09.13
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
public class MathHelper {

    public static List<Location> getCircleLocs(Location loc, Integer r, Integer h, Boolean hollow, Boolean sphere, int plus_y) {
        List<Location> circleblocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx + r; x++) {
            for (int z = cz - r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        circleblocks.add(new Location(loc.getWorld(), x, y + plus_y, z));
                    }
                }
            }
        }

        return circleblocks;
    }

    public static double getPercentage(int now, int max) {
        return Math.round(((double) now / (double) max) * 100D) / 100D;
    }

    public static void setXPProgress(Player p, int now, int max) {
        p.setLevel(now);
        p.setExp(1 - (float) (getPercentage(now, max)));
    }

    public static void setXPProgress(int now, int max) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            setXPProgress(p, now, max);
        }
    }

    public static float toDegree(double angle) {
        return (float) Math.toDegrees(angle);
    }

}
