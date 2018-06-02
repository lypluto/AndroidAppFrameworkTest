package com.lltest.util;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 *
 */

public class PreferenceUtil {
    private static final String CONFIG_PATH = "config";
    public static final String KEY_LOGIN_TOKEN = "LoginToken";

    // TODO: defire more sharedPref keys here:

    public static String getPrefString(Context context, String key, final String defaultValue) {
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    public static void setPrefString(Context context, final String key, final String value) {
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        settings.edit().putString(key, value).apply();
    }

    public static boolean getPrefBoolean(Context context, final String key, final boolean defaultValue) {
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    public static boolean hasKey(Context context, final String key) {
        return context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE).contains(key);
    }

    public static void setPrefBoolean(Context context, final String key, final boolean value) {
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        settings.edit().putBoolean(key, value).apply();
    }

    public static void setPrefInt(Context context, final String key, final int value) {
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        settings.edit().putInt(key, value).apply();
    }

    public static int getPrefInt(Context context, final String key, final int defaultValue) {
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    public static void setPrefFloat(Context context, final String key, final float value) {
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        settings.edit().putFloat(key, value).apply();
    }

    public static float getPrefFloat(Context context, final String key, final float defaultValue) {
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    public static void setPrefLong(Context context, final String key, final long value) {
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        settings.edit().putLong(key, value).apply();
    }

    public static long getPrefLong(Context context, final String key, final long defaultValue) {
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    public static void removePref(Context context, final String key){
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        settings.edit().remove(key).apply();
    }

    public static void clearPreference(Context context) {
        final SharedPreferences settings =
                context.getSharedPreferences(CONFIG_PATH, MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
    }
}
