package com.example.uiwrapper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.pecpwee.lib.envmock.EnvMockInstaller;
import com.pecpwee.lib.envmock.PlayConfig;
import com.pecpwee.lib.envmock.utils.LogUtils;

/**
 * Created by pw on 2018/3/22.
 */

public class EnvMockUI {
    private static final String TAG = "EnvMockUI ";
    private static StateManager sController;

    private static void initCoreLib(Context context) {
        PlayConfig playConfig = new PlayConfig.Builder(context)
                .setAutoPlayMode(false)
                .setAutoStopMode(false)
                .setDebugEnable(false)
//                .setConnHookEnable(false)
                .build();
        EnvMockInstaller.installPlayService(playConfig);
    }

    public static void install(Application application, Context context) {
        initCoreLib(context);

        sController = new StateManager();
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtils.d(TAG + "onActivityStarted " + activity.getClass().getSimpleName());


            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtils.d(TAG + "onActivityResumed " + activity.getClass().getSimpleName());
                sController.addFloatingWindow(activity);

            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtils.d(TAG + "onActivityPaused " + activity.getClass().getSimpleName());
                sController.removeFloatingWindow(activity);
            }

            @Override
            public void onActivityStopped(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });

    }


}
