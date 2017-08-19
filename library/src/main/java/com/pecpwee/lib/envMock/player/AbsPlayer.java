package com.pecpwee.lib.envMock.player;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.pecpwee.lib.envMock.PlayConfig;
import com.pecpwee.lib.envMock.model.CONST;
import com.pecpwee.lib.envMock.utils.LogUtils;
import com.pecpwee.lib.envMock.utils.ThreadManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by pw on 2017/6/12.
 */

public abstract class AbsPlayer<T> {
    private static final String TAG = "AbsPlayer ";


    protected T mListener;
    private static final int HANDLER_DO_PLAY = 1;

    private ArrayList<TimedRunnable> mTimedActionsList;
    private int mCurrentTimedActionIndex = 0;
    private volatile boolean isNeedNextAction = false;
    private float mStartPlayOffset = 0;
    private long mCurrentStartTime;
    private long mFileRecordStartTime;
    private long mFileRecordStopTime;
    private long mAcutalStartPastTimestamp;
    private CountDownLatch latchLock = null;

    private boolean isNeedReloadData = true;

    private Handler workHandler = new Handler(ThreadManager.PLAY_LOOPER);

    public final void setListener(T listener) {
        this.mListener = listener;
    }

    private Handler mainHandler;//used for delay time

    public AbsPlayer(T listener) {
        mainHandler = new Handler(ThreadManager.MAIN_LOOPER) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case HANDLER_DO_PLAY:
                        doAction();
                        scheduleAction(mCurrentTimedActionIndex + 1);
                        mCurrentTimedActionIndex++;
                        break;
                }
            }
        };
        mTimedActionsList = new ArrayList<>();
        this.mListener = listener;
    }

    protected void addTimedAction(TimedRunnable timeRunnable) {
        mTimedActionsList.add(timeRunnable);
    }

    public final synchronized void startPlay(long startBaseTime) {

        isNeedNextAction = true;
        this.mCurrentStartTime = startBaseTime;
        this.mStartPlayOffset = PlayConfig.getInstance().getStartPlayPercentage();
        this.latchLock = new CountDownLatch(1);
        workHandler.post(new Runnable() {
            @Override
            public void run() {
                loadDataIfNeed();
                dealWithPlayOffset();
                scheduleAction(mCurrentTimedActionIndex);
                latchLock.countDown();
            }
        });
        LogUtils.d(getClass().getSimpleName() + "doStartPlay");

    }

    public abstract String getDefaultFilePath();

    public abstract String getConfigFilePath();

    private synchronized void doAction() {
        LogUtils.d(TAG + getDefaultFilePath() + "doAction");

        if (mCurrentTimedActionIndex >= mTimedActionsList.size()) {
            stopPlay();
            return;
        }
        Runnable action = mTimedActionsList.get(mCurrentTimedActionIndex);
        mainHandler.post(action);

    }

    private void scheduleAction(int actionIndex) {
        if (!isNeedNextAction) {
            return;
        }
        if (actionIndex >= mTimedActionsList.size()) {
            return;
        }


        long nextOnePastTime = mTimedActionsList.get(actionIndex).getTime();
        long intervalTime = ((nextOnePastTime - mAcutalStartPastTimestamp) + mCurrentStartTime) - System.currentTimeMillis();
        intervalTime = intervalTime < 0 ? 0 : intervalTime;
        LogUtils.d(TAG + getDefaultFilePath() + " intervalTime " + intervalTime);
        mainHandler.sendEmptyMessageDelayed(HANDLER_DO_PLAY, intervalTime);
    }

    protected abstract ILineDataParser getParser();


    public synchronized void loadDataIfNeed() {
        if (!isNeedReloadData) {
            return;
        }
        mTimedActionsList.clear();
        String configFilePath = getConfigFilePath();
        if (!TextUtils.isEmpty(configFilePath)) {
            doLoadData(configFilePath);
        } else {
            doLoadData(getDefaultFilePath());
        }
        isNeedReloadData = false;
    }



    private void dealWithPlayOffset() {
        long recordTimeInterval = mFileRecordStopTime - mFileRecordStartTime;
        LogUtils.d("mFileRecordStartTime" + mFileRecordStartTime);
        LogUtils.d("mFileRecordStopTime" + mFileRecordStopTime);
        LogUtils.d("recordTimeInterval" + recordTimeInterval);
        long startOffsetTime = (long) (recordTimeInterval * mStartPlayOffset);
        LogUtils.d("startOffsetTime" + startOffsetTime);

        for (int i = 0; i < mTimedActionsList.size(); i++) {
            if (mTimedActionsList.get(i).getTime() - mFileRecordStartTime > startOffsetTime) {
                mCurrentTimedActionIndex = i;
                LogUtils.d("mCurrentTimedActionIndex" + mCurrentTimedActionIndex);
                mAcutalStartPastTimestamp = mTimedActionsList.get(i).getTime();
                LogUtils.d("mAcutalStartPastTimestamp" + mAcutalStartPastTimestamp);
                break;
            }
        }

    }

    private synchronized void doLoadData(String path) {
        doLoadData(new File(path));
    }

    private synchronized void doLoadData(File file) {
        ILineDataParser parser = getParser();
        FileInputStream fis = null;
        if (!file.exists()) {
            throw new RuntimeException("cannot find mock data file");
        }
        mTimedActionsList.clear();
        LogUtils.d(getClass().getSimpleName() + "loading data from" + file.getAbsolutePath());
        try {
            int linecount = 0;

            fis = new FileInputStream(file);
            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;
            while ((line = br.readLine()) != null) {
                parser.onNewLineGot(line);
                linecount++;
            }

            br.close();
            LogUtils.d(getClass().getSimpleName() + "parsed the count of the line is:" + linecount);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void notifyDataSourceChanged() {
        isNeedReloadData = true;
    }

    public final synchronized void resetPlayProgress() {
        mCurrentTimedActionIndex = 0;
    }

    public final synchronized void stopPlay() {
        if (latchLock != null) {
            try {
                latchLock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isNeedNextAction = false;
        mainHandler.removeMessages(HANDLER_DO_PLAY);
        LogUtils.d(getClass().getSimpleName() + "doStopPlay");
    }


    public interface ILineDataParser {
        void onNewLineGot(String line);
    }

    public static abstract class TimedRunnable implements Runnable {
        long time = 0l;

        public TimedRunnable(long time) {
            this.time = time;
        }

        @Override
        public abstract void run();

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    /**
     * Created by pw on 2017/6/12.
     */

    public abstract class AbsParser implements ILineDataParser {
        private int type;
        private boolean isNeedParsingType = true;

        public void onNewLineGot(String line) {
            if (isNeedParsingType) {
                type = parseType(line);
                isNeedParsingType = false;
            } else {
                if (type == CONST.BASE_START_RECORD_TIME) {
                    long time = 0;
                    time = Long.parseLong(line);
                    AbsPlayer.this.mFileRecordStartTime = time;
                    AbsPlayer.this.mAcutalStartPastTimestamp = time;

                } else if (type == CONST.BASE_STOP_RECORD_TIME) {
                    long time = 0;
                    time = Long.parseLong(line);
                    AbsPlayer.this.mFileRecordStopTime = time;
                } else {
                    parseValue(type, line);
                }
                isNeedParsingType = true;
            }
        }


        protected int parseType(String line) {
            return Integer.valueOf(line);
        }

        protected abstract void parseValue(int type, String line);
    }
}
