package me.michidk.zsurvivalgames.gamestate;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class GameStateOverEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private GameState oldState;

    public GameStateOverEvent(GameState oldState) {
        this.oldState = oldState;
    }

    public GameState getOldState() {
        return oldState;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
