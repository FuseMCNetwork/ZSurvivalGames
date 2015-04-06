package me.michidk.zsurvivalgames.modules;

import com.google.common.collect.Lists;
import me.michidk.zsurvivalgames.GameManager;
import me.michidk.zsurvivalgames.Main;
import me.michidk.zsurvivalgames.gamestate.PeacefulState;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.features.CompassTargetFeature;
import net.fusemc.zcore.featureSystem.features.spectatorFeature.SpectatorController;
import net.fusemc.zcore.featureSystem.features.spectatorFeature.SpectatorFeature;
import net.fusemc.zcore.featureSystem.features.spectatorFeature.SpectatorListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Random;

/**
 * Copyright by michidk
 * Created: 10.08.2014.
 */
public class SpectatorManager implements Listener {

    private SpectatorFeature spectatorFeature;
    private Random random = new Random();

    public SpectatorManager() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        spectatorFeature = ZCore.getFeatureManager().getFeature(SpectatorFeature.class);
        spectatorFeature.enable();
    }

    public void addSpectator(final Player p) {
        if (getSpectators().contains(p)) return;

        ZCore.getFeatureManager().getFeature(CompassTargetFeature.class).removePlayer(p);
        spectatorFeature.addSpectator(p, true);

        if (p.getLocation().getY() <= 0) {
            p.setHealth(20);
            Player spawn = SpectatorController.getRandomPlayerWithoutACertainPlayer(p);
            if (spawn != null) {
                p.teleport(spawn.getLocation());
            } else {
                if (!GameManager.deathmatch) {  //not deathmatch
                    p.teleport(GameManager.getInstance().getSpawnManager().getSpawnByPlayer(p).toLocation(p.getWorld()));
                } else {                        //deathmatch
                    p.teleport(GameManager.getInstance().getSgDataManager().getDeathmatchSpawn(random.nextInt(4)));
                }
            }
        }

        p.sendMessage("\u00A78\u00A7l[\u00A7b\u2756\u00A78\u00A7l] \u00A7aDu bist nun Zuschauer.");
    }

    //wrappers
    public List<Player> getPlayingPlayers() {
        return spectatorFeature.getPlayingPlayers();
    }

    public List<Player> getSpectators() {
        return spectatorFeature.getSpectators();
    }

    public boolean isPlayingPlayer(Player p) {
        return getPlayingPlayers().contains(p);
    }

    public boolean isSpectator(Player p) {
        return getSpectators().contains(p);
    }

    public SpectatorFeature getSpectatorFeature() {
        return spectatorFeature;
    }
}
