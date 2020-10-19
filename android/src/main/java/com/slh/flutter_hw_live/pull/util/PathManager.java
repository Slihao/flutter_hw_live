package com.slh.flutter_hw_live.pull.util;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author LJ
 * @date 2019-4-2
 */
public class PathManager {

    private static final String TAG = PathManager.class.getSimpleName();
    /**
     * 文件路径管理中的基础路径
     */
    private static String APP_ROOT_PATH = "";

    private static String APP_DIR_NAME = "CloudVideo";

    private static String INTERNAL_DIR = "";

    private static String EXTERNAL_DIR = "";

    static {
        refreshRootPath();
    }


    private static void refreshRootPath() {
        INTERNAL_DIR = UtilBase.context().getFilesDir().getPath();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            EXTERNAL_DIR = Environment.getExternalStorageDirectory().getPath();
            APP_ROOT_PATH = EXTERNAL_DIR + File.separator + APP_DIR_NAME + File.separator;
        } else {
            APP_ROOT_PATH = INTERNAL_DIR + File.separator + APP_DIR_NAME + File.separator;
        }
    }

    /**
     * 获取sdcard路径
     * as:/mnt/sdcard/
     *
     * @return String
     */
    public static String getAppRootPath() {
        String absolutePath = APP_ROOT_PATH;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    /**
     * 获取日志存储路径
     * as:/mnt/sdcard/OTT/LOG/
     *
     * @return String
     */
    public static String getLogPath() {
        String absolutePath = APP_ROOT_PATH + "LOG" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    /**
     * file zip save path
     * as:/mnt/sdcard/OTT/Mail/
     *
     * @return String
     */
    public static String getMailZipPath() {
        String absolutePath = APP_ROOT_PATH + "Mail" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    public static String getVideoPath() {
        String absolutePath = APP_ROOT_PATH + "Video" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    public static String getGIFPath() {
        String absolutePath = APP_ROOT_PATH + "GIF" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    public static String getUserPicPath() {
        String absolutePath = APP_ROOT_PATH + "userPic" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    public static String getIconCachePath() {
        String absolutePath = APP_ROOT_PATH + "iconCache" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    public static String getPicturesPath() {
        if (TextUtils.isEmpty(EXTERNAL_DIR)) {
            return null;
        }
        String absolutePath = EXTERNAL_DIR + File.separator + "Pictures" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    /**
     * auto create the file's path
     */
    private static void autoCreatePath(String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        boolean result = file.mkdir();
        LogUtils.d("mkdir " + path + " " + result);
    }

    /**
     * 获取日志存储路径
     * as:/mnt/sdcard/OTT/LOG/Player/
     *
     * @return String
     */
    public static String getPlayerLogPath() {
        String absolutePath = APP_ROOT_PATH + "LOG" + File.separator + "Player" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    /**
     * 获取日志存储路径
     * as:/mnt/sdcard/OTT/LOG/
     *
     * @return String
     */
    public static String getAppLogPath() {
        String absolutePath = APP_ROOT_PATH + "LOG" + File.separator + "App" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    /**
     * 获取CrashLog
     * as:/mnt/sdcard/OTT/LOG/CrashLog/
     *
     * @return
     */
    public static String getCrashLog() {
        String absolutePath = APP_ROOT_PATH + "LOG" + File.separator + "CrashLog" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    /**
     * 获取CrashLog
     * as:/mnt/sdcard/OTT/LOG/CrashLog/yyyyMMdd
     */
    public static String getCrashLogPath() {
        getCrashLog();//创建 as:/mnt/sdcard/OTT/LOG/Errorlog/

        String absolutePath = APP_ROOT_PATH
                + "LOG" + File.separator
                + "CrashLog" + File.separator
                + new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date()) + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }

    /**
     * 获取日志存储路径
     * as:/mnt/sdcard/OTT/LOG/Errorlog/yyyyMMdd
     *
     * @return String
     */
    public static String getErrorLogPath() {
        getErrorLog();//创建 as:/mnt/sdcard/OTT/LOG/Errorlog/

        String absolutePath = APP_ROOT_PATH
                + "LOG" + File.separator
                + "ErrorLog" + File.separator
                + new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date()) + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }


    /**
     * 获取日志存储路径
     * as:/mnt/sdcard/OTT/LOG/Errorlog/
     *
     * @return String
     */
    public static String getErrorLog() {
        String absolutePath = APP_ROOT_PATH
                + "LOG" + File.separator
                + "ErrorLog" + File.separator;
        autoCreatePath(absolutePath);
        return absolutePath;
    }
}
