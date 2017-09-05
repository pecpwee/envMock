package com.pecpwee.lib.envMock.middle.connect;

import android.content.Context;
import android.net.Network;
import android.text.TextUtils;

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


    /*
    * 46    Network getActiveNetwork();
47    Network getActiveNetworkForUid(int uid, boolean ignoreBlocked);
48    NetworkInfo getActiveNetworkInfo();
49    NetworkInfo getActiveNetworkInfoForUid(int uid, boolean ignoreBlocked);

50    NetworkInfo getNetworkInfo(int networkType);
51    NetworkInfo getNetworkInfoForUid(in Network network, int uid, boolean ignoreBlocked);
52    NetworkInfo[] getAllNetworkInfo();
53    Network getNetworkForType(int networkType);
54    Network[] getAllNetworks();
55    NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int userId);
 NetworkCapabilities getNetworkCapabilities(in Network network)
    *
    * */
    @Override
    public Object onInvoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (TextUtils.isEmpty(methodName)) {
            throw new RuntimeException("method name is null");
        }
        if ("getActiveNetwork".equals(methodName)) {
            return middleConnectivityManager.getActiveNetwork();
        } else if ("getActiveNetworkForUid".equals(methodName)) {
            return middleConnectivityManager.getActiveNetwork();
        } else if ("getActiveNetworkInfo".equals(methodName)) {
            return middleConnectivityManager.getActiveNetworkInfo();
        } else if ("getActiveNetworkInfoForUid".equals(methodName)) {
            return middleConnectivityManager.getActiveNetworkInfo();
        } else if ("getNetworkInfo".equals(methodName)) {
            if (args != null && args.length == 1) {
                return middleConnectivityManager.getNetworkInfo((Integer) args[0]);
            }
        } else if ("getNetworkInfoForUid".equals(methodName)) {
            return middleConnectivityManager.getNetworkInfo((Network) args[0]);
        } else if ("getAllNetworkInfo".equals(methodName)) {
            return middleConnectivityManager.getAllNetworkInfo();
        } else if ("getAllNetworks".equals(methodName)) {
            return middleConnectivityManager.getAllNetworks();
        } else if ("getDefaultNetworkCapabilitiesForUser".equals(methodName)) {
            return middleConnectivityManager.getAllNetworkCapacities();
        } else if ("getNetworkCapabilities".equals(methodName)) {
            return middleConnectivityManager.getNetworkCapabilities((Network) args[0]);
        }

        return method.invoke(interfaceBinder, args);

    }
}