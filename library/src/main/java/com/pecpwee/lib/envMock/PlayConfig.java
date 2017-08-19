package com.pecpwee.lib.envMock;

import android.content.Context;
import android.os.Looper;

import java.util.HashMap;

/**
 * Created by pw on 2017/8/17.
 */

public class PlayConfig extends AbsConfig {
    private static PlayConfig CONFIG_INSTANCE;
    private Context context;
    private Looper looper;
    private HashMap<String, Boolean> mModuleStateMap;


    private boolean isAutoPlayMode = true;
    private boolean isAutoStopMode = true;

    private String GpsRecordFilePath = null;
    private String WifiRecordFilePath = null;
    private String CellRecordFilePath = null;

    private float mStartPlayPercentage = 0;

    public static PlayConfig getInstance() {
        return CONFIG_INSTANCE;
    }

    public boolean isAutoMode() {
        return isAutoPlayMode || isAutoStopMode;
    }

    public static void setInstance(PlayConfig config) {
        CONFIG_INSTANCE = config;
    }

    PlayConfig() {
        mModuleStateMap = new HashMap<>();
        mModuleStateMap.put(Context.WIFI_SERVICE, true);
        mModuleStateMap.put(Context.LOCATION_SERVICE, true);
        mModuleStateMap.put(Context.TELEPHONY_SERVICE, true);
    }

    public Context getContext() {
        return context;
    }

    public Looper getLooper() {
        return looper;
    }

    public HashMap<String, Boolean> getModuleStateMap() {
        return mModuleStateMap;
    }

    public boolean isAutoPlayMode() {
        return isAutoPlayMode;
    }

    public boolean isAutoStopMode() {
        return isAutoStopMode;
    }

    public String getGpsRecordFilePath() {
        return GpsRecordFilePath;
    }

    public void setGpsRecordFilePath(String gpsRecordFilePath) {
        ensureConfigCanChange();
        this.GpsRecordFilePath = gpsRecordFilePath;
    }

    public String getWifiRecordFilePath() {
        return WifiRecordFilePath;
    }

    public void setWifiRecordFilePath(String wifiRecordFilePath) {
        ensureConfigCanChange();
        this.WifiRecordFilePath = wifiRecordFilePath;
    }

    public String getCellRecordFilePath() {
        return CellRecordFilePath;
    }

    public void setCellRecordFilePath(String cellRecordFilePath) {
        ensureConfigCanChange();
        this.CellRecordFilePath = cellRecordFilePath;
    }

    public float getStartPlayPercentage() {
        return mStartPlayPercentage;
    }

    public void setStartPlayPercentage(float mStartPlayPercentage) {
        ensureConfigCanChange();
        this.mStartPlayPercentage = mStartPlayPercentage;
    }

    private void ensureConfigCanChange() {
        if (this.isAutoPlayMode || this.isAutoStopMode) {
            throw new RuntimeException("the config cannot be change");
        }
    }

    public static class Builder {
        private PlayConfig config;


        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("parameters should not be null");
            }
            config = new PlayConfig();
            config.context = context;
            config.looper = context.getMainLooper();
        }

        public Builder setWorkingLooper(Looper looper) {
            config.looper = looper;
            return this;
        }


        public Builder setAutoPlayMode(boolean isEnbale) {
            config.isAutoPlayMode = isEnbale;
            return this;
        }

        public Builder setAutoStopMode(boolean isEnbale) {
            config.isAutoStopMode = isEnbale;
            return this;
        }

        public Builder setBeginOffsetPercent(float percentOffset) {
            config.mStartPlayPercentage = percentOffset;
            return this;
        }

        public Builder setGpsMockFile(String filePath) {
            config.GpsRecordFilePath = filePath;
            return this;
        }

        public Builder setWifiMockFilePath(String filePath) {
            config.WifiRecordFilePath = filePath;
            return this;
        }

        public Builder setCellMockFilePath(String filePath) {
            config.CellRecordFilePath = filePath;
            return this;
        }

        public PlayConfig build() {
            return config;
        }

        //between 0 to 1.

    }

}
