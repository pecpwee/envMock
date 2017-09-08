package com.pecpwee.lib.envMock.middle.location;

import android.app.PendingIntent;
import android.content.Context;

import com.pecpwee.lib.envMock.hook.AbsIManager;
import com.pecpwee.lib.envMock.hook.CenterServiceManager;
import com.pecpwee.lib.envMock.utils.LogUtils;

import java.lang.reflect.Method;

/**
 * Created by pw on 2017/6/2.
 */

public class ILocationManager extends AbsIManager {

    AbsMiddleLocationManagerService middleLocationManagerService;

    public ILocationManager(Object origBinderInterface, String serviceName) {
        super(origBinderInterface, serviceName);
        this.middleLocationManagerService = (AbsMiddleLocationManagerService) CenterServiceManager
                .getInstance()
                .getServiceFetcher(Context.LOCATION_SERVICE)
                .getMiddleManagerService();
    }

    //AIDL ADDRESS:
    //https://android.googlesource.com/platform/frameworks/base/+/master/location/java/android/location/ILocationManager.aidl
    @Override
    public InvokeReturnObj onInvoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        //Android L and Android M
        if ("requestLocationUpdates".equals(methodName)) {

            if (args.length == 4) {
                middleLocationManagerService.requestLocationUpdates(args[0], args[1], (PendingIntent) args[2], (String) args[3]);
                return new InvokeReturnObj(true, null);
            }
        } else if ("removeUpdates".equals(methodName)) {
            LogUtils.d("removeUpdates");
            if (args.length == 3) {
                middleLocationManagerService.removeUpdates(args[0], (PendingIntent) args[1], (String) args[2]);
                return new InvokeReturnObj(true, null);
            }
        } else if ("addGpsStatusListener".equals(methodName)) {
            if (args.length == 2) {
                boolean b = middleLocationManagerService.addGpsStatusListener(args[0], (String) args[1]);
                return new InvokeReturnObj(true, b);

            }
        } else if ("removeGpsStatusListener".equals(methodName)) {
            if (args.length == 2) {
                middleLocationManagerService.removeGpsStatusListener(args[0]);
                return new InvokeReturnObj(true, null);
            }
        }

        //7.0以上 addNmeaListener等都使用如下方法
        else if ("addGnssMeasurementsListener".equals(methodName)) {
            boolean b = middleLocationManagerService.addGnssMeasurementsListener(args[0], (String) args[1]);
            return new InvokeReturnObj(true, b);
        } else if ("removeGnssMeasurementsListener".equals(methodName)) {
            middleLocationManagerService.removeGnssMeasurementsListener(args[0]);
            return new InvokeReturnObj(true, null);
        } else if ("registerGnssStatusCallback".equals(methodName)) {//等同于addGpsStatus
            boolean b = middleLocationManagerService.registerGnssStatusCallback(args[0], (String) args[1]);
            return new InvokeReturnObj(true, b);
        } else if ("unregisterGnssStatusCallback".equals(methodName)) {
            middleLocationManagerService.unregisterGnssStatusCallback(args[0]);
            return new InvokeReturnObj(true, null);
        }

        return new InvokeReturnObj(false, null);//backup

    }
}
