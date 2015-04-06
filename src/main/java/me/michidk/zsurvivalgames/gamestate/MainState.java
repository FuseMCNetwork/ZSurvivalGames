package me.michidk.zsurvivalgames.gamestate;

import me.michidk.zsurvivalgames.GameManager;
import me.michidk.zsurvivalgames.GameSettings;
import net.fusemc.zcore.PlayerLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class MainState extends GameState {


    public MainState() {
        super(15 * 60);
    }

    @Override
    public void enter() {

    }

    @Override
    public void leave() {

    }

    @Override
    public void onTick(int time) {
        if (time == 5 * 60) Bukkit.broadcastMessage(GameSettings.GAME_PREFIX + "Noch 5 Minuten bis zum Deathmatch!");
        else if (time == 60) Bukkit.broadcastMessage(GameSettings.GAME_PREFIX + "Noch eine Minute bis zum Deathmatch!");
    }

    private boolean timeReduced = false;
    //handle deathmatch
    @EventHandler(priority = EventPriority.HIGH)
    public void onDeath(PlayerDeathEvent e) {
        if (timeReduced) return;
        if (GameManager.getInstance().getSpectatorManager().getPlayingPlayers().size() <= 4) {
            time = 65;
            timeReduced = true;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLeave(PlayerLeaveEvent e) {
        if (timeReduced) return;
        if (GameManager.getInstance().getSpectatorManager().getPlayingPlayers().size() <= 4) {
            time = 65;
            timeReduced = true;
        }
    }

}
