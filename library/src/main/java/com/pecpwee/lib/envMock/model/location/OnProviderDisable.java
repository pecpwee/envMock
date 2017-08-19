package com.pecpwee.lib.envMock.model.location;

import com.pecpwee.lib.envMock.model.AbsTimeModel;

/**
 * Created by pw on 2017/6/9.
 */

public class OnProviderDisable extends AbsTimeModel {
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    String providerName;

    public OnProviderDisable(String providerName) {
        super(CONST.ON_PROVIDER_DISABLE);
        this.providerName = providerName;
    }
}
