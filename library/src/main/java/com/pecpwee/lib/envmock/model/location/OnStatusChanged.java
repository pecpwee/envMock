package com.pecpwee.lib.envmock.model.location;

import android.os.Bundle;

import com.pecpwee.lib.envmock.model.AbsTimeModel;

/**
 * Created by pw on 2017/6/7.
 */

public class OnStatusChanged extends AbsTimeModel {
    String provider;
    int status;
    Bundle extras;

    public OnStatusChanged(String provider, int status, Bundle extras) {
        super(CONST.ON_STATUS_CHANGED);
        this.provider = provider;
        this.status = status;
        this.extras = extras;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Bundle getExtras() {
        return extras;
    }

    public void setExtras(Bundle extras) {
        this.extras = extras;
    }
}
