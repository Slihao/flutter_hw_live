package com.slh.flutter_hw_live.pull.util;

public class Data {
    //判断点播还是直播
    private static boolean type = false;

    //判断是否要更新UUID
    private static boolean uuidShouldChange = false;

    public static boolean getType() {
        return type;
    }

    public static void setType(boolean type) {
        Data.type = type;
    }

    public static void setUuidShouldChange(boolean uuidShouldChange) {
        Data.uuidShouldChange = uuidShouldChange;
    }

    public static boolean getUuidShouldChange() {
        return uuidShouldChange;
    }
}