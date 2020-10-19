package com.slh.flutter_hw_live.pull.weplayer;

public class PlayDate {

    private static String fps;

    private static String spd;

    private static String res;

    private static boolean manualDownload;

    public static void setFps(String fps) {
        PlayDate.fps = fps;
    }

    public static void setSpd(String spd) {
        PlayDate.spd = spd;
    }

    public static void setRes(String res) {
        PlayDate.res = res;
    }

    public static void setManualDownload(boolean manualDownload) {
        PlayDate.manualDownload = manualDownload;
    }

    public static String getFps() {
        return fps;
    }

    public static String getSpd() {
        return spd;
    }

    public static String getRes() {
        return res;
    }

    public static boolean getManualDownload() {
        return manualDownload;
    }


}