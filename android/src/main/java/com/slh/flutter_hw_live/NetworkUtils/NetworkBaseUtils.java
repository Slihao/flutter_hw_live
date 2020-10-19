package com.slh.flutter_hw_live.NetworkUtils;

import android.content.Context;

public final class NetworkBaseUtils {
    private static Context context;

    private NetworkBaseUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化工具类
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        if (NetworkBaseUtils.context == null) {
            NetworkBaseUtils.context = context.getApplicationContext();
        }
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (context != null) return context;
        throw new NullPointerException("u should init first");
    }
}
