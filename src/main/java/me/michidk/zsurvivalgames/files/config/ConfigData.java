package me.michidk.zsurvivalgames.files.config;

import org.bukkit.Material;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class ConfigData {

    private Material[] destroyable;
    private Material[] placeable;

    public ConfigData() {

    }

    public Material[] getPlaceable() {
        return placeable;
    }

    public Material[] getDestroyable() {
        return destroyable;
    }
}
