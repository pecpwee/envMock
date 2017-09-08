package com.pecpwee.lib.envMock.middle.connect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.pecpwee.lib.envMock.hook.AbsServiceFetcher;
import com.pecpwee.lib.envMock.player.AbsPlayer;
import com.pecpwee.lib.envMock.player.connect.ConnPlayer;
import com.pecpwee.lib.envMock.utils.LogUtils;
import com.pecpwee.lib.envMock.utils.reflect.MethodUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by pw on 2017/9/4.
 */

public class ConnectivityServiceFactory extends AbsServiceFetcher {
    public ConnectivityServiceFactory(Context context) {
        super(context);
    }

    //          public ConnectivityManager(Context context, IConnectivityManager service)

    @Override
    public Object createManagerObj(IBinder binder) {
        try {
            Class lmsInterfaceStub = Class.forName(getServiceBinderFullName() + "$Stub");
            Object serviceInterface = MethodUtils.invokeStaticMethod(lmsInterfaceStub, "asInterface", binder);
            int paramLength = ConnectivityManager.class.getConstructors()[0].getParameterTypes().length;
            Object[] paramList = null;
            if (paramLength == 2) {
                paramList = new Object[]{context, serviceInterface};
            }
            ConnectivityManager connManager = MethodUtils.invokeConstructor(ConnectivityManager.class, paramList);
            return connManager;
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
    public InvocationHandler createProxyServiceBinder(String serviceName) {
        return new IConnectivityManager(getOrigBinderProxyObj(), serviceName);
    }

    @Override
    protected Object createMiddleManagerService() {
        return new MiddleConnectivityManager();
    }

    @Override
    protected AbsPlayer createPlayer() {
        return new ConnPlayer((IConnectivityListener) getMiddleManagerService());
    }

    @Override
    public String getServiceName() {
        return Context.CONNECTIVITY_SERVICE;
    }

    @Override
    public String getServiceBinderFullName() {
        return "android.net.IConnectivityManager";
    }
}
