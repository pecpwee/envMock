package com.pecpwee.lib.envmock.middle.location;

/**
 * Created by pw on 2017/6/2.
 */

public class AbsClassWrapper {
    protected Object baseObj;

    public AbsClassWrapper(Object baseObj) {
        this.baseObj = baseObj;
    }

    public Object getBaseObj() {
        return baseObj;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AbsClassWrapper) {
            return this.baseObj.equals(((AbsClassWrapper) obj).getBaseObj());
        }
        return false;
    }
}
