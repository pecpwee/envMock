package com.pecpwee.lib.envMock;

import android.content.Context;
import android.os.Build;
import android.os.Handler;

import com.pecpwee.lib.envMock.hook.AbsServiceFetcher;
import com.pecpwee.lib.envMock.hook.CenterServiceManager;
import com.pecpwee.lib.envMock.middle.connect.ConnectivityServiceFactory;
import com.pecpwee.lib.envMock.middle.location.LocationServiceFactory;
import com.pecpwee.lib.envMock.middle.telephony.TelephonyServiceFactory;
import com.pecpwee.lib.envMock.middle.wifi.WifiServiceFactory;
import com.pecpwee.lib.envMock.utils.ThreadManager;

import java.util.ArrayList;

/**
 * Created by pw on 2017/6/2.
 */

public class EnvMockInstaller {

    public static boolean isPlayServiceInstalled = false;
    public static boolean isRecordServiceInstalled = false;

    public static synchronized boolean installPlayService(PlayConfig config) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        PlayConfig.setInstance(config);
        ensureCenterServiceInstalled(config.getContext());
        ThreadManager.PLAY_LOOPER = config.getLooper();
        ThreadManager.PLAY_HANDLER = new Handler(ThreadManager.PLAY_LOOPER);
        isPlayServiceInstalled = true;
        return true;
    }

    public static synchronized boolean installRecordService(RecordConfig config) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        RecordConfig.setConfigInstance(config);
        ensureCenterServiceInstalled(config.getContext());
        ThreadManager.RECORD_LOOPER = config.getLooper();
        ThreadManager.RECORD_HANDLER = new Handler(config.getLooper());
        isRecordServiceInstalled = true;
        return true;
    }

    private static void ensureCenterServiceInstalled(Context context) {
        if (isPlayServiceInstalled || isRecordServiceInstalled) {
            return;
        }

        ThreadManager.MAIN_LOOPER = context.getMainLooper();
        ThreadManager.MAIN_HANDLER = new Handler(context.getMainLooper());

        CenterServiceManager.init(context);

        ArrayList<AbsServiceFetcher> serviceList = new ArrayList();
        serviceList.add(new WifiServiceFactory(context));

        serviceList.add(new LocationServiceFactory(context));

        serviceList.add(new TelephonyServiceFactory(context));

        serviceList.add(new ConnectivityServiceFactory(context));

        for (AbsServiceFetcher serviceFactory : serviceList) {
            serviceFactory.install();
        }
    }
}
