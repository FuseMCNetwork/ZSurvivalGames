package me.michidk.zsurvivalgames.utils;

import me.michidk.DKLib.RandomExt;
import me.michidk.DKLib.effects.ParticleEffect;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ml
 * Date: 27.09.13
 * Time: 22:21
 */
public class Helper {

    static RandomExt random = new RandomExt(new Random());

    public static void makeInvisible(Player p) {
        for (Player pe : Bukkit.getOnlinePlayers()) {
            pe.hidePlayer(p);
        }
    }

    public static void cleanPlayer(Player p) {
        p.setFlying(false);
        p.setHealth(20);
        p.setExp(0);
        p.setLevel(0);
        p.setFoodLevel(20);
        p.setFireTicks(0);

        clearInv(p);

        for (PotionEffect t : p.getActivePotionEffects()) {
            p.removePotionEffect(t.getType());
        }
    }

    public static void clearInv(Player p) {
        PlayerInventory i = p.getInventory();
        i.clear();
        i.setArmorContents(null);
    }

    public static void effectBomb(Player p, ParticleEffect pe) {
        List<Location> list = MathHelper.getCircleLocs(p.getLocation(), 5, 5, false, true, 0);
        for (Location l : list) {
            if (random.percentChance(0.10)) {
                pe.play(l.add(0, 2, 0), 0, 0, 0, 0, 5);
            }
        }
    }

    public static void playSound(Sound s) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), s, 1, 1);
        }
    }

    public static void playNote(Instrument i, Note n) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playNote(p.getLocation(), i, n);
        }

    }

}
