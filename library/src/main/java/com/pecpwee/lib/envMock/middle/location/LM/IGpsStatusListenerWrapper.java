package com.pecpwee.lib.envMock.middle.location.LM;


import com.pecpwee.lib.envMock.middle.location.AbsClassWrapper;
import com.pecpwee.lib.envMock.utils.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by pw on 2017/6/8.
 */

public class IGpsStatusListenerWrapper extends AbsClassWrapper {
    public IGpsStatusListenerWrapper(Object baseObj) {
        super(baseObj);
    }

    public void onGpsStarted() {
        try {
            MethodUtils.invokeMethod(baseObj, "onGpsStarted");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void onGpsStopped() {
        try {
            MethodUtils.invokeMethod(baseObj, "ON_GPS_STOPPED");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void onFirstFix(int ttff) {
        try {
            MethodUtils.invokeMethod(baseObj, "onFirstFix", ttff);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void onSvStatusChanged(int svCount, int[] prns, float[] snrs,
                                  float[] elevations, float[] azimuths, int ephemerisMask,
                                  int almanacMask, int usedInFixMask) {
        try {
            MethodUtils.invokeMethod(baseObj
                    , "onSvStatusChanged"
                    ,
                    svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    public void onNmeaReceived(long timestamp, String nmea) {
        try {
            MethodUtils.invokeMethod(baseObj, "onNmeaReceived", timestamp, nmea);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
