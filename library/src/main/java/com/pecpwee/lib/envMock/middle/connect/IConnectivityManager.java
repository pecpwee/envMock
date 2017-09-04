package com.pecpwee.lib.envMock.middle.connect;

import android.content.Context;

import com.pecpwee.lib.envMock.hook.AbsIManager;
import com.pecpwee.lib.envMock.hook.CenterServiceManager;

import java.lang.reflect.Method;

/**
 * Created by pw on 2017/9/4.
 * http://androidxref.com/7.1.1_r6/xref/frameworks/base/core/java/android/net/IConnectivityManager.aidl
 */

public class IConnectivityManager extends AbsIManager {

    private MiddleConnectivityManager middleConnectivityManager;
    private boolean isWifiConnect;

    public IConnectivityManager(Object interfaceBinder) {
        super(interfaceBinder);
        this.middleConnectivityManager = (MiddleConnectivityManager) CenterServiceManager
                .getInstance()
                .getServiceFetcher(Context.CONNECTIVITY_SERVICE)
                .getMiddleManagerService();

    }



    @Override
    public Object onInvoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        if ("getActiveNetworkInfo".equals(methodName)) {
            return middleConnectivityManager.getActiveNetworkInfo();

        } else if ("getAllNetworks".equals(methodName)) {
            return middleConnectivityManager.getAllNetworks();
        }
        return method.invoke(interfaceBinder, args);
    }
}
