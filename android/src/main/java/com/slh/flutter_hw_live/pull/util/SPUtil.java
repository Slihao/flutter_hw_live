package com.slh.flutter_hw_live.pull.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 用于存储新增的profile
 *
 * @author
 */

public class SPUtil {
    private static final String TAG = "SPUtil";
    private static SPUtil mSPUtil;
    /**
     * sharepreference存储文件名称
     */
    private static final String PREFERENCE_FILE_NAME = "fjyd_SharedPreference";

    public static SharedPreferences getSP() {
        return get().sharedPreferences;
    }

    /**
     * 获取单例
     *
     * @return
     */
    public static SPUtil get() {
        if (null == mSPUtil) {
            mSPUtil = new SPUtil();
        }
        return mSPUtil;
    }

    private final SharedPreferences sharedPreferences = UtilBase.context().getSharedPreferences(PREFERENCE_FILE_NAME, Context
            .MODE_PRIVATE);

    public static String getStringData(String key, String defaultValue) {
        String content = getSP().getString(key, null);
        return TextUtils.isEmpty(content) ? defaultValue : content;
    }

    public static int getIntData(String key, int defaultValue) {
        return getSP().getInt(key, defaultValue);
    }

    public static boolean getBoolData(String key, boolean defaultValue) {
        return getSP().getBoolean(key, defaultValue);
    }

    public static long getLongData(String key, long defaultValue) {
        return getSP().getLong(key, defaultValue);
    }

    public static void setStringData(String key, String value) {
        SharedPreferences.Editor editor = getSP().edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setIntData(String key, int value) {
        SharedPreferences.Editor editor = getSP().edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void setBoolData(String key, boolean value) {
        SharedPreferences.Editor editor = getSP().edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void setLongData(String key, long value) {
        SharedPreferences.Editor editor = getSP().edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static void remove(String key) {
        SharedPreferences.Editor editor = getSP().edit();
        editor.remove(key);
        editor.apply();
    }

    public void putString(String key, String value) {
        getSP().edit().putString(key, value).apply();
    }


}
