package me.michidk.zsurvivalgames.gamestate;

import me.michidk.DKLib.effects.ParticleEffect;
import me.michidk.DKLib.utils.RandomFirework;
import me.michidk.zsurvivalgames.GameManager;
import me.michidk.zsurvivalgames.GameSettings;
import me.michidk.zsurvivalgames.utils.Helper;
import me.michidk.zsurvivalgames.utils.MathHelper;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class CountdownState extends GameState {


    public CountdownState() {
        super(17);
    }   //1 seconds world load time

    @Override
    public void enter() {
        GameManager.getInstance().getPlayerFreezer().enable();
    }

    @Override
    public void leave() {
        GameManager.getInstance().getPlayerFreezer().disable();

        for (Player p : Bukkit.getOnlinePlayers()) {
            Helper.effectBomb(p, ParticleEffect.WITCH_MAGIC);
            p.setExp(0);
            p.setTotalExperience(0);
            p.setLevel(0);
        }
        Helper.playSound(Sound.NOTE_PIANO);

        Bukkit.broadcastMessage(GameSettings.GAME_PREFIX + "M\u00F6gen die Spiele beginnen!");
        RandomFirework.spawnRandomFirework(GameManager.getInstance().getWorld().getSpawnLocation());
    }

    @Override
    public void onTick(int time) {
        MathHelper.setXPProgress(time, super.startTime);
        if (time <= 3) {
            Helper.playSound(Sound.ORB_PICKUP);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent e) {
        e.setCancelled(true);
    }

}
