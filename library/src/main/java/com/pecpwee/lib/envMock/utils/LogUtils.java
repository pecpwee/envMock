package com.pecpwee.lib.envMock.utils;

import android.util.Log;

/**
 * Created by pw on 14-12-19.
 */
public class LogUtils {
    private static final String TAG = "envMock";
    private static boolean isLogEnable = true;


    public static void d(String message) {
        if (isLogEnable == false) {
            return;
        }
        Log.d(TAG, message);
    }

    public static void log(Throwable t) {
        if (isLogEnable == false) {
            return;
        }

        if (t == null) {
            return;
        }
        Log.d(TAG, "exception", t);
    }

}
