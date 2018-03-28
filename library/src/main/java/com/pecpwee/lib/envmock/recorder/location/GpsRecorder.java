package com.pecpwee.lib.envmock.recorder.location;

import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;

import com.pecpwee.lib.envmock.AbsConfig;
import com.pecpwee.lib.envmock.EnvMockInstaller;
import com.pecpwee.lib.envmock.hook.CenterServiceManager;
import com.pecpwee.lib.envmock.model.location.OnFirstFix;
import com.pecpwee.lib.envmock.model.location.OnGpsStarted;
import com.pecpwee.lib.envmock.model.location.OnGpsStopped;
import com.pecpwee.lib.envmock.model.location.OnLocationChanged;
import com.pecpwee.lib.envmock.model.location.OnNmeaReceived;
import com.pecpwee.lib.envmock.model.location.OnProviderDisable;
import com.pecpwee.lib.envmock.model.location.OnProviderEnabled;
import com.pecpwee.lib.envmock.model.location.OnStatusChanged;
import com.pecpwee.lib.envmock.model.location.OnSvStatusChanged;
import com.pecpwee.lib.envmock.player.location.LM.IGpsStatusListener;
import com.pecpwee.lib.envmock.player.location.N.IGnssStatusListener;
import com.pecpwee.lib.envmock.recorder.AbsRecorder;
import com.pecpwee.lib.envmock.utils.LogUtils;
import com.pecpwee.lib.envmock.utils.reflect.MethodUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by pw on 2017/6/2.
 */

public class GpsRecorder extends AbsRecorder {

    private LocationManager locationManager;


    public GpsRecorder() {
        locationManager = (LocationManager) CenterServiceManager.getInstance().getServiceFetcher(Context.LOCATION_SERVICE).getOrigManagerObj();
    }


    @Override
    public void onStart() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationlistner);
            locationManager.addNmeaListener(nmeaListener);
            registerGpsStatusListener();
        } catch (SecurityException e) {
            LogUtils.log(e);
        }

    }

    @Override
    public void onStop() {
        locationManager.removeNmeaListener(nmeaListener);
        locationManager.removeUpdates(locationlistner);
        removeGpsStatusListener();
    }

    @Override
    public String getDefaultFilePath() {
        return AbsConfig.DEFAULT_FILEPATH_GPS;
    }


    GpsStatus.NmeaListener nmeaListener = new GpsStatus.NmeaListener() {
        @Override
        public void onNmeaReceived(long timestamp, String nmea) {
            doRecord(new OnNmeaReceived(timestamp, nmea));
        }
    };

    LocationListener locationlistner = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            doRecord(new OnLocationChanged(location));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            doRecord(new OnStatusChanged(provider, status, extras));
        }

        @Override
        public void onProviderEnabled(String provider) {
            doRecord(new OnProviderEnabled(provider));
        }

        @Override
        public void onProviderDisabled(String provider) {
            doRecord(new OnProviderDisable(provider));
        }
    };


    private Object gpsStatusTrasnportListener = getGpsStatusTransport();

    private void registerGpsStatusListener() {
        Object binderInterfaceObj = CenterServiceManager.getInstance().getServiceFetcher(Context.LOCATION_SERVICE)
                .getOrigBinderProxyObj();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                MethodUtils.invokeMethod(binderInterfaceObj, "registerGnssStatusCallback", gpsStatusTrasnportListener, CenterServiceManager.getInstance().getPackageName());

            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MethodUtils.invokeMethod(binderInterfaceObj, "addGpsStatusListener", gpsStatusTrasnportListener, CenterServiceManager.getInstance().getPackageName());

            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void removeGpsStatusListener() {
        Object binderObj = CenterServiceManager.getInstance().getServiceFetcher(Context.LOCATION_SERVICE)
                .getOrigBinderProxyObj();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                MethodUtils.invokeMethod(binderObj, "unregisterGnssStatusCallback", gpsStatusTrasnportListener);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                MethodUtils.invokeMethod(binderObj, "removeGpsStatusListener", gpsStatusTrasnportListener);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private Object getGpsStatusTransport() {
        Class clazz = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                clazz = Class.forName("android.location.IGnssStatusListener");
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                clazz = Class.forName("android.location.IGpsStatusListener");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Object obj = Proxy.newProxyInstance(EnvMockInstaller.class.getClassLoader(),
                new Class<?>[]{IBinder.class, IInterface.class, clazz},
                new GpsTransportRecord());
        return obj;

    }

    private class GpsTransportRecord implements InvocationHandler {

        private IBinder baseObj;

        public GpsTransportRecord() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                this.baseObj = new IGnssStatusListener() {
                    @Override
                    public void onSvStatusChanged(int _arg01, int[] _arg1, float[] _arg12, float[] arg3, float[] _arg4) {
                        doRecord(new OnSvStatusChanged(
                                _arg01
                                , _arg1
                                , _arg12
                                , arg3
                                , _arg4
                                , 0, 0, 0));
                    }

                    @Override
                    public void onGnssStarted() {
                        doRecord(new OnGpsStarted());
                    }

                    @Override
                    public void onGnssStopped() {
                        doRecord(new OnGpsStopped());
                    }

                    @Override
                    public void onFirstFix(int ttff) {
                        doRecord(new OnFirstFix(ttff));
                    }
                };
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.baseObj = new IGpsStatusListener() {
                    @Override
                    public void onSvStatusChanged(int _arg01, int[] _arg1, float[] _arg12, float[] arg3, float[] _arg4, int _arg5, int _arg6, int _arg7) {
                        doRecord(new OnSvStatusChanged(
                                _arg01
                                , _arg1
                                , _arg12
                                , arg3
                                , _arg4
                                , _arg5, _arg6, _arg7));
                    }

                    @Override
                    public void onGpsStarted() {
                        doRecord(new OnGpsStarted());
                    }

                    @Override
                    public void onGpsStopped() {
                        doRecord(new OnGpsStopped());
                    }

                    @Override
                    public void onFirstFix(int ttff) {
                        doRecord(new OnFirstFix(ttff));

                    }
                };
            }
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(baseObj, args);
        }
    }


}
