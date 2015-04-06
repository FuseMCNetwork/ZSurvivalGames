package me.michidk.zsurvivalgames.modules;

import me.michidk.DKLib.RandomExt;
import me.michidk.zsurvivalgames.GameManager;
import me.michidk.zsurvivalgames.GameSettings;
import me.michidk.zsurvivalgames.Main;
import me.michidk.zsurvivalgames.utils.SGHelper;
import net.fusemc.zcore.util.LocationUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class SpecialBlockListener implements Listener {

    private RandomExt random = new RandomExt(new Random());

    public SpecialBlockListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    //chest start
    @EventHandler(ignoreCancelled = true)
    public void onChestOpen(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (e.getClickedBlock() == null) {
            return;
        }

        if (!GameManager.getInstance().getSpectatorManager().getPlayingPlayers().contains(e.getPlayer())) return;

        Player p = e.getPlayer();
        Block b = e.getClickedBlock();
        if (SGHelper.isOpChest(b)) {
            p.openInventory(Main.getInstance().getChestManager().getOpChest(e.getClickedBlock().getLocation()));
            p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1, 0);
            e.setCancelled(true);
        } else if (SGHelper.isChest(b)) {
            p.openInventory(Main.getInstance().getChestManager().getNormalChest(e.getClickedBlock().getLocation()));
            p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1, 1);
            e.setCancelled(true);
        } else if (b.getType().equals(Material.FURNACE) || b.getType().equals(Material.BURNING_FURNACE)) {
            Furnace furnace = (Furnace) b.getState();
            p.openInventory(Main.getInstance().getChestManager().getFurnance(e.getClickedBlock().getLocation()));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onChestClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (!(e.getInventory().getName().equals(GameSettings.CHESTNAME) || e.getInventory().getName().equals(GameSettings.OPCHESTNAME))) {
            return;
        }
        p.playSound(p.getLocation(), Sound.CHEST_CLOSE, 1, 1);
    }
    //end

    @EventHandler(ignoreCancelled = true)
    public void onWheat(BlockBreakEvent e) {
        if (!e.getBlock().getType().equals(Material.CROPS)) {
            return;
        }
        Player p = e.getPlayer();
        Block b = e.getBlock();
        World w = b.getWorld();

        Location l = LocationUtil.centerLocation(b.getLocation());

        //remove drops
        e.setCancelled(true);
        b.setType(Material.AIR);

        //30% chance to drop wheat
        if (random.percentChance(0.3)) {
            w.dropItemNaturally(l, new ItemStack(Material.WHEAT));
        }
        /*  create laggs
        //drop 0-2 seeds per block break
        if (random.percentChance(0.3)) {
            int count = random.randInt(0, 2);
            for (int i = 0; i < count; i++) {
                w.dropItemNaturally(l, new ItemStack(Material.SEEDS));
            }
        }
        */
    }

    /*  blockupdates are enabled now
    @EventHandler(ignoreCancelled = true)
    public void onSugar(BlockBreakEvent e) {   //to recreate blocked physics
        if (!e.getBlock().getType().equals(Material.SUGAR_CANE_BLOCK)) {
            return;
        }
        e.setCancelled(true);

        Location loc = e.getBlock().getLocation().clone();

        for (int x = 0; x < 3; x++) {
            if (loc.getBlock().getType().equals(Material.SUGAR_CANE_BLOCK)) {

                loc.getBlock().setType(Material.AIR);
                loc.getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(Material.SUGAR_CANE));

                loc.add(0, 1, 0);
            } else {
                return;
            }
        }
    }
    */

}
