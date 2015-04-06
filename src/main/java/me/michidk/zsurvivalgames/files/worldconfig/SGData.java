package me.michidk.zsurvivalgames.files.worldconfig;

import net.fusemc.zcore.featureSystem.features.borderFeature.BorderSettings;

/**
 * Copyright by michidk
 * Created: 15.08.2014.
 */
public class SGData {

    private LocationVector[] deathmatchSpawns;
    private BorderSettings.Shape borderShape;
    private int borderRadius;

    public SGData() {

    }

    public LocationVector[] getDeathmatchSpawns() {
        return deathmatchSpawns;
    }

    public BorderSettings.Shape getBorderShape() {
        return borderShape;
    }

    public int getBorderRadius() {
        return borderRadius;
    }
}
