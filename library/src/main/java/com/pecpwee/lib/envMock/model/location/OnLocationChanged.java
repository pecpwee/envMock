package com.pecpwee.lib.envMock.model.location;

import android.location.Location;

import com.pecpwee.lib.envMock.model.AbsTimeModel;

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
