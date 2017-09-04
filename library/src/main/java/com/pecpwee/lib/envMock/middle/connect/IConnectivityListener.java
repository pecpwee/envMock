package com.pecpwee.lib.envMock.middle.connect;

import android.net.Network;
import android.net.NetworkInfo;

/**
 * Created by pw on 2017/9/4.
 */

public interface IConnectivityListener {
    void setActiveNetworkInfo(NetworkInfo networkInfo);

    void setAllNetworks(Network[] networks);
}
