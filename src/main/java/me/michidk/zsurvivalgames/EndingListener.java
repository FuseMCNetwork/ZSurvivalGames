package me.michidk.zsurvivalgames;

import net.fusemc.zcore.PlayerLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class EndingListener implements Listener {

    private static Map<String, Location> deathMap = new HashMap<>();

    public EndingListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        e.setDeathMessage(null);
        deathMap.put(p.getName(), p.getLocation());

        if (p.getKiller() != null && p.getKiller() instanceof Player) {
            Player killer = p.getKiller();
            p.sendMessage("\u00A78\u00A7l[\u00A74\u2694\u00A78\u00A7l] \u00A73" + killer.getDisplayName() + " \u00A7ahat dich get\u00F6tet!");
            killer.sendMessage("\u00A78\u00A7l[\u00A72\u2694\u00A78\u00A7l] \u00A7aDu hast \u00A73" + p.getDisplayName() + " \u00A7aget\u00F6tet!");
        } else {
            p.sendMessage("\u00A78\u00A7l[\u00A74\u2694\u00A78\u00A7l] \u00A7aDu bist gestorben!");
        }

        GameManager.getInstance().getSpectatorManager().addSpectator(p);
        checkForWin();
    }

    @EventHandler
    public void onPlayerLeave(PlayerLeaveEvent e) {
        checkForWin();
    }

    private static void checkForWin() {
        List<Player> playing = GameManager.getInstance().getSpectatorManager().getPlayingPlayers();
        if (playing.size() == 1) {
            GameManager.getInstance().win(playing.get(0));  //player is the last playing player and has won the game
        } else if (playing.size() <= 0) {//on disconnect: bukkit.getOnlinePlayers().length() = 1; getPlayingPlayers().length() = 0;
            if (Bukkit.getOnlinePlayers().length <= 0) {
                Bukkit.shutdown();      //no player online
            } else {
                GameManager.endGame();  //spectators are online
            }
        }
    }

}
