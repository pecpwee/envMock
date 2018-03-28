package com.example.uiwrapper;

import android.content.Context;
import android.location.Location;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.pecpwee.lib.envmock.PlayController;
import com.pecpwee.lib.envmock.utils.LogUtils;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pw on 2018/3/24.
 */

public class MainPannelRepository {

    private CoordinateConverter coordinateConverter;

    public MainPannelRepository(Context context) {
        coordinateConverter = new CoordinateConverter(context.getApplicationContext());
        coordinateConverter.from(CoordinateConverter.CoordType.GPS);
    }

    public LatLng convert2AmapLoc(Location rawLocation) {
        LatLng latLng = new LatLng(rawLocation.getLatitude(), rawLocation.getLongitude());
        return coordinateConverter.coord(latLng).convert();
    }

    public List<LatLng> getPathList() {
        List<Location> locationList = null;
        try {
            locationList = PlayController.getInstance().getCompletePlayPathLocations();
        } catch (FileNotFoundException e) {
        }
        if (locationList == null){
            return null;
        }

        LogUtils.d("getPathList:" + locationList.size());
        List<LatLng> latLngs = new ArrayList();
        for (Location location : locationList) {
            latLngs.add(convert2AmapLoc(location));
        }
        return latLngs;
    }

}
