package com.pecpwee.lib.envMock.middle.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import com.pecpwee.lib.envMock.hook.AbsServiceFetcher;
import com.pecpwee.lib.envMock.player.AbsPlayer;
import com.pecpwee.lib.envMock.player.wifi.WifiPlayer;
import com.pecpwee.lib.envMock.utils.LogUtils;
import com.pecpwee.lib.envMock.utils.reflect.MethodUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by pw on 2017/6/14.
 */

public class WifiServiceFactory extends AbsServiceFetcher {

    public WifiServiceFactory(Context context) {
        super(context);
    }

    @Override
    public String getServiceName() {
        return Context.WIFI_SERVICE;
    }

    @Override
    public String getServiceBinderFullName() {
        return "android.net.wifi.IWifiManager";

    }


    @Override
    public Object createMiddleManagerService() {
        return new MiddleWifiManagerService();
    }


    @Override
    public Object createManagerObj(IBinder binder) {
        try {
            Class lmsInterfaceStub = Class.forName(getServiceBinderFullName() + "$Stub");
            Object serviceInterface = MethodUtils.invokeStaticMethod(lmsInterfaceStub, "asInterface", binder);
            int paramLength = WifiManager.class.getConstructors()[0].getParameterTypes().length;
            Object[] paramList = null;
            if (paramLength == 2) {//wifiManager在Pixel AndroidN 上构造方法变成两个参数，比较特别
                paramList = new Object[]{context, serviceInterface};//、、pixil
            } else if (paramLength == 3) {
                paramList = new Object[]{context, serviceInterface, context.getMainLooper()};
            }

            WifiManager wifiManager = MethodUtils.invokeConstructor(WifiManager.class, paramList);
            Object obj = wifiManager;
            return obj;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            LogUtils.log(t);
        }
        return null;

    }

    @Override
    public AbsPlayer createPlayer() {
        WifiPlayer wifiPlayer = new WifiPlayer((IWifiPlayerListener) getMiddleManagerService());
        return wifiPlayer;
    }

}
