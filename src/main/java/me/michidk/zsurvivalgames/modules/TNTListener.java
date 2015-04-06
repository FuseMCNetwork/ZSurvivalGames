package me.michidk.zsurvivalgames.modules;

import me.johnking.jlib.protocol.util.EntityUtilities;
import me.michidk.zsurvivalgames.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.HashMap;

/**
 * Copyright by michidk
 * Date: 05.10.13
 * Time: 13:47
 */
public class TNTListener implements Listener {

    private static HashMap<Integer, String> list = new HashMap<>();

    public TNTListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    //canceling destroying tnt
    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        if (e.getBlock().getType().equals(Material.TNT)) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if (!(e.getBlock().getType() == Material.TNT)) {
            return;
        }

        e.getBlock().setType(Material.AIR);

        Location loc = e.getBlock().getLocation();
        loc.add(0.5D, 0.5D, 0.5D);

        //cast not necessary, but good to know
        TNTPrimed tnt = (TNTPrimed) p.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
        tnt.setFuseTicks(tnt.getFuseTicks() - 30);  //recude fuse time by 1.5 seconds
        list.put(tnt.getEntityId(), p.getName());
        e.setCancelled(false);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (!(e.getEntity().getType() == EntityType.PRIMED_TNT)) {
            return;
        }

        int EID = e.getEntity().getEntityId();
        if (!list.containsKey(EID)) {
            return;
        }

        e.setCancelled(true);
        Location l = e.getLocation();
        l.getWorld().createExplosion(l.getX(), l.getY(), l.getZ(), 2F, false, false);   //normal TNT: 4F
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) return;
        if (!(e.getEntity() instanceof Player || e.getDamager() instanceof Player)) return;

        if (e.getEntity().getType() != EntityType.PLAYER) {
            e.setCancelled(true);
        }

        Player damager = Bukkit.getPlayerExact(list.get(e.getDamager().getEntityId()));
        EntityUtilities.setKiller((LivingEntity) e.getEntity(), damager, 20 * 10);
    }


}
