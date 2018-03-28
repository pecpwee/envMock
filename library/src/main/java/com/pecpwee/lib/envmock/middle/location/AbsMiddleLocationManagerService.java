package com.pecpwee.lib.envmock.middle.location;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.pecpwee.lib.envmock.PlayConfig;
import com.pecpwee.lib.envmock.middle.IMiddleService;
import com.pecpwee.lib.envmock.middle.location.LM.ListenerTransportWrapper;
import com.pecpwee.lib.envmock.middle.location.LM.LocationRequestWrapper;
import com.pecpwee.lib.envmock.middle.location.LM.RequestLocationWrapper;
import com.pecpwee.lib.envmock.player.PlayerManager;
import com.pecpwee.lib.envmock.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by pw on 2017/6/2.
 */

public abstract class AbsMiddleLocationManagerService implements IGpsPlayerListener, IMiddleService {
    private static final String TAG = "AbsMiddleLocationManagerService ";
    private ArrayList<RequestLocationWrapper> mRequestLocationList = new ArrayList<>();

    public synchronized LocationManager getOrigLocationManager(Context context) {
        if (contextCachedCachedLocationService == null) {
            contextCachedCachedLocationService = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        return contextCachedCachedLocationService;
    }

    private LocationManager contextCachedCachedLocationService;

    public AbsMiddleLocationManagerService() {
    }


    public final synchronized void requestLocationUpdates(Object LocationRequest, Object ILocationListener,
                                                          PendingIntent pendingintent, String packageName) {
        // TODO: 2017/6/6 support pendingIntent
        LocationRequestWrapper locationRequestWrapper = new LocationRequestWrapper(LocationRequest);
        final ListenerTransportWrapper listenerTransportWrapper = new ListenerTransportWrapper(ILocationListener);
        RequestLocationWrapper requestLocationWrapper = new RequestLocationWrapper(locationRequestWrapper, listenerTransportWrapper);
        mRequestLocationList.remove(requestLocationWrapper);
        mRequestLocationList.add(requestLocationWrapper);
        LogUtils.d(TAG + "requestLocationUpdates");

        if (mRequestLocationList.size() == 1 && PlayConfig.getInstance().isAutoPlayMode()) {
            PlayerManager.doStartPlay();
        }
    }

    public final synchronized void removeUpdates(Object ILocationListener, PendingIntent intent, String packageName) {
        // TODO: 2017/6/6 support pendingIntent
        final ListenerTransportWrapper listenerTransportWrapper = new ListenerTransportWrapper(ILocationListener);
        RequestLocationWrapper requestLocationWrapper = new RequestLocationWrapper(null, listenerTransportWrapper);

        mRequestLocationList.remove(requestLocationWrapper);

        LogUtils.d("removeUpdates,now the size:" + mRequestLocationList.size());
        if (mRequestLocationList.size() == 0 && PlayConfig.getInstance().isAutoStopMode()) {
            PlayerManager.doStopPlay();
        }
    }

    //ListenerTransport
    @Override
    public final synchronized void onLocationChanged(final Location location) {
        for (final RequestLocationWrapper requestLocationWrapper : mRequestLocationList) {
            LocationRequestWrapper locationRequestWrapper = requestLocationWrapper.getLocationRequestWrapper();
            final ListenerTransportWrapper listenerTransportWrapper = requestLocationWrapper.getListenerTransportWrapper();
            String providerName = locationRequestWrapper.getProvider();
            if (LocationManager.GPS_PROVIDER.equals(location.getProvider())) {
                if (LocationManager.GPS_PROVIDER.equals(providerName)
                        || LocationManager.PASSIVE_PROVIDER.equals(providerName))
                    listenerTransportWrapper.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            listenerTransportWrapper.getLocationListener().onLocationChanged(location);
                        }
                    });
            } else if (LocationManager.NETWORK_PROVIDER.equals(location.getProvider())) {
                if (LocationManager.NETWORK_PROVIDER.equals(providerName)
                        || LocationManager.PASSIVE_PROVIDER.equals(providerName))
                    listenerTransportWrapper.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            listenerTransportWrapper.getLocationListener().onLocationChanged(location);
                        }
                    });

            }
        }
    }

    //ListenerTransport
    //网络改变才回调这个
    @Override
    public final synchronized void onStatusChanged(final String provider, final int status, final Bundle extras) {
        for (final RequestLocationWrapper requestLocationWrapper : mRequestLocationList) {
            LocationRequestWrapper locationRequestWrapper = requestLocationWrapper.getLocationRequestWrapper();
            final ListenerTransportWrapper listenerTransportWrapper = requestLocationWrapper.getListenerTransportWrapper();
            String providerName = locationRequestWrapper.getProvider();
            if (LocationManager.NETWORK_PROVIDER.equals(providerName)
                    || LocationManager.PASSIVE_PROVIDER.equals(providerName))
                listenerTransportWrapper.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        listenerTransportWrapper.getLocationListener().onStatusChanged(provider, status, extras);
                    }
                });
        }
    }

    //ListenerTransport
    @Override
    public final synchronized void onProviderEnabled(final String provider) {
        for (final RequestLocationWrapper requestLocationWrapper : mRequestLocationList) {
            final ListenerTransportWrapper listenerTransportWrapper = requestLocationWrapper.getListenerTransportWrapper();
            LocationRequestWrapper locationRequestWrapper = requestLocationWrapper.getLocationRequestWrapper();
            String providerName = locationRequestWrapper.getProvider();
            if (LocationManager.GPS_PROVIDER.equals(providerName)
                    || LocationManager.PASSIVE_PROVIDER.equals(providerName)) {

                listenerTransportWrapper.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        listenerTransportWrapper.getLocationListener().onProviderEnabled(provider);
                    }
                });
            }

        }
    }

    //ListenerTransport
    @Override
    public final synchronized void onProviderDisabled(final String provider) {
        for (final RequestLocationWrapper requestLocationWrapper : mRequestLocationList) {

            final ListenerTransportWrapper listenerTransportWrapper = requestLocationWrapper.getListenerTransportWrapper();

            LocationRequestWrapper locationRequestWrapper = requestLocationWrapper.getLocationRequestWrapper();
            String providerName = locationRequestWrapper.getProvider();
            if (LocationManager.GPS_PROVIDER.equals(providerName)
                    || LocationManager.PASSIVE_PROVIDER.equals(providerName)) {
                listenerTransportWrapper.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        listenerTransportWrapper.getLocationListener().onProviderEnabled(provider);
                    }
                });

            }
        }
    }


    //Method below should be implement by child class
    public boolean addGpsStatusListener(Object IGpsStatusListener, String packageName) {
        throw new RuntimeException("method have not implement");
    }

    public void removeGpsStatusListener(Object GpsStatusListenerTransportWrapper) {
        throw new RuntimeException("method have not implement");

    }

    //7.0 and above
    public boolean addGnssMeasurementsListener(Object IGnssMeasurementsListener, String packageName) {
        throw new RuntimeException("method have not implement");

    }

    public void removeGnssMeasurementsListener(Object IGnssMeasurementsListener) {
        throw new RuntimeException("method have not implement");
    }

    public boolean registerGnssStatusCallback(Object IGnssStatusListener, String packageName) {
        throw new RuntimeException("method have not implement");
    }

    public void unregisterGnssStatusCallback(Object IGnssStatusListener) {
        throw new RuntimeException("method have not implement");
    }

    @Override
    public void onFirstFix(int ttff) {
        throw new RuntimeException("method have not implement");
    }

    @Override
    public void onNmeaReceived(final long timestamp, final String nmea) {
        throw new RuntimeException("method have not implement");
    }

    @Override
    public void resetMockData() {
        //nothing to do here.
    }
}
