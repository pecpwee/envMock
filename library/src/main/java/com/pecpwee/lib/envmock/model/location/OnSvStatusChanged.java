package com.pecpwee.lib.envmock.model.location;

import com.pecpwee.lib.envmock.model.AbsTimeModel;

/**
 * Created by pw on 2017/6/8.
 */

public class OnSvStatusChanged extends AbsTimeModel {
    public int svCount;
    public int[] prns;
    public float[] snrs;
    public float[] elevations;
    public float[] azimuths;
    public int ephemerisMask;
    public int almanacMask;
    public int usedInFixMask;

    public OnSvStatusChanged(int svCount, int[] prns, float[] snrs, float[] elevations, float[] azimuths, int ephemerisMask, int almanacMask, int usedInFixMask) {
        super(CONST.ON_SVSTATUS_CHANGED);
        this.svCount = svCount;
        this.prns = prns;
        this.snrs = snrs;
        this.elevations = elevations;
        this.azimuths = azimuths;
        this.ephemerisMask = ephemerisMask;
        this.almanacMask = almanacMask;
        this.usedInFixMask = usedInFixMask;
    }
}
