package com.pecpwee.lib.envMock.middle.connect;

import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

/**
 * Created by pw on 2017/9/4.
 */

public interface IConnectivityListener {
    void setActiveNetwork(Network network, NetworkInfo networkInfo);

    void setAllNetworks(Network[] networks, NetworkInfo[] networkInfos, NetworkCapabilities[] capabilitiesArray);
}
