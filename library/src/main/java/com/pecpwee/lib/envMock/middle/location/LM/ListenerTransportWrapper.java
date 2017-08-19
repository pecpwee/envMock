package com.pecpwee.lib.envMock.middle.location.LM;

import android.location.LocationListener;
import android.os.Handler;

import com.pecpwee.lib.envMock.middle.location.AbsClassWrapper;
import com.pecpwee.lib.envMock.utils.reflect.FieldUtils;

/**
 * Created by pw on 2017/6/2.
 */

//226    private class ListenerTransport extends ILocationListener.Stub {

public class ListenerTransportWrapper extends AbsClassWrapper {

    private Handler handler;
    private LocationListener listener;


    public ListenerTransportWrapper(Object baseObj) {
        super(baseObj);
    }

    public Handler getHandler() {
//        try {
//            clazz = Class.forName("android.location.LocationManager$ListenerTransport");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        if (handler != null) {
            return handler;
        }
        try {
            handler = (Handler) FieldUtils.readField(baseObj, "mListenerHandler", true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return handler;
    }

    public LocationListener getLocationListener() {
        if (listener != null) {
            return listener;
        }
        try {
            listener = (LocationListener) FieldUtils.readField(baseObj, "mListener", true);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return listener;
    }



}
