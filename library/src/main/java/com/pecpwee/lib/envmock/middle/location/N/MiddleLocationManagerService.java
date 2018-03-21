package com.pecpwee.lib.envmock.middle.location.N;

import com.pecpwee.lib.envmock.middle.location.AbsMiddleLocationManagerService;

import java.util.ArrayList;

/**
 * Created by pw on 2017/6/7.
 */

public class MiddleLocationManagerService extends AbsMiddleLocationManagerService {
    private ArrayList<IGnssStatusListenerWrapper> mGpsStatusWrapperList = new ArrayList<>();


    @Override
    public synchronized void onGpsStarted() {
        for (final IGnssStatusListenerWrapper gnssListener : mGpsStatusWrapperList) {
            gnssListener.onGnssStarted();
        }
    }


    @Override
    public synchronized void onGpsStopped() {
        for (final IGnssStatusListenerWrapper gnssListener : mGpsStatusWrapperList) {
            gnssListener.onGnssStopped();
        }
    }

    @Override
    public synchronized void onFirstFix(int ttff) {
        for (final IGnssStatusListenerWrapper gnssListener : mGpsStatusWrapperList) {
            gnssListener.onFirstFix(ttff);
        }
    }

    @Override
    public synchronized void onSvStatusChanged(int svCount, int[] prns, float[] snrs, float[] elevations, float[] azimuths, int ephemerisMask, int almanacMask, int usedInFixMask) {
        for (final IGnssStatusListenerWrapper gnssListener : mGpsStatusWrapperList) {
            gnssListener.onSvStatusChanged(svCount, prns, snrs, elevations, azimuths);
        }
    }

    @Override
    public synchronized boolean registerGnssStatusCallback(Object IGnssStatusListener, String packageName) {
        IGnssStatusListenerWrapper iGnssStatusListener = new IGnssStatusListenerWrapper(IGnssStatusListener);
        mGpsStatusWrapperList.remove(iGnssStatusListener);
        return mGpsStatusWrapperList.add(iGnssStatusListener);
    }

    @Override
    public synchronized void unregisterGnssStatusCallback(Object IGnssStatusListener) {
        mGpsStatusWrapperList.remove(new IGnssStatusListenerWrapper(IGnssStatusListener));
        return;
    }

    @Override
    public synchronized void onNmeaReceived(long timestamp, String nmea) {
        for (final IGnssStatusListenerWrapper gnssListener : mGpsStatusWrapperList) {
            gnssListener.onNmeaReceived(timestamp, nmea);
        }
    }
}
