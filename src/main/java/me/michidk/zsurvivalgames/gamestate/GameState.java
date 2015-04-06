package me.michidk.zsurvivalgames.gamestate;

import me.michidk.zsurvivalgames.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public abstract class GameState implements Listener, Runnable {

    public int startTime;
    public int time;
    public boolean active;
    private int schedulerID;

    public static List<GameState> running = new ArrayList<>();

    public GameState(int time) {
        this.startTime = time;
        this.time = time;

        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        schedulerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), this, 0L, 20L);
        enter();

        this.active = true;
        running.add(this);
    }

    public abstract void enter();

    public abstract void leave();

    public abstract void onTick(int time);

    @Override
    public void run() {
        onTick(time);
        if (time <= 0) {
            end();
            return;
        }
        time--;
    }

    public void kill() {
        Bukkit.getScheduler().cancelTask(schedulerID);
        this.active = false;
        running.remove(this);
    }

    public void end() {
        kill();
        leave();
        HandlerList.unregisterAll(this);
        GameStateOverEvent event = new GameStateOverEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }

}
