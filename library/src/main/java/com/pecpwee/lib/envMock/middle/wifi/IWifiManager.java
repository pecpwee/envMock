package com.pecpwee.lib.envMock.middle.wifi;

import android.content.Context;

import com.pecpwee.lib.envMock.hook.AbsIManager;
import com.pecpwee.lib.envMock.hook.CenterServiceManager;

import java.lang.reflect.Method;

/**
 * Created by pw on 2017/6/8.
 */

public class IWifiManager extends AbsIManager {
    private MiddleWifiManagerService middleWifiManager;

    public IWifiManager(Object interfaceBinder, String serviceName) {
        super(interfaceBinder, serviceName);

        this.middleWifiManager = (MiddleWifiManagerService) CenterServiceManager
                .getInstance()
                .getServiceFetcher(Context.WIFI_SERVICE)
                .getMiddleManagerService();
    }


    @Override
    public InvokeReturnObj onInvoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        //List<ScanResult> getScanResults(String callingPackage);
        if ("getScanResults".equals(methodName)) {
            if (args.length == 1) {
                return new InvokeReturnObj(true, middleWifiManager.getScanResults((String) args[0]));
            }
        } else if ("getConnectionInfo".equals(methodName)) {
            return new InvokeReturnObj(true, middleWifiManager.getConnectionInfo());
        } else if ("startScan".equals(methodName)) {
            middleWifiManager.startScan(args[0], args[1]);
            return new InvokeReturnObj(true, null);
            //只是作为激活播放。实际还是需要invoke系统
        }

        return new InvokeReturnObj(false, null);
    }


}
