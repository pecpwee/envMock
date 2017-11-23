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
    private HashMap<String, ModuleStateWrapper> mModuleStateMap;


    private boolean isAutoPlayMode = true;
    private boolean isAutoStopMode = true;

    private String GpsRecordFilePath = null;
    private String WifiRecordFilePath = null;
    private String CellRecordFilePath = null;
    private String ConnRecordFilePath = null;

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
        mModuleStateMap.put(Context.WIFI_SERVICE, new ModuleStateWrapper());
        mModuleStateMap.put(Context.LOCATION_SERVICE, new ModuleStateWrapper());
        mModuleStateMap.put(Context.TELEPHONY_SERVICE, new ModuleStateWrapper());
        mModuleStateMap.put(Context.CONNECTIVITY_SERVICE, new ModuleStateWrapper());
    }

    public Context getContext() {
        return context;
    }

    public Looper getLooper() {
        return looper;
    }

    public HashMap<String, ModuleStateWrapper> getModuleStateMap() {
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

    public String getConnRecordFilePath() {
        return ConnRecordFilePath;
    }

    public String setConnRecordFilePath() {
        ensureConfigCanChange();
        return ConnRecordFilePath;
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

        public Builder setConnMockFilePath(String filePath) {
            config.ConnRecordFilePath = filePath;
            return this;
        }

        public Builder setWifiPlayerEnable(boolean isEnable) {
            config.mModuleStateMap.get(Context.WIFI_SERVICE).isPlayEnable = isEnable;
            return this;
        }

        public Builder setGpsPlayerEnable(boolean isEnable) {
            config.mModuleStateMap.get(Context.LOCATION_SERVICE).isPlayEnable = isEnable;
            return this;
        }


        public Builder setConnPlayerEnable(boolean isEnable) {
            config.mModuleStateMap.get(Context.CONNECTIVITY_SERVICE).isPlayEnable = isEnable;
            return this;

        }

        public Builder setCellPlayerEnable(boolean isEnable) {
            config.mModuleStateMap.get(Context.TELEPHONY_SERVICE).isPlayEnable = isEnable;
            return this;
        }


        public Builder setWifiHookEnable(boolean isEnable) {
            config.mModuleStateMap.get(Context.WIFI_SERVICE).isHookEnable = isEnable;
            return this;
        }

        public Builder setGpsHookEnable(boolean isEnable) {
            config.mModuleStateMap.get(Context.LOCATION_SERVICE).isHookEnable = isEnable;
            return this;
        }


        public Builder setConnHookEnable(boolean isEnable) {
            config.mModuleStateMap.get(Context.CONNECTIVITY_SERVICE).isHookEnable = isEnable;
            return this;

        }

        public Builder setCellHookEnable(boolean isEnable) {
            config.mModuleStateMap.get(Context.TELEPHONY_SERVICE).isHookEnable = isEnable;
            return this;
        }


        public Builder setDebugEnable(boolean isDebug) {
            config.isDebug = isDebug;
            return this;
        }

        public PlayConfig build() {
            return config;
        }
    }

    public static class ModuleStateWrapper {
        public boolean isPlayEnable = true;
        public boolean isHookEnable = true;
    }

}
