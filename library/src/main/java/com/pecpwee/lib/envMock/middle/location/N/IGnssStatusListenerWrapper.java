package com.pecpwee.lib.envMock.middle.location.N;

import android.os.Build;

import com.pecpwee.lib.envMock.middle.location.AbsClassWrapper;
import com.pecpwee.lib.envMock.utils.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by pw on 2017/6/8.
 */

public class IGnssStatusListenerWrapper extends AbsClassWrapper {
    public IGnssStatusListenerWrapper(Object baseObj) {
        super(baseObj);
    }

    public void onGnssStarted() {
        try {
            MethodUtils.invokeMethod(baseObj, "onGnssStarted");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void onGnssStopped() {
        try {
            MethodUtils.invokeMethod(baseObj, "onGnssStopped");
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

    /*
    * Android Oreo:
    * public void onSvStatusChanged(int svCount, int[] prnWithFlags,float[] cn0s, float[] elevations, float[] azimuths, float[] carrierFreqs) {
    *
    * Android N:
    * public void onSvStatusChanged(int svCount, int[] prns, float[] snrs, float[] elevations, float[] azimuths)
    *
    * Below Android N:
    * public void onSvStatusChanged(int svCount, int[] prns, float[] snrs, float[] elevations, float[] azimuths
    *                               , int ephemerisMask,int almanacMask, int usedInFixMask)
    *
    * */
    public void onSvStatusChanged(int svCount, int[] prnWithFlags,
                                  float[] cn0s, float[] elevations, float[] azimuths) {
        try {
            Method[] method = baseObj.getClass().getMethods();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MethodUtils.invokeMethod(baseObj, "onSvStatusChanged", svCount, prnWithFlags, cn0s, elevations, azimuths, new float[1]);// TODO: 2017/9/21  
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                MethodUtils.invokeMethod(baseObj, "onSvStatusChanged", svCount, prnWithFlags, cn0s, elevations, azimuths);
            }
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
