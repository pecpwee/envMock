package com.pecpwee.lib.envmock.middle.location.LM;

import com.pecpwee.lib.envmock.middle.location.AbsClassWrapper;
import com.pecpwee.lib.envmock.utils.reflect.FieldUtils;

/**
 * Created by pw on 2017/6/2.
 */


// http://androidxref.com/7.1.1_r6/xref/frameworks/base/location/java/android/location/LocationRequest.java
public class LocationRequestWrapper extends AbsClassWrapper {


    /* ITEM:
    * 144    private int mQuality = POWER_LOW;
145    private long mInterval = 60 * 60 * 1000;   // 60 minutes
146    private long mFastestInterval = (long)(mInterval / FASTEST_INTERVAL_FACTOR);  // 10 minutes
147    private boolean mExplicitFastestInterval = false;
148    private long mExpireAt = Long.MAX_VALUE;  // no expiry
149    private int mNumUpdates = Integer.MAX_VALUE;  // no expiry
150    private float mSmallestDisplacement = 0.0f;    // meters
151    private WorkSource mWorkSource = null;
152    private boolean mHideFromAppOps = false; // True if this request shouldn't be counted by AppOps
154    private String mProvider = LocationManager.FUSED_PROVIDER;  // for deprecated APIs that explicitly request a provider

    *
    *主要是定位的请求参数的合集，比如间隔时间什么的
    * */
    public LocationRequestWrapper(Object baseObj) {
        super(baseObj);
    }

    public String getProvider() {
        try {
            return (String) FieldUtils.readField(baseObj, "mProvider");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
