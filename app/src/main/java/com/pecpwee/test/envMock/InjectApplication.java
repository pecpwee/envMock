package com.pecpwee.test.envMock;

import android.app.Application;
import android.content.Context;

import com.pecpwee.lib.envmock.EnvMockInstaller;
import com.pecpwee.lib.envmock.PlayConfig;
import com.pecpwee.lib.envmock.RecordConfig;

/**
 * Created by pw on 2017/6/2.
 */

public class InjectApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        PlayConfig playConfig = new PlayConfig.Builder(this)
                .setAutoPlayMode(true)
                .setAutoStopMode(true)
                .setBeginOffsetPercent(0.3f)
                .setDebugEnable(true)
//                .setConnHookEnable(false)
                .build();
        EnvMockInstaller.installPlayService(playConfig);
        RecordConfig recordConfig = new RecordConfig.Builder(this)
                .setSampleInterval(5000L)
                .build();
        EnvMockInstaller.installRecordService(recordConfig);
    }
}
