package com.pecpwee.test.envMock;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pecpwee.lib.envMock.utils.LogUtils;
import com.pecpwee.lib.envMock.utils.TimerJob;

import java.util.Iterator;
import java.util.List;

//tester
public class PlayerActivity extends AppCompatActivity {
    private TextView tvOutput;
    private LocationManager locationManager;
    private WifiManager wifiManager;
    private TelephonyManager telephonyManager;

    private Button btnStart;
    private Button btnStop;
    private Button btnGoRecord;
    private TimerJob timerJob = new TimerJob();
    private LocateSignalBundle signalBundle = new LocateSignalBundle();
    private static int PERMISSION_REQUEST_CODE = 1;

    public static class LocateSignalBundle {
        public List<ScanResult> wifiList;
        public WifiInfo connectWifi;
        public String nmea;
        public Location gpsLocation;
        public CellLocation cellLocation;
        public int gpsFixCount = 0;

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();

            if (wifiList != null) {
                sb.append("wifiList size:").append(wifiList.size()).append("\n");
                for (ScanResult sc : wifiList) {
                    sb.append(sc.BSSID).append("\n");
                }
            }
            if (connectWifi != null) {
                sb.append("connectWifi.ssid:" + connectWifi.getSSID()).append("\n");
            }
            sb.append("gpsFixCount:").append(gpsFixCount).append("\n");
            if (nmea != null) {
                sb.append("nmea:").append(nmea).append("\n");
            }
            if (gpsLocation != null) {
                sb.append("gpslocation:")
                        .append(gpsLocation.getLatitude())
                        .append(",")
                        .append(gpsLocation.getLongitude())
                        .append("\n");
            }
            if (cellLocation != null) {
                sb.append("celllocation:");
                if (cellLocation instanceof GsmCellLocation) {
                    sb.append("GsmCellLocation");
                }
                if (cellLocation instanceof CdmaCellLocation) {
                    sb.append("CdmaCellLocation");
                }
            }
            return sb.toString();

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        requestPermission();
        tvOutput = (TextView) findViewById(R.id.tv_output);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnGoRecord = (Button) findViewById(R.id.btn_go_record);

        getSupportActionBar().setTitle("Replay");
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        timerJob = new TimerJob()
                .setInterval(2000)
                .setRunnable(new Runnable() {
                    @Override
                    public void run() {

                        signalBundle.wifiList = wifiManager.getScanResults();
                        signalBundle.connectWifi = wifiManager.getConnectionInfo();
                        signalBundle.cellLocation = telephonyManager.getCellLocation();
                        tvOutput.setText(signalBundle.toString());
                    }
                });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(PlayerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 10, locationlistner);
                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10, 10, passiveListener);
                locationManager.addNmeaListener(nmeaListener);
                locationManager.addGpsStatusListener(statusListener);
                wifiManager.startScan();
                timerJob.start();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.removeUpdates(locationlistner);
                locationManager.removeUpdates(passiveListener);

                locationManager.removeNmeaListener(nmeaListener);
                locationManager.removeGpsStatusListener(statusListener);
                timerJob.stop();

            }
        });

        btnGoRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayerActivity.this, RecorderActivity.class));
            }
        });
    }


    GpsStatus.NmeaListener nmeaListener = new GpsStatus.NmeaListener() {
        @Override
        public void onNmeaReceived(long timestamp, String nmea) {
            signalBundle.nmea = nmea;
//            LogUtils.d("nmea:" + nmea);
        }
    };
    LocationListener passiveListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            LogUtils.d("passive got");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogUtils.d("passive onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogUtils.d("passive onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogUtils.d("passive onProviderDisabled");
        }
    };


    LocationListener locationlistner = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LogUtils.d("onLocationChanged" + location.getLatitude() + "," + location.getLongitude());
            signalBundle.gpsLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogUtils.d("onStatusChanged " + "provider:" + provider + ",status:" + status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogUtils.d("onProviderEnabled " + provider);

        }

        @Override
        public void onProviderDisabled(String provider) {
            LogUtils.d("onProviderDisabled " + provider);
        }
    };
    GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        @Override
        public void onGpsStatusChanged(int event) {
            int viewCount = 0;
            int currentfixCount = 0;
            GpsStatus gpsStatus = null;
            try {
                if (ActivityCompat.checkSelfPermission(PlayerActivity.this
                        , Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                gpsStatus = locationManager.getGpsStatus(null);
                if (gpsStatus == null) {
                    return;
                }
                Iterator<GpsSatellite> it = gpsStatus.getSatellites().iterator();
                if (it == null) {
                    return;
                }
                try {
                    while (it.hasNext()) {
                        GpsSatellite s = it.next();
                        if (s == null) {
                            return;
                        }
                        currentfixCount += s.usedInFix() ? 1 : 0;
                        viewCount++;
                    }
                } catch (Throwable t) {//s.usedInFix() NOSUCHMETHOD
                }
                signalBundle.gpsFixCount = currentfixCount;
                LogUtils.d("onGpsStatusChanged fixed satellite num:" + currentfixCount);
            } catch (Throwable t) {
                LogUtils.log(t);
            }
        }


    };
    private void requestPermission() {
        ActivityCompat.requestPermissions(PlayerActivity.this,
                new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION
                        , android.Manifest.permission.ACCESS_FINE_LOCATION
                        , android.Manifest.permission.READ_PHONE_STATE
                        , android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                PERMISSION_REQUEST_CODE
        );
    }

}
