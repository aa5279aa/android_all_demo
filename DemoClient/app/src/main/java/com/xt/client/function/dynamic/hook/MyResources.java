package com.xt.client.function.dynamic.hook;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import androidx.annotation.DrawableRes;

import com.xt.client.function.dynamic.manager.DynamicResourceManager;

public class MyResources extends Resources {

    /**
     * Create a new Resources object on top of an existing set of assets in an
     * AssetManager.
     *
     * @param assets  Previously created AssetManager.
     * @param metrics Current display metrics to consider when
     *                selecting/computing resource values.
     * @param config  Desired device configuration to consider when
     * @deprecated Resources should not be constructed by apps.
     * See {@link Context#createConfigurationContext(Configuration)}.
     */
    public MyResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        super(assets, metrics, config);
    }


    @Override
    public Drawable getDrawable(@DrawableRes int id, Theme theme)
            throws NotFoundException {
        try {
            return getDrawableForDensity(id, 0, theme);
        } catch (Exception e) {

        }
        return DynamicResourceManager.getInstance().resourcesMap.get("host").getDrawable(id, theme);
    }

}
