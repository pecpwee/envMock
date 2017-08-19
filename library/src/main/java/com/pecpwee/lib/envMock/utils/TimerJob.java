package com.pecpwee.lib.envMock.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by pw on 2017/6/12.
 */

public class TimerJob {

    private static final int HANDLER_DO_PLAY = 1;
    private Runnable mRunnable;
    private long mInterval;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_DO_PLAY:
                    mRunnable.run();
                    scheduleNextWork();
                    break;
            }
        }
    };//used for delay time

    public TimerJob setRunnable(Runnable r) {
        this.mRunnable = r;
        return this;
    }

    public TimerJob setInterval(long timeMills) {
        this.mInterval = timeMills;
        return this;
    }

    public void stop() {
        handler.removeMessages(HANDLER_DO_PLAY);
    }


    public void start() {
        handler.sendEmptyMessageDelayed(HANDLER_DO_PLAY, 0);
    }

    private void scheduleNextWork() {
        handler.sendEmptyMessageDelayed(HANDLER_DO_PLAY, mInterval);
    }
}
