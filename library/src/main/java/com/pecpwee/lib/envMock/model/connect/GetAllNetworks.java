package com.pecpwee.lib.envMock.model.connect;

import android.net.Network;

import com.pecpwee.lib.envMock.model.AbsObjWrapperModel;

/**
 * Created by pw on 2017/9/4.
 */

public class GetAllNetworks extends AbsObjWrapperModel<Network[]> {
    public GetAllNetworks(Network[] obj) {
        super(CONST.GetAllNetworks, obj);
    }
}
