package com.pecpwee.lib.envMock.middle.telephony;

import android.content.Context;
import android.os.IBinder;
import android.telephony.TelephonyManager;

import com.pecpwee.lib.envMock.hook.AbsServiceFetcher;
import com.pecpwee.lib.envMock.player.AbsPlayer;
import com.pecpwee.lib.envMock.player.telephony.TelephonyPlayer;
import com.pecpwee.lib.envMock.utils.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by pw on 2017/6/14.
 */

public class TelephonyServiceFactory extends AbsServiceFetcher {

    public TelephonyServiceFactory(Context context) {
        super(context);
    }

    @Override
    public Object createManagerObj(IBinder binder) {
        try {
            return MethodUtils.invokeConstructor(TelephonyManager.class, context);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Object getOrigManagerObj() {
        throw new RuntimeException("cannot retrive original manager of the service:" + getServiceName());
    }

    @Override
    protected Object createMiddleManagerService() {
        return new MiddleTelephonyManagerService();
    }

    @Override
    protected AbsPlayer createPlayer() {
        return new TelephonyPlayer((ITelephonyPlayerListener) getMiddleManagerService());
    }

    @Override
    public String getServiceName() {
        return Context.TELEPHONY_SERVICE;
    }

    @Override
    public String getServiceBinderFullName() {
        return "com.android.internal.telephony.ITelephony";
    }
}
