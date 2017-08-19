package com.pecpwee.lib.envMock.hook;

import android.content.Context;
import android.os.IBinder;
import android.os.IInterface;

import com.pecpwee.lib.envMock.player.AbsPlayer;
import com.pecpwee.lib.envMock.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * Created by pw on 2017/6/14.
 */

public abstract class AbsServiceFetcher {
    protected Context context;
    private IBinder mOrigServiceBinder;
    private IBinder mProxyServiceBinder;
    private Object mOrigManager;
    private Object mProxyManager;
    private Object mMiddleManagerService;
    private Object mInterfaceBinder;

    private AbsPlayer mPlayer;

    public AbsServiceFetcher(Context context) {
        this.context = context;
        getOrigServiceBinder();//需要提前获取一次，因为之后会通过其他方式覆盖掉这个量
    }

    //返回未被hook的XXXXXservice的binder对象
    public final IBinder getOrigServiceBinder() {
        if (mOrigServiceBinder == null) {
            mOrigServiceBinder = createOrigBinderByServiceName();
        }
        return mOrigServiceBinder;
    }

    public final Object getOrigBinderProxyObj() {
        if (mInterfaceBinder == null) {
            mInterfaceBinder = createOrigBinderInterfaceObj();
        }
        return mInterfaceBinder;
    }

    private Object createOrigBinderInterfaceObj() {

        Class stubClass = getServiceInterfaceStubClass();

        try {
            Method asInterfaceMethod = null;
            asInterfaceMethod = stubClass.getDeclaredMethod("asInterface", IBinder.class);
            return asInterfaceMethod.invoke(null, getOrigServiceBinder());

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    //返回xxxxManager对象，例如LocationManager,这个是未被hook过的原始对象
    public final Object getOrigManagerObj() {

        if (mOrigManager == null) {
            mOrigManager = createManagerObj(getOrigServiceBinder());
        }
        return mOrigManager;

    }

    //返回xxxxManager对象，例如LocationManager,这个是被hook过的
    public final Object getProxyManagerObj() {
        if (mProxyManager == null) {
            mProxyManager = createManagerObj(getProxyServiceBinder());
        }
        return mProxyManager;
    }

    public final Object getMiddleManagerService() {
        if (mMiddleManagerService == null) {
            mMiddleManagerService = createMiddleManagerService();
        }
        return mMiddleManagerService;
    }

    //具体某个service binder
    public final IBinder getProxyServiceBinder() {
        if (mProxyServiceBinder == null) {

            mProxyServiceBinder = (IBinder) Proxy.newProxyInstance(getClass().getClassLoader()
                    , new Class[]{IBinder.class
                            , IInterface.class
                            , getServiceInterfaceClass()
                            , InvocationListenerProvider.class}
                    , createProxyServiceBinder());

        }
        return mProxyServiceBinder;
    }

    //得到对应的player
    public final AbsPlayer getPlayer() {
        if (mPlayer == null) {
            mPlayer = createPlayer();
        }
        return mPlayer;
    }

    private IBinder createOrigBinderByServiceName() {

        Class<?> serviceManager = null;
        try {
            serviceManager = Class.forName("android.os.ServiceManager");
            Method getService = serviceManager.getDeclaredMethod("getService", String.class);
            IBinder rawBinder = (IBinder) getService.invoke(null, getServiceName());
            return rawBinder;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    public abstract Object createManagerObj(IBinder binder);

    public abstract InvocationHandler createProxyServiceBinder();

    public void install() {
        installOnCenterServiceManager();
        installOnSysServiceManager();
    }

    protected Class getServiceInterfaceClass() {
        try {
            return Class.forName(getServiceBinderFullName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected Class getServiceInterfaceStubClass() {
        try {
            return Class.forName(getServiceBinderFullName() + "$Stub");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }


    private void installOnCenterServiceManager() {
        CenterServiceManager.getInstance().addService(getServiceName(), this);
    }

    private void installOnSysServiceManager() {
        IBinder dispatcherBinder = (IBinder) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[]{IBinder.class},
                new ProxyServiceManagerDispatcherBinder(getServiceName()));
        try {
            Class<?> serviceManager = Class.forName("android.os.ServiceManager");
            Field cacheField = serviceManager.getDeclaredField("sCache");
            cacheField.setAccessible(true);
            Map<String, IBinder> cache = (Map) cacheField.get(null);
            cache.put(getServiceName(), dispatcherBinder);
        } catch (Throwable t) {
            LogUtils.log(t);
        }
    }

    protected abstract Object createMiddleManagerService();

    protected abstract AbsPlayer createPlayer();

    public abstract String getServiceName();

    public abstract String getServiceBinderFullName();


}
