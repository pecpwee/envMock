package com.pecpwee.lib.envmock.model.connect;

import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

import com.pecpwee.lib.envmock.model.AbsTimeModel;

/**
 * Created by pw on 2017/9/4.
 */

public class GetAllNetworks extends AbsTimeModel {

    Network[] allNetworks;
    NetworkCapabilities[] networkCapabilities;
    NetworkInfo[] networkInfos;

    public Network[] getAllNetworks() {
        return this.allNetworks;
    }

    public NetworkCapabilities[] getNetworkCapabilities() {
        return this.networkCapabilities;
    }

    public NetworkInfo[] getNetworkInfos() {
        return this.networkInfos;
    }


    public GetAllNetworks(Network[] allNetworks, NetworkInfo[] networkInfos, NetworkCapabilities[] networkCapabilities) {
        super(CONST.GetAllNetworks);
        this.allNetworks = allNetworks;
        this.networkInfos = networkInfos;
        this.networkCapabilities = networkCapabilities;
    }
}
