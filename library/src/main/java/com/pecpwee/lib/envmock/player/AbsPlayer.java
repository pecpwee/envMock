package com.pecpwee.lib.envmock.player;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.pecpwee.lib.envmock.PlayConfig;
import com.pecpwee.lib.envmock.model.CONST;
import com.pecpwee.lib.envmock.utils.LogUtils;
import com.pecpwee.lib.envmock.utils.ThreadManager;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by pw on 2017/6/12.
 */

public abstract class AbsPlayer<T> {
    private static final String TAG = "AbsPlayer ";


    protected T mListener;
    private static final int HANDLER_DO_PLAY = 1;

    private int mCurrentTimedActionIndex = 0;
    private volatile boolean isNeedNextAction = false;
    private float mStartPlayOffset = 0;
    private long mCurrentStartTime;
    private long mFileRecordStartTime;
    private long mFileRecordStopTime;
    private long mAcutalStartPastTimestamp;


    private Handler workHandler = new Handler(ThreadManager.PLAY_LOOPER);

    public final void setListener(T listener) {
        this.mListener = listener;
    }

    private PlayerDataSource mDataSource;
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
        this.mListener = listener;
        mDataSource = new PlayerDataSource();
    }


    public float getCurrentPlayProgress() {
        return mCurrentTimedActionIndex / (float) mDataSource.getSize();
    }

    protected void addTimedAction(TimedRunnable timeRunnable) {
        mDataSource.addTimedAction(timeRunnable);
    }

    public final synchronized void startPlay(long startBaseTime) throws FileNotFoundException {

        isNeedNextAction = true;
        this.mCurrentStartTime = startBaseTime;
        this.mStartPlayOffset = PlayConfig.getInstance().getStartPlayPercentage();

        tryLoadData();
        dealWithPlayOffset();
        scheduleAction(mCurrentTimedActionIndex);

        LogUtils.d(getClass().getSimpleName() + "doStartPlay");

    }

    public boolean isSourceFileReady() {
        File file = getDataSourceFile();
        if (file != null && file.exists()) {
            return true;
        }
        return false;
    }


    public void tryLoadData() throws FileNotFoundException {
        File file = getDataSourceFile();

        mDataSource.loadDataIfNeed(file, getParser());
    }


    private File getDataSourceFile() {
        String path = getConfigFilePath();
        if (TextUtils.isEmpty(path)) {
            path = getDefaultFilePath();
        }
        File file = new File(path);
        return file;

    }


    public boolean isDataLoadedOK() {
        return mDataSource.isDataLoadedOk();
    }

    public abstract String getDefaultFilePath();

    public abstract String getConfigFilePath();

    private synchronized void doAction() {
        LogUtils.d(TAG + getDefaultFilePath() + "doAction");

        if (mCurrentTimedActionIndex >= mDataSource.getSize()) {
            stopPlay();
            return;
        }
        Runnable action = mDataSource.getAction(mCurrentTimedActionIndex);
        mainHandler.post(action);

    }

    private void scheduleAction(int actionIndex) {
        if (!isNeedNextAction) {
            return;
        }
        if (actionIndex >= mDataSource.getSize()) {
            return;
        }


        long nextOnePastTime = mDataSource.getAction(actionIndex).getTime();
        long intervalTime = ((nextOnePastTime - mAcutalStartPastTimestamp) + mCurrentStartTime) - System.currentTimeMillis();
        intervalTime = intervalTime < 0 ? 0 : intervalTime;
        LogUtils.d(TAG + getDefaultFilePath() + " intervalTime " + intervalTime);
        mainHandler.sendEmptyMessageDelayed(HANDLER_DO_PLAY, intervalTime);
    }

    protected abstract ILineDataParser getParser();


    private void dealWithPlayOffset() {
        long recordTimeInterval = mFileRecordStopTime - mFileRecordStartTime;
        LogUtils.d("mFileRecordStartTime" + mFileRecordStartTime);
        LogUtils.d("mFileRecordStopTime" + mFileRecordStopTime);
        LogUtils.d("recordTimeInterval" + recordTimeInterval);
        long startOffsetTime = (long) (recordTimeInterval * mStartPlayOffset);
        LogUtils.d("startOffsetTime" + startOffsetTime);

        for (int i = 0; i < mDataSource.getSize(); i++) {
            if (mDataSource.getAction(i).getTime() - mFileRecordStartTime > startOffsetTime) {
                mCurrentTimedActionIndex = i;
                LogUtils.d("mCurrentTimedActionIndex" + mCurrentTimedActionIndex);
                mAcutalStartPastTimestamp = mDataSource.getAction(i).getTime();
                LogUtils.d("mAcutalStartPastTimestamp" + mAcutalStartPastTimestamp);
                break;
            }
        }

    }


    public void notifyDataSourceChanged() {
        mDataSource.setNeedReloadData();
    }

    public final synchronized void resetPlayProgress() {
        mCurrentTimedActionIndex = 0;
    }

    public final synchronized void stopPlay() {
        isNeedNextAction = false;
        mainHandler.removeMessages(HANDLER_DO_PLAY);
        LogUtils.d(getClass().getSimpleName() + "doStopPlay");
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
