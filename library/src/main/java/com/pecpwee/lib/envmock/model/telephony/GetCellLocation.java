package com.pecpwee.lib.envmock.model.telephony;

import android.os.Bundle;

import com.pecpwee.lib.envmock.model.AbsTimeModel;

/**
 * Created by pw on 2017/6/16.
 */

public class GetCellLocation extends AbsTimeModel {
    private Bundle bundle;

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }


    public GetCellLocation(Bundle bundle) {

        super(CONST.GET_CELL_LOCATION);
        this.bundle = bundle;
    }
}
