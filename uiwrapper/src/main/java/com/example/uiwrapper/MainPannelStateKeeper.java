package com.example.uiwrapper;

import com.amap.api.maps.model.CameraPosition;

/**
 * Created by pw on 2018/3/26.
 */

public class MainPannelStateKeeper {

    public static UIState sUIState = new UIState();

    public static class UIState {
        CameraPosition cameraPosition;
    }
}
