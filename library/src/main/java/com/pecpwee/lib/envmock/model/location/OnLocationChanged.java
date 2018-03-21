package com.pecpwee.lib.envmock.model.location;

import android.location.Location;

import com.pecpwee.lib.envmock.model.AbsTimeModel;

/**
 * Created by pw on 2017/6/6.
 */

public class OnLocationChanged extends AbsTimeModel {
    private Location location;
    public OnLocationChanged(Location location) {
        super(CONST.ON_LOCATION_CHANGED);
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
