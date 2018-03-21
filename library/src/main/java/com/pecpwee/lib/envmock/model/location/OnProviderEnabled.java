package com.pecpwee.lib.envmock.model.location;

import com.pecpwee.lib.envmock.model.AbsTimeModel;

/**
 * Created by pw on 2017/6/9.
 */

public class OnProviderEnabled extends AbsTimeModel {
    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    String providerName;

    public OnProviderEnabled(String providerName) {
        super(CONST.ON_PROVIDER_ENABLE);
        this.providerName = providerName;
    }
}
