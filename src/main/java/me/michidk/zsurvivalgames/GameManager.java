package me.michidk.zsurvivalgames;

import me.johnking.zsignadapter.ZSignAdapter;
import me.michidk.DKLib.BlockLocation;
import me.michidk.zsurvivalgames.files.chest.ChestManager;
import me.michidk.zsurvivalgames.files.spawns.Spawn;
import me.michidk.zsurvivalgames.files.spawns.SpawnManager;
import me.michidk.zsurvivalgames.files.worldconfig.SGDataManager;
import me.michidk.zsurvivalgames.gamestate.*;
import me.michidk.zsurvivalgames.modules.PlayerFreezer;
import me.michidk.zsurvivalgames.modules.SpecialBlockListener;
import me.michidk.zsurvivalgames.modules.SpectatorManager;
import me.michidk.zsurvivalgames.modules.TNTListener;
import me.michidk.zsurvivalgames.utils.Helper;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.FeatureManager;
import net.fusemc.zcore.featureSystem.features.CompassTargetFeature;
import net.fusemc.zcore.featureSystem.features.borderFeature.BorderFeature;
import net.fusemc.zcore.featureSystem.features.borderFeature.BorderSettings;
import net.fusemc.zcore.featureSystem.features.disguiseFeature.DisguiseFeature;
import net.fusemc.zcore.featureSystem.features.lobbyFeature.CountdownBreakEvent;
import net.fusemc.zcore.featureSystem.features.lobbyFeature.CountdownTickEvent;
import net.fusemc.zcore.featureSystem.features.lobbyFeature.GameStartEvent;
import net.fusemc.zcore.featureSystem.features.spectatorFeature.SpectatorFeature;
import net.fusemc.zcore.util.LocationUtil;
import net.fusemc.zcore.util.WinHelper;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class GameManager implements Listener {

    private static GameManager instance;

    private SpectatorManager spectatorManager;
    private PlayerFreezer playerFreezer;
    private TNTListener tntListener;
    private SpawnManager spawnManager;
    private SGDataManager sgDataManager;
    private GameListener gameListener;

    private World world;
    private Player winner;
    private Player minigameWinner;

    public static boolean started = false;
    public static boolean peaceful = false;
    public static boolean deathmatch = false;
    public static Difficulty difficulty = Difficulty.NORMAL;

    public GameManager() {
        instance = this;

        this.spectatorManager = new SpectatorManager();
        this.playerFreezer = new PlayerFreezer();
        this.tntListener = new TNTListener();

        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler
    public void onCountdownTick(CountdownTickEvent e) {
        if (e.getTime() == 10) {
            if (Main.useSignAdapter) {
                ZSignAdapter.setJoinable(false);
            }
        }
    }

    @EventHandler
    public void onCountdownBreak(CountdownBreakEvent e) {
        if (Main.useSignAdapter) {
            ZSignAdapter.setJoinable(true);
        }
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        this.world = e.getWorld();
        this.minigameWinner = e.getMinigameWinner();

        //unnecessary but nice
        if (Main.useSignAdapter) {
            ZSignAdapter.setJoinable(false);
        }

        //world settings
        world.setPVP(false);
        world.setAutoSave(false);
        world.setDifficulty(difficulty);
        world.setGameRuleValue("doDaylightCycle", "true");
        world.setGameRuleValue("keepInventory", "false");
        world.setGameRuleValue("mobGriefing", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("naturalRegeneration", "true");
        world.setDifficulty(Difficulty.NORMAL);

        //world settings
        sgDataManager = new SGDataManager(world);

        //enable some advanced features
        FeatureManager fm = ZCore.getFeatureManager();
        fm.getFeature(CompassTargetFeature.class).enable(LocationUtil.centerLocation(world.getSpawnLocation()));
        fm.getFeature(BorderFeature.class).enable(new BorderSettings(sgDataManager.getBorderRadius(), sgDataManager.getBordeShape(), 5, BorderSettings.Shape.VERTICAL_SQUARE_TUBE, BlockLocation.fromLocation(world.getSpawnLocation())));

        //spawn them
        spawnManager = new SpawnManager(world);
        for (Player p : Bukkit.getOnlinePlayers()) {
            Spawn spawn = spawnManager.getFreeSpawn();
            spawn.setPlayer(p);
            p.teleport(spawn.toLocation(world));
            Helper.cleanPlayer(p);
        }

        //give random start items
        if (e.getMinigameWinner() != null) {
            Bukkit.broadcastMessage(GameSettings.GAME_PREFIX + "\u00A73" + minigameWinner.getName() + " \u00A7ahatte als letzter den Goldblock und bekommt \u00A73Startitems\u00A7!");
            randomStartItems(e.getMinigameWinner());
        }

        //start the game!
        new CountdownState();
        gameListener = new GameListener();
        new SpecialBlockListener();
        new EndingListener();
    }

    @EventHandler
    public void onGameStateOver(GameStateOverEvent e) {
        if (e.getOldState() instanceof CountdownState) {                            //after countdown
            new PeacefulState();                                                    //peace time
        } else if (e.getOldState() instanceof PeacefulState) {                      //after peaceful time (if it was not the deathmatch)
            new MainState();                                                        //enable mainstate
        } else if (e.getOldState() instanceof MainState) {                          //if main state is over
            new DeathmatchState();                                                  //enable Deathmatchstate for deathmatch things
        }
    }

    public void randomStartItems(Player winner) {
        if (winner == null) return;
        int count = new Random().nextInt(3) + 1;
        PlayerInventory inv = winner.getInventory();
        for (int i = 0; i < 3; i++) {
            inv.addItem(ChestManager.getRandomItem(Main.getInstance().getChestManager().normalItems));
        }
    }

    public void win(Player winner) {
        if (this.winner != null) {
            return;
        }
        this.winner = winner;
        world.setPVP(false);

        while (GameState.running.size() > 0) {
            GameState.running.get(0).kill(); 
        }

        WinHelper.win(winner);
    }

    public static void endGame() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Main.getInstance().getGameManager().getSpectatorManager().getSpectatorFeature().removeSpectator(p);
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Player p:Bukkit.getOnlinePlayers()) {
                    p.kickPlayer("lobby");
                }
                Bukkit.shutdown();
            }
        }, 10 * 20L);
    }

    public static GameManager getInstance() {
        return instance;
    }

    public World getWorld() {
        return world;
    }

    public SpectatorManager getSpectatorManager() {
        return spectatorManager;
    }

    public PlayerFreezer getPlayerFreezer() {
        return playerFreezer;
    }

    public SpawnManager getSpawnManager() {
        return spawnManager;
    }

    public TNTListener getTntListener() {
        return tntListener;
    }

    public SGDataManager getSgDataManager() {
        return sgDataManager;
    }
}
