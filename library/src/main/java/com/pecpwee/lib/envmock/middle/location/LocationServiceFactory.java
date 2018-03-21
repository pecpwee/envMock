package com.pecpwee.lib.envmock.middle.location;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;

import com.pecpwee.lib.envmock.hook.AbsServiceFetcher;
import com.pecpwee.lib.envmock.player.AbsPlayer;
import com.pecpwee.lib.envmock.player.location.GpsPlayer;
import com.pecpwee.lib.envmock.utils.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by pw on 2017/6/14.
 */

public class LocationServiceFactory extends AbsServiceFetcher {
    public LocationServiceFactory(Context context) {
        super(context);
    }

    @Override
    public Object createManagerObj(IBinder binder) {
        try {
            Class lmsInterfaceStub = Class.forName("android.location.ILocationManager$Stub");
            Object serviceInterface = MethodUtils.invokeStaticMethod(lmsInterfaceStub, "asInterface", binder);
            return MethodUtils.invokeConstructor(LocationManager.class, context, serviceInterface);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Object createMiddleManagerService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new com.pecpwee.lib.envmock.middle.location.N.MiddleLocationManagerService();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new com.pecpwee.lib.envmock.middle.location.LM.MiddleLocationManagerService();
        }
        return null;
    }

    @Override
    public AbsPlayer createPlayer() {
        return new GpsPlayer((IGpsPlayerListener) getMiddleManagerService());
    }

    @Override
    public String getServiceName() {
        return Context.LOCATION_SERVICE;
    }

    @Override
    public String getServiceBinderFullName() {
        return "android.location.ILocationManager";
    }
}
