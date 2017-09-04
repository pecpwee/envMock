package com.pecpwee.lib.envMock.middle.connect;

import android.net.Network;
import android.net.NetworkInfo;

import com.pecpwee.lib.envMock.middle.IMiddleService;

/**
 * Created by pw on 2017/9/4.
 */

public class MiddleConnectivityManager implements IMiddleService, IConnectivityListener {

    NetworkInfo mActiveNetworkInfo;
    Network[] mAllNetworks;

    @Override
    public void resetMockData() {
        mActiveNetworkInfo = null;
        mAllNetworks = null;
    }

    NetworkInfo getActiveNetworkInfo() {
        return mActiveNetworkInfo;
    }

    public Network[] getAllNetworks() {
        return mAllNetworks;
    }

    @Override
    public void setActiveNetworkInfo(NetworkInfo networkInfo) {
        this.mActiveNetworkInfo = networkInfo;
    }

    @Override
    public void setAllNetworks(Network[] networks) {
        this.mAllNetworks = networks;
    }
}
