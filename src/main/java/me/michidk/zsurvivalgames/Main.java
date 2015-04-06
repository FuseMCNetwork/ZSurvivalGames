package me.michidk.zsurvivalgames;

import me.johnking.zsignadapter.ZSignAdapter;
import me.michidk.zsurvivalgames.files.chest.ChestManager;
import me.michidk.zsurvivalgames.files.config.ConfigManager;
import net.fusemc.zcore.ZCore;
import net.fusemc.zcore.featureSystem.FeatureManager;
import net.fusemc.zcore.featureSystem.features.BloodFeature;
import net.fusemc.zcore.featureSystem.features.ChatFormatFeature;
import net.fusemc.zcore.featureSystem.features.NoWeatherFeature;
import net.fusemc.zcore.featureSystem.features.TabTitleFeature;
import net.fusemc.zcore.featureSystem.features.corpseFeature.CorpseFeature;
import net.fusemc.zcore.featureSystem.features.disguiseFeature.DisguiseFeature;
import net.fusemc.zcore.featureSystem.features.lobbyFeature.LobbyFeature;
import net.fusemc.zcore.featureSystem.features.lobbyFeature.LobbySettings;
import net.fusemc.zcore.featureSystem.features.messageFeature.JoinLeaveFeature;
import net.fusemc.zcore.featureSystem.features.messageFeature.WelcomeFeature;
import net.fusemc.zcore.featureSystem.features.spectatorFeature.SpectatorFeature;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    private static Main instance = null;

    public Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Logger logger = Logger.getLogger(GameSettings.GAME_NAME);

    private GameManager gameManager;
    private ChestManager chestManager;
    private ConfigManager configManager;

    public static boolean useSignAdapter = false;

    @Override
    public void onEnable() {
        instance = this;

        //      - register ZCore features -
        // normal features: features with the .enable() methode [not .enable(some bullshit here)]
        List<Class> normalFeatures = Arrays.asList(new Class[]{
                JoinLeaveFeature.class,
                ChatFormatFeature.class,
                BloodFeature.class,
                //AntiWorldTransformFeature.class,we need blockupdates in SG
                NoWeatherFeature.class,
                SpectatorFeature.class,
                DisguiseFeature.class,
        });

        // register features with reflection
        FeatureManager fm = ZCore.getFeatureManager();
        for (Class clazz : normalFeatures) {
            try {
                clazz.getMethod("enable").invoke(fm.getFeature(clazz));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        // advanced features
        LobbySettings lobby = new LobbySettings(GameSettings.MIN_PLAYERS, GameSettings.MAX_PLAYERS, GameSettings.START_TIME, Bukkit.getWorlds().get(0));
        fm.getFeature(LobbyFeature.class).enable(lobby);
        //fm.getFeature(CorpseFeature.class).enable(20 * 15);   //disabled because create tablist entries & the dont sleep
        fm.getFeature(WelcomeFeature.class).enable(GameSettings.GAME_NAME);
        fm.getFeature(TabTitleFeature.class).enable("Survival Games");

        // initalize classes
        gameManager = new GameManager();
        chestManager = new ChestManager();
        configManager = new ConfigManager();

        useSignAdapter = Bukkit.getPluginManager().getPlugin("ZSignAdapter") != null;
        // register server in lobby
        if (useSignAdapter) {
            ZSignAdapter.register("SG");
        }

    }

    @Override
    public void onDisable() {

    }

    public static Main getInstance() {
        return instance;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ChestManager getChestManager() {
        return chestManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}