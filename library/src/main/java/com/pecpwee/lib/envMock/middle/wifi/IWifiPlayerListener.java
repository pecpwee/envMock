package com.pecpwee.lib.envMock.middle.wifi;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;

import java.util.List;

/**
 * Created by pw on 2017/6/12.
 */

public interface IWifiPlayerListener {
    void setScanWifiList(List<ScanResult> list);
    void setConnectionWifiInfo(WifiInfo wifiInfo);
}
