package com.pecpwee.lib.envmock.model.connect;

import android.net.Network;
import android.net.NetworkInfo;

import com.pecpwee.lib.envmock.model.AbsTimeModel;

/**
 * Created by pw on 2017/9/4.
 */

public class GetActiveNetwork extends AbsTimeModel {

    private NetworkInfo networkInfo;
    private Network network;

    public GetActiveNetwork(Network network, NetworkInfo networkInfo) {
        super(CONST.GetActiveNetwork);
        this.network = network;
        this.networkInfo = networkInfo;
    }

    public NetworkInfo getNetworkInfo() {
        return networkInfo;
    }

    public void setNetworkInfo(NetworkInfo networkInfo) {
        this.networkInfo = networkInfo;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
