package com.pecpwee.lib.envMock.middle.location.LM;

import com.pecpwee.lib.envMock.middle.location.AbsMiddleLocationManagerService;

import java.util.ArrayList;

/**
 * Created by pw on 2017/6/7.
 */

public class MiddleLocationManagerService extends AbsMiddleLocationManagerService {
    private ArrayList<IGpsStatusListenerWrapper> mGpsStatusWrapperList = new ArrayList<>();


    @Override
    public synchronized void onGpsStarted() {
        for (final IGpsStatusListenerWrapper gpsStatusWrapper : mGpsStatusWrapperList) {
            gpsStatusWrapper.onGpsStarted();
        }
    }

    @Override
    public synchronized void onGpsStopped() {
        for (final IGpsStatusListenerWrapper gpsStatusWrapper : mGpsStatusWrapperList) {
            gpsStatusWrapper.onGpsStopped();
        }
    }


    @Override
    public synchronized boolean addGpsStatusListener(Object IGpsStatusListener, String packageName) {
        IGpsStatusListenerWrapper gpsStatusWrapper = new IGpsStatusListenerWrapper(IGpsStatusListener);
        mGpsStatusWrapperList.remove(gpsStatusWrapper);
        mGpsStatusWrapperList.add(gpsStatusWrapper);
        return true;
    }

    @Override
    public synchronized void removeGpsStatusListener(Object GpsStatusListenerTransportWrapper) {
        mGpsStatusWrapperList.remove(new IGpsStatusListenerWrapper(GpsStatusListenerTransportWrapper));
    }

    @Override
    public synchronized void onFirstFix(int ttff) {
        for (final IGpsStatusListenerWrapper gpsStatusWrapper : mGpsStatusWrapperList) {
            gpsStatusWrapper.onFirstFix(ttff);
        }
    }

    @Override
    public synchronized void onSvStatusChanged(int svCount, int[] prns, float[] snrs, float[] elevations, float[] azimuths, int ephemerisMask, int almanacMask, int usedInFixMask) {
        for (final IGpsStatusListenerWrapper gpsStatusWrapper : mGpsStatusWrapperList) {
            gpsStatusWrapper.onSvStatusChanged(svCount, prns, snrs, elevations, azimuths, ephemerisMask, almanacMask, usedInFixMask);
        }
    }


    @Override
    public synchronized void onNmeaReceived(long timestamp, String nmea) {
        for (final IGpsStatusListenerWrapper gpsStatusWrapper : mGpsStatusWrapperList) {
            gpsStatusWrapper.onNmeaReceived(timestamp, nmea);
        }
    }

}
