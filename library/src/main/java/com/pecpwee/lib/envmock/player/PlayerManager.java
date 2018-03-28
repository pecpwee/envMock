package com.pecpwee.lib.envmock.player;

import android.content.Context;
import android.location.Location;

import com.pecpwee.lib.envmock.PlayConfig;
import com.pecpwee.lib.envmock.hook.AbsServiceFetcher;
import com.pecpwee.lib.envmock.hook.CenterServiceManager;
import com.pecpwee.lib.envmock.player.location.GpsPlayer;
import com.pecpwee.lib.envmock.utils.LogUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by pw on 2018/3/24.
 */

public class PlayerManager {


    public static void doStartPlay() {
        long time = System.currentTimeMillis();
        AbsPlayer player = null;
        Set<String> ServiceNameSet = PlayConfig.getInstance().getModuleStateMap().keySet();
        for (String sName : ServiceNameSet) {
            if (PlayConfig.getInstance().getModuleStateMap().get(sName).isPlayEnable) {
                player = CenterServiceManager.getInstance().getServiceFetcher(sName).getPlayer();
                player.startPlay(time);
            }
        }
    }

    public static void doStopPlay() {
        Collection<AbsServiceFetcher> fetchers = CenterServiceManager.getInstance().getInstalledServiceCollection();

        for (AbsServiceFetcher fetcher : fetchers) {
            fetcher.getPlayer().stopPlay();
        }
        resetLastPlayLocation();
    }

    public static synchronized float getPlayingProgress() {
        GpsPlayer gpsPlayer = (GpsPlayer) CenterServiceManager.getInstance().getServiceFetcher(Context.LOCATION_SERVICE).getPlayer();
        if (gpsPlayer == null) {
            throw new RuntimeException("GPS Player module not enable");
        }
        return gpsPlayer.getCurrentPlayProgress();
    }


    public static synchronized List<Location> getCompletePlayPathLocations() {
        GpsPlayer gpsPlayer = (GpsPlayer) CenterServiceManager.getInstance().getServiceFetcher(Context.LOCATION_SERVICE).getPlayer();
        if (gpsPlayer == null) {
            throw new RuntimeException("GPS Player module not enable");
        }
        gpsPlayer.tryLoadData();
        return gpsPlayer.getCompleteGpsPathLocation();
    }

    public static synchronized void resetLastPlayLocation() {
        GpsPlayer gpsPlayer = (GpsPlayer) CenterServiceManager.getInstance().getServiceFetcher(Context.LOCATION_SERVICE).getPlayer();
        gpsPlayer.resetLastPlayedLocation();
    }

    public static synchronized Location getLastPlayLocation() {
        LogUtils.d("getLastPlayLocation");
        GpsPlayer gpsPlayer = (GpsPlayer) CenterServiceManager.getInstance().getServiceFetcher(Context.LOCATION_SERVICE).getPlayer();
        Location location = gpsPlayer.getLastPlayedLocation();
        return location;
    }

}
