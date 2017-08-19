package com.pecpwee.lib.envMock.middle.telephony;

import android.content.Context;

import com.pecpwee.lib.envMock.hook.AbsIManager;
import com.pecpwee.lib.envMock.hook.CenterServiceManager;

import java.lang.reflect.Method;

/**
 * Created by pw on 2017/6/14.
 */

public class ITelephonyManager extends AbsIManager {

    MiddleTelephonyManagerService middleTelephonyManagerService;

    public ITelephonyManager(Object origBinderInterface) {
        super(origBinderInterface);

        this.middleTelephonyManagerService = (MiddleTelephonyManagerService) CenterServiceManager
                .getInstance()
                .getServiceFetcher(Context.TELEPHONY_SERVICE)
                .getMiddleManagerService();

    }

    @Override
    public Object onInvoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if ("getCellLocation".equals(methodName)) {
            return middleTelephonyManagerService.getCellLocation();
        } else if ("getNetworkType".equals(methodName)) {
            return middleTelephonyManagerService.getNetworkType();
        }
        return method.invoke(interfaceBinder, args);
    }
}
