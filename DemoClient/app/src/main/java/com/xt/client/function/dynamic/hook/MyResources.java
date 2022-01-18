package com.xt.client.function.dynamic.hook;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import androidx.annotation.StringRes;

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
    public String getString(@StringRes int id) throws NotFoundException {
        Resources plugin = DynamicResourceManager.getInstance().resourcesMap.get("plugin");
        if (plugin != null) {
            return plugin.getString(id);
        }
        return getText(id).toString();
    }


}
