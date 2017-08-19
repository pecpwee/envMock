package com.pecpwee.lib.envMock.hook;

import android.content.Context;
import android.text.TextUtils;

import com.pecpwee.lib.envMock.player.AbsPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pw on 2017/6/8.
 */

public class CenterServiceManager {

    private Map<String, AbsServiceFetcher> mInstalledServiceMap;
    private static CenterServiceManager instance;
    private static boolean hasInit;
    private static Context context;
    private String packageName;

    public static void init(Context context) {
        CenterServiceManager.context = context;
        instance = new CenterServiceManager();
        hasInit = true;
    }

    public static boolean isInited() {
        return hasInit;
    }

    public static CenterServiceManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("you should invoke installPlayService method first");
        }
        return instance;
    }


    private CenterServiceManager() {
        this.mInstalledServiceMap = new HashMap<>();
        packageName = context.getPackageName();
    }

    public String getPackageName() {
        return packageName;
    }

    public void addService(String serviceName, AbsServiceFetcher factory) {
        if (TextUtils.isEmpty(serviceName)) {
            return;
        }
        if (factory == null) {
            return;
        }
        mInstalledServiceMap.put(serviceName, factory);
    }

    public Collection<AbsServiceFetcher> getInstalledServiceCollection() {
        return mInstalledServiceMap.values();
    }

    public AbsServiceFetcher getServiceFetcher(String serviceName) {
        return mInstalledServiceMap.get(serviceName);
    }

    public AbsPlayer getPlayer(String serviceName) {
        return getServiceFetcher(serviceName).getPlayer();
    }

}
