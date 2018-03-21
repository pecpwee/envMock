package com.pecpwee.lib.envmock;

import android.content.Context;
import android.os.Looper;

/**
 * Created by pw on 2017/8/19.
 */

public class RecordConfig extends AbsConfig {
    private static RecordConfig CONFIG_INSTANCE;
    private Context context;
    private Looper looper;
    private long SAMPLE_INTERVAL = 5 * 1000L;
    private int WIFI_SCAN_RECORD_COUNT_UPPER_LIMIT = 10;

    public static RecordConfig getInstance() {
        return CONFIG_INSTANCE;
    }

    public static void setConfigInstance(RecordConfig configInstance) {
        CONFIG_INSTANCE = configInstance;
    }

    public long getSampleInterval() {
        return SAMPLE_INTERVAL;
    }

    public void setSampleInterval(long SAMPLE_INTERVAL) {
        this.SAMPLE_INTERVAL = SAMPLE_INTERVAL;
    }

    public int getWifiScanCountUpperLimit() {
        return WIFI_SCAN_RECORD_COUNT_UPPER_LIMIT;
    }

    public void setWifiScanUpperCount(int WIFI_SCAN_RECORD_COUNT_UPPER_LIMIT) {
        this.WIFI_SCAN_RECORD_COUNT_UPPER_LIMIT = WIFI_SCAN_RECORD_COUNT_UPPER_LIMIT;
    }

    public Looper getLooper() {
        return looper;
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public static class Builder {
        private RecordConfig config;

        public Builder(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("parameters should not be null");
            }
            config = new RecordConfig();
            config.context = context;
            config.looper = context.getMainLooper();
        }

        public Builder setWorkingLooper(Looper looper) {
            config.looper = looper;
            return this;
        }

        public Builder setSampleInterval(long interval) {
            config.SAMPLE_INTERVAL = interval;
            return this;
        }

        public Builder setDebugEnable(boolean isDebug) {
            config.isDebug = isDebug;
            return this;
        }

        public RecordConfig build() {

            return config;
        }
    }

}
