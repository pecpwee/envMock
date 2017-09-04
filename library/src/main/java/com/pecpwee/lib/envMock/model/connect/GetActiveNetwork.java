package com.pecpwee.lib.envMock.model.connect;

import android.net.NetworkInfo;

import com.pecpwee.lib.envMock.model.AbsObjWrapperModel;

/**
 * Created by pw on 2017/9/4.
 */

public class GetActiveNetwork extends AbsObjWrapperModel<NetworkInfo> {

    public GetActiveNetwork(NetworkInfo networkInfo) {
        super(CONST.GetActiveNetwork, networkInfo);
    }

}
