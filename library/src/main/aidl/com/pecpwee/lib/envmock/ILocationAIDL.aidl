// ILocationAIDL.aidl
package com.pecpwee.lib.envmock;

// Declare any non-default types here with import statements
import android.location.Location;
interface  ILocationAIDL
{
    void onGpsStarted();
    void onGpsStopped();
    void onFirstFix(int ttff);
    void onSvStatusChanged(int svCount, in int[] prns, in float[] snrs,
            in float[] elevations, in float[] azimuths,
            int ephemerisMask, int almanacMask, int usedInFixMask);
    void onNmeaReceived(long timestamp, String nmea);
}
