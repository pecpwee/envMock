package com.pecpwee.lib.envMock.recorder;

import android.text.TextUtils;

import com.pecpwee.lib.envMock.PlayConfig;
import com.pecpwee.lib.envMock.model.AbsTimeModel;
import com.pecpwee.lib.envMock.model.CONST;
import com.pecpwee.lib.envMock.utils.GsonFactory;
import com.pecpwee.lib.envMock.utils.ThreadManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by pw on 2017/6/12.
 */

public abstract class AbsRecorder {
    protected File mFile;
    private StringBuilder sb = new StringBuilder();
    private CountDownLatch latchlock = null;

    public final void start() {
        if (latchlock != null) {
            try {
                latchlock.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        sb.setLength(0);
        doRecord(CONST.BASE_START_RECORD_TIME, String.valueOf(System.currentTimeMillis()));
        onStart();
    }

    public final void stop() {
        onStop();
        latchlock = new CountDownLatch(1);
        storeRecord();
        doRecord(CONST.BASE_STOP_RECORD_TIME, String.valueOf(System.currentTimeMillis()));
    }

    public abstract void onStart();

    public abstract void onStop();

    public synchronized void setFilePath(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException();
        }
        this.mFile = new File(filePath);
    }

    private void storeRecord() {

        ThreadManager.RECORD_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                writeSDCardFile(mFile, sb.toString(), true);
                latchlock.countDown();

            }
        });

    }

    public abstract String getDefaultFilePath();

    protected synchronized void doRecord(int type, String objStr) {

        if (mFile == null) {
            File dir = new File(PlayConfig.DEFAULT_RECORD_DIR_PATH);
            dir.mkdirs();
            mFile = new File(getDefaultFilePath());
        }

        sb.append(type
                + "\n"
                + objStr
                + "\n");
    }


    protected void doRecord(AbsTimeModel obj) {

        doRecord(obj.getType(), GsonFactory.getGson().toJson(obj));
    }

    public static void writeSDCardFile(File file, String content, boolean append) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file, append));
            bos.write(content.getBytes("UTF-8"));
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}