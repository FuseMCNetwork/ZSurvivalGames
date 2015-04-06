package me.michidk.zsurvivalgames;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class GameListener implements Listener {

    public GameListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onJoin(PlayerLoginEvent e) {
        if (GameManager.started) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, GameSettings.ERROR_PREFIX + "Die Runde hat bereits begonnen!");
        }
    }

    //config start
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Material mat = e.getBlock().getType();
        if (!Main.getInstance().getConfigManager().placeable.contains(mat)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Material mat = e.getBlock().getType();
        if (!Main.getInstance().getConfigManager().destroyable.contains(mat)) {
            e.setCancelled(true);
        }
    }
    //end

    //banned crafting materials start
    private List<Material> bannedCraftMaterials = Arrays.asList(new Material[]{Material.BUCKET, Material.WATER, Material.STATIONARY_WATER, Material.WATER_BUCKET, Material.LAVA, Material.STATIONARY_LAVA, Material.LAVA_BUCKET});

    @EventHandler
    public void onPlayerBucket(CraftItemEvent e) {
        if (bannedCraftMaterials.contains(e.getRecipe().getResult().getType())) {
            e.setResult(Event.Result.DENY);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerPreBucket(PrepareItemCraftEvent e) {
        if (bannedCraftMaterials.contains(e.getRecipe().getResult().getType())) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }
    //end

    @EventHandler
    public void onEnder(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }

        if (e.getItem().getType().equals(Material.ENDER_PEARL) || e.getItem().getType().equals(Material.EYE_OF_ENDER)) {
            e.setCancelled(true);
        }

    }

    @EventHandler
    public void onBedInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (e.getClickedBlock().getType() == Material.BED) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onHangingDestroy(HangingBreakByEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onFrameRotate(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof ItemFrame) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRemoveFrameItem(EntityDamageByEntityEvent e) {
        if (e.getEntityType() == EntityType.ITEM_FRAME) {
            e.setDamage(0);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        Iterator<Block> iter = e.blockList().iterator();
        while (iter.hasNext()) {
            Block b = iter.next();
            if (b.getType() == Material.ITEM_FRAME || b.getType() == Material.PAINTING) {
                iter.remove();
            }
        }
    }

    @EventHandler
    public void onGrow(BlockGrowEvent e) {
        e.setCancelled(false);
    }

}
