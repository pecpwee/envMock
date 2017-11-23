package com.pecpwee.lib.envMock;

import android.os.Environment;

/**
 * Created by pw on 2017/8/19.
 */

public class AbsConfig {


    public static final String EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DEFAULT_RECORD_DIR_PATH = EXTERNAL_STORAGE_PATH + "/" + "envMock";

    public static final String DEFAULT_FILEPATH_GPS = DEFAULT_RECORD_DIR_PATH
            + "/" + "GpsRecord.log";
    public static final String DEFAULT_FILEPATH_WIFI = DEFAULT_RECORD_DIR_PATH
            + "/" + "WifiRecord.log";
    public static final String DEFAULT_FILEPATH_CELL = DEFAULT_RECORD_DIR_PATH
            + "/" + "TelephonyRecord.log";
    public static final String DEFAULT_FILEPATH_CONN = DEFAULT_RECORD_DIR_PATH
            + "/" + "ConnRecord.log";

    public boolean isDebug = false;
}