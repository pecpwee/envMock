package com.pecpwee.lib.envMock.middle.location;

import android.location.Location;
import android.os.Bundle;

/**
 * Created by pw on 2017/6/6.
 * <p>
 * onGpsStatus都是从外部的Onxxxxx方法回调，然后
 */

public interface IGpsPlayerListener {
    void onLocationChanged(Location location);

    void onStatusChanged(String provider, int status, Bundle extras);

    void onProviderEnabled(String provider);

    void onProviderDisabled(String provider);

    void onGpsStarted();

    void onGpsStopped();

    void onFirstFix(int ttff);


    void onSvStatusChanged(int svCount, int[] prns, float[] snrs, float[] elevations, float[] azimuths, int ephemerisMask,
                           int almanacMask, int usedInFixMask);
//
//    public void onSvStatusChanged(GpsStatus gpsStatus);
//    public void onSvStatusChanged(GnssStatus gpsStatus);//统一使用上面的gpsStatus对象。因为GnssStatus和GpsStatus本来就可以互相换

    void onNmeaReceived(long timestamp, String nmea);

}
