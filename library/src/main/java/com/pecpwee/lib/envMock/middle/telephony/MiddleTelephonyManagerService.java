package com.pecpwee.lib.envMock.middle.telephony;

import android.os.Bundle;

import com.pecpwee.lib.envMock.middle.IMiddleService;

/**
 * Created by pw on 2017/6/14.
 */

public class MiddleTelephonyManagerService implements ITelephonyPlayerListener, IMiddleService {

    private Bundle cellLocation;
    private int networkType;

    //返回一个bundle，TelephonyManager进行解析为原始的CellLocation对象
    public synchronized Bundle getCellLocation() {
        return cellLocation;
    }

    public synchronized int getNetworkType() {
        return networkType;
    }

    @Override
    public synchronized void setCellLocation(Bundle bundle) {
        this.cellLocation = bundle;
    }

    @Override
    public synchronized void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    @Override
    public void resetMockData() {
        cellLocation = null;
        networkType = 0;
    }
}
