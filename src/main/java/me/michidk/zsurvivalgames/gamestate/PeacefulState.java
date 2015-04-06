package me.michidk.zsurvivalgames.gamestate;

import me.michidk.zsurvivalgames.GameManager;
import me.michidk.zsurvivalgames.GameSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class PeacefulState extends GameState {


    public PeacefulState() {
        super(20);
    }

    private boolean deathmatch;
    Random random = new Random();

    @Override
    public void enter() {
        GameManager.peaceful = true;
        GameManager.getInstance().getWorld().setPVP(false);
        Bukkit.broadcastMessage(GameSettings.GAME_PREFIX + "20 Sekunden Unverwundbarkeit.");
    }

    @Override
    public void leave() {
        GameManager.peaceful = false;
        GameManager.getInstance().getWorld().setPVP(true);
        Bukkit.broadcastMessage(GameSettings.GAME_PREFIX + "Die Unverwundbarkeit ist vorbei.");
    }

    @Override
    public void onTick(int time) {
    }

    @EventHandler
    public void onVoid(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getCause() != EntityDamageEvent.DamageCause.VOID) return;
        Player p = (Player) e.getEntity();

        p.setVelocity(new Vector(0,0,0));   //stop from falling
        if (!deathmatch) {  //not deathmatch
            p.teleport(GameManager.getInstance().getSpawnManager().getSpawnByPlayer(p).toLocation(p.getWorld()));
        } else {            //deathmatch
            p.teleport(GameManager.getInstance().getSgDataManager().getDeathmatchSpawn(random.nextInt(4)));
        }

        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (e.getCause() == EntityDamageEvent.DamageCause.VOID) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        e.setCancelled(true);
    }

    public boolean isDeathmatch() {
        return deathmatch;
    }

    public void setDeathmatch(boolean deathmatch) {
        this.deathmatch = deathmatch;
    }
}
