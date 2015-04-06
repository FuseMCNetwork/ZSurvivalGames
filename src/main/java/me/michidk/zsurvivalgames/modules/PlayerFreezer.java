package me.michidk.zsurvivalgames.modules;

import me.johnking.jlib.JLib;
import me.johnking.jlib.protocol.PacketType;
import me.johnking.jlib.protocol.event.ProtocolPacket;
import me.michidk.DKLib.event.PlayerBlockMoveEvent;
import me.michidk.zsurvivalgames.GameManager;
import me.michidk.zsurvivalgames.Main;
import net.fusemc.zcore.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created with IntelliJ IDEA.
 * User: ml
 * Date: 21.09.13
 * Time: 18:29
 */
public class PlayerFreezer implements Listener {

    private PotionEffect pe = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128);

    public void enable() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setWalkSpeed(0);

            World w = p.getWorld();
            w.setDifficulty(Difficulty.PEACEFUL);
            p.setFoodLevel(0);

            sendNoEffectPacket(p, true);
        }

    }

    public void disable() {
        HandlerList.unregisterAll(this);

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setWalkSpeed(0.2f);

            World w = p.getWorld();
            w.setDifficulty(GameManager.difficulty);
            p.setFoodLevel(20);

            sendNoEffectPacket(p, false);
        }
    }

    private void sendNoEffectPacket(Player p, boolean enable) {
        int data = 0;
        if (!enable) {
            data = 1;
        }

        try {
            if (enable) {
                ProtocolPacket packet = new ProtocolPacket(PacketType.Server.ENTITY_EFFECT);

                packet.setInt(0, p.getEntityId());
                packet.setByte(0, (byte) (pe.getType().getId() & 0xFF));
                packet.setByte(1, (byte) (pe.getAmplifier() & 0xFF));
                if (pe.getDuration() > 32767) {
                    packet.setShort(0, (short) 32767);
                } else {
                    packet.setShort(0, (short) pe.getDuration());
                }
                JLib.getProtocolManager().sendPacket(packet.getHandle());
            } else {
                ProtocolPacket packet = new ProtocolPacket(PacketType.Server.REMOVE_ENTITY_EFFECT);
                packet.setInt(0, p.getEntityId());
                packet.setInt(1, pe.getType().getId());
                JLib.getProtocolManager().sendPacket(packet.getHandle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerBlockMove(PlayerBlockMoveEvent e) {
        e.getPlayer().teleport(LocationUtil.centerLocation(e.getPlayer().getLocation()));
    }

    @EventHandler
    public void onTarget(EntityTargetLivingEntityEvent e) {
        if (!(e.getTarget() instanceof Player)) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent e) {
        e.setAmount(0);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        e.setCancelled(true);
    }
}
