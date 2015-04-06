package me.michidk.zsurvivalgames.gamestate;

import me.michidk.zsurvivalgames.GameManager;
import me.michidk.zsurvivalgames.GameSettings;
import me.michidk.zsurvivalgames.utils.Helper;
import me.michidk.zsurvivalgames.utils.MathHelper;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.features.borderFeature.BorderFeature;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class DeathmatchState extends GameState {

    public static boolean started = false;

    public DeathmatchState() {
        super(5 * 60);
    }

    @Override
    public void enter() {
        if (started) return;
        started = true;

        GameManager.deathmatch = true;

        ZCore.getFeatureManager().getFeature(BorderFeature.class).disable();
        Bukkit.broadcastMessage(GameSettings.GAME_PREFIX + "Das Deathmatch hat begonnen!");


        for (int x = -2; x < 2; x++) {
            for (int z = -2; z < 2; z++) {
                Location loc = GameManager.getInstance().getSgDataManager().getDeathmatchSpawn(0).clone().add(x * 16, 0, z * 16);
                loc.getWorld().getChunkAt(loc.getBlock()).load();
            }
        }

        int i = 0;
        for (Player p:Bukkit.getOnlinePlayers()) {
            p.teleport(GameManager.getInstance().getSgDataManager().getDeathmatchSpawn(i));
            i++;
        }

    }

    @Override
    public void leave() {
        GameManager.getInstance().getWorld().setPVP(false);
        Bukkit.broadcastMessage(GameSettings.GAME_PREFIX + "Ihr habt zulange gebraucht. Niemand hat gewonnen!");
        GameManager.deathmatch = false;
        GameManager.endGame();
    }

    @Override
    public void onTick(int time) {
        if (time <= 10) {
            MathHelper.setXPProgress(time, super.startTime);
        }
        if (time <= 3) {
            Helper.playSound(Sound.ORB_PICKUP);
        }
    }

}
