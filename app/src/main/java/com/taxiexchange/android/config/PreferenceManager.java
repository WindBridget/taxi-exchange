package com.taxiexchange.android.config;

import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by Nhahv on 11/4/2016.
 * <></>
 */

public class PreferenceManager {

    private static PreferenceManager mInstance;
    private static SharedPreferences mPreferences ;
    private static final String SHARE_PREFERENCES = "TAXI_EXCHANGE_PREFERENCES";

    public PreferenceManager() {
        mPreferences = Apps.getInstances().getSharedPreferences(SHARE_PREFERENCES, Context.MODE_PRIVATE);
    }

    public static void initPreference() {
        mPreferences = Apps.getInstances().getSharedPreferences(SHARE_PREFERENCES, Context.MODE_PRIVATE);

    }

    public static PreferenceManager getInstance() {
        if (mInstance == null) {
            mInstance = new PreferenceManager();
        }
        return mInstance;
    }

    public static void writePreference(String key, String value) {
        if (mPreferences == null) {
            initPreference();
        }
        mPreferences.edit().putString(key, value).apply();
    }

    public static void writePreference(String key, int value) {
        if (mPreferences == null) {
            initPreference();
        }
        mPreferences.edit().putInt(key, value).apply();
    }

    public static void writePreference(String key, boolean value) {
        if (mPreferences == null) {
            initPreference();
        }
        mPreferences.edit().putBoolean(key, value).apply();
    }

    public static void writePreference(String key, long value) {
        if (mPreferences == null) {
            initPreference();
        }
        mPreferences.edit().putLong(key, value).apply();
    }
    public static String getString(String key) {
        if (mPreferences == null) {
            initPreference();
        }
        return mPreferences.getString(key, null);
    }

    public static int getInt(String key) {
        if (mPreferences == null) {
            initPreference();
        }
        return mPreferences.getInt(key, -1);
    }

    public static long getLong(String key) {
        if (mPreferences == null) {
            initPreference();
        }
        return mPreferences.getLong(key, -1);
    }

    public static boolean getBoolean(String key) {
        if (mPreferences == null) {
            initPreference();
        }
        return mPreferences.getBoolean(key, false);
    }
}
