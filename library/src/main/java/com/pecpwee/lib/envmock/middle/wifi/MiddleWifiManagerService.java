package com.pecpwee.lib.envmock.middle.wifi;


import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;

import com.pecpwee.lib.envmock.middle.IMiddleService;
import com.pecpwee.lib.envmock.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pw on 2017/6/8.
 */

public class MiddleWifiManagerService implements IWifiPlayerListener, IMiddleService {
    private List<ScanResult> currentScanWifiList;
    private WifiInfo connectionWifiInfo;

    public MiddleWifiManagerService() {
        currentScanWifiList = new ArrayList<>();
    }

    public synchronized List<ScanResult> getScanResults(String callingPackage) {
        return currentScanWifiList;
    }


    public synchronized WifiInfo getConnectionInfo() {
        return connectionWifiInfo;
    }

//    private ScanResult createScanResult() {
//        try {
//            ScanResult scanResult = MethodUtils.invokeConstructor(ScanResult.class, new Object[]{});
//            return scanResult;
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public synchronized void startScan(Object requested, Object ws) {
    }

    @Override
    public synchronized void setScanWifiList(List<ScanResult> list) {
        LogUtils.d("setScanWifiList");

        this.currentScanWifiList = list;
    }

    @Override
    public synchronized void setConnectionWifiInfo(WifiInfo wifiInfo) {
        LogUtils.d("setConnectionWifiInfo");
        this.connectionWifiInfo = wifiInfo;
    }

    @Override
    public void resetMockData() {
        if (currentScanWifiList != null) {
            currentScanWifiList.clear();
        }
        connectionWifiInfo = null;

    }

    /*
    *    public ScanResult(String Ssid, String BSSID, long hessid, int anqpDomainId, String caps,
            int level, int frequency,
            long tsf, int distCm, int distSdCm, int channelWidth, int centerFreq0, int centerFreq1,
            boolean is80211McRTTResponder)
    *
    * */
}
