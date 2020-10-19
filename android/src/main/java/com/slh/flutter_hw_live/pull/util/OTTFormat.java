package com.slh.flutter_hw_live.pull.util;

import android.text.TextUtils;

/**
 * 在此写用途
 *
 * @version V1.0 <描述当前版本功能>
 * @FileName: com.fmcc.lib.common.utils.uiutil.OTTFormat.java
 * @author: yh
 * @date: 2018-07-25 14:57
 */

public class OTTFormat {


    private static final String TAG = OTTFormat.class.getSimpleName();

    public OTTFormat() {
    }

    public static Long convertLong(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        } else {
            try {
                return Long.valueOf(Long.parseLong(value));
            } catch (NumberFormatException var2) {
                LogUtils.e("" + var2);
                return null;
            }
        }
    }

    public static long convertSimpleLong(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0L;
        } else {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException var2) {
                LogUtils.e("" + var2);
                return 0L;
            }
        }
    }

    public static double convertSimpleDouble(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0.0D;
        } else {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException var2) {
                LogUtils.e("" + var2);
                return 0.0D;
            }
        }
    }

    public static Integer convertInteger(String value) {
        if (TextUtils.isEmpty(value)) {
            return null;
        } else {
            try {
                return Integer.valueOf(Integer.parseInt(value));
            } catch (NumberFormatException var2) {
                LogUtils.e("" + var2);
                return null;
            }
        }
    }

    public static int convertInt(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException var2) {
                LogUtils.e("" + var2);
                return 0;
            }
        }
    }

    public static int convertIntDefault(String value, int defaultValue) {
        if (TextUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException var3) {
                LogUtils.e("" + var3);
                return defaultValue;
            }
        }
    }

    public static float convertFloatDefault(String value) {
        if (TextUtils.isEmpty(value)) {
            return 0.0F;
        } else {
            try {
                return Float.parseFloat(value);
            } catch (NumberFormatException var2) {
                LogUtils.e("" + var2);
                return 0.0F;
            }
        }
    }

}
