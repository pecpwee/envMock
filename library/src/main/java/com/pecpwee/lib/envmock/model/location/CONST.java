package com.pecpwee.lib.envmock.model.location;

/**
 * Created by pw on 2017/6/8.
 */
/*
*
226    private class ListenerTransport extends ILocationListener.Stub {
256        public void onLocationChanged(Location location) {
264        public void onStatusChanged(String provider, int status, Bundle extras) {
278        public void onProviderEnabled(String provider) {
286        public void onProviderDisabled(String provider) {


1397    private class GnssStatusListenerTransport extends IGnssStatusListener.Stub {
1599        public void onGnssStarted() {
1608        public void onGnssStopped() {
1617        public void onFirstFix(int ttff) {
1627        public void onSvStatusChanged(int svCount, int[] prnWithFlags,
1628                float[] cn0s, float[] elevations, float[] azimuths) {
1641        public void onNmeaReceived(long timestamp, String nmea) {
*
*
*
* */
public class CONST {

    public static final int ON_LOCATION_CHANGED = 1;
    public static final int ON_STATUS_CHANGED = ON_LOCATION_CHANGED + 1;
    public static final int ON_PROVIDER_ENABLE = ON_STATUS_CHANGED + 1;
    public static final int ON_PROVIDER_DISABLE = ON_PROVIDER_ENABLE + 1;

    public static final int ON_GPS_START = ON_PROVIDER_DISABLE + 1;
    public static final int ON_GPS_STOPPED = ON_GPS_START + 1;
    public static final int ON_FIRST_FIX = ON_GPS_STOPPED + 1;
    public static final int ON_SVSTATUS_CHANGED = ON_FIRST_FIX + 1;

    public static final int ON_NMEA_RECEIVED = ON_SVSTATUS_CHANGED + 1;


}
