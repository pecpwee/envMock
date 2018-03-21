package com.pecpwee.lib.envmock.model.location;

import com.pecpwee.lib.envmock.model.AbsTimeModel;

/**
 * Created by pw on 2017/6/9.
 */

public class OnFirstFix extends AbsTimeModel {

    private int ftt;

    public OnFirstFix(int type) {
        super(CONST.ON_FIRST_FIX);
    }

    public int getFtt() {
        return ftt;
    }

    public void setFtt(int ftt) {
        this.ftt = ftt;
    }
}
