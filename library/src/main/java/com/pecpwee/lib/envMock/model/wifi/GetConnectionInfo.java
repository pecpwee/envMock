package com.pecpwee.lib.envMock.model.wifi;

import android.net.wifi.WifiInfo;

import com.pecpwee.lib.envMock.model.AbsTimeModel;

/**
 * Created by pw on 2017/6/12.
 */

public class GetConnectionInfo extends AbsTimeModel {
    public WifiInfo getWifiInfo() {
        return wifiInfo;
    }

    public void setWifiInfo(WifiInfo wifiInfo) {
        this.wifiInfo = wifiInfo;
    }

    WifiInfo wifiInfo;

    public GetConnectionInfo(WifiInfo wifiInfo) {
        super(CONST.GET_CONNECTION_INFO);
        this.wifiInfo = wifiInfo;
    }
}
