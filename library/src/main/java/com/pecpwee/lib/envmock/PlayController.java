package com.pecpwee.lib.envmock;

import android.content.Context;
import android.location.Location;

import com.pecpwee.lib.envmock.hook.CenterServiceManager;
import com.pecpwee.lib.envmock.player.PlayerManager;
import com.pecpwee.lib.envmock.utils.LogUtils;

import java.util.List;

/**
 * Created by pw on 2017/8/17.
 */

public class PlayController {

    private boolean isPlaying = false;

    public static PlayController INSTANCE;

    private PlayController() {
    }

    public synchronized static PlayController getInstance() {
        if (!EnvMockInstaller.isPlayServiceInstalled) {
            throw new RuntimeException("you should installed the envMock service first");
        }
        if (PlayConfig.getInstance().isAutoPlayMode()) {
            throw new RuntimeException("in the auto mode" +
                    ",you should not create PlayController that used for manual control." +
                    "please config before install the service");
        }

        if (INSTANCE == null) {
            INSTANCE = new PlayController();
        }
        return INSTANCE;

    }

    public float getPlayingProgress() {
        try {
            return PlayerManager.getPlayingProgress();
        } catch (Exception e) {
            LogUtils.log(e);
        }
        return -1;
    }

    public Location getLastPlayedLocation() {
        return PlayerManager.getLastPlayLocation();
    }

    public boolean isPlaying() {
        return isPlaying;
    }


    public synchronized List<Location> getCompletePlayPathLocations() {
        return PlayerManager.getCompletePlayPathLocations();
    }

    public synchronized void startPlay() {
        if (isPlaying) {
            throw new RuntimeException("you are playing!you should stop the replay before you call play method again");
        }
        ensureIsManualMode();

        long time = System.currentTimeMillis();

        PlayerManager.doStartPlay();
        isPlaying = true;
    }

    public synchronized void stopPlay() {

        ensureIsManualMode();

        if (!isPlaying) {
            throw new RuntimeException("there is no module is playing!");
        }
        PlayerManager.doStopPlay();
        isPlaying = false;

    }

    //between 0 to 1.
    public synchronized void setBeginOffsetPercent(float percentOffset) {
        ensureNoPlaying();
        ensureIsManualMode();
        PlayConfig.getInstance().setStartPlayPercentage(percentOffset);
    }


    public PlayController setWifiPlayerEnable(boolean isEnable) {
        setPlayEnableState(Context.WIFI_SERVICE, isEnable);
        return this;
    }

    public PlayController setGpsPlayerEnable(boolean isEnable) {
        setPlayEnableState(Context.LOCATION_SERVICE, isEnable);
        return this;
    }

    public PlayController setConnPlayerEnable(boolean isEnable) {
        setPlayEnableState(Context.CONNECTIVITY_SERVICE, isEnable);
        return this;
    }

    public PlayController setCellPlayerEnable(boolean isEnable) {
        setPlayEnableState(Context.TELEPHONY_SERVICE, isEnable);
        return this;
    }

    private void setPlayEnableState(String serviceName, boolean state) {
        ensureNoPlaying();
        ensureIsManualMode();
        PlayConfig.getInstance().getModuleStateMap().get(serviceName).isPlayEnable = state;
    }


    public PlayController setWifiHookEnable(boolean isEnable) {
        setHookEnableState(Context.WIFI_SERVICE, isEnable);
        return this;
    }

    public PlayController setGpsHookEnable(boolean isEnable) {
        setHookEnableState(Context.LOCATION_SERVICE, isEnable);
        return this;
    }

    public PlayController setConnHookEnable(boolean isEnable) {
        setHookEnableState(Context.CONNECTIVITY_SERVICE, isEnable);
        return this;
    }

    public PlayController setCellHookEnable(boolean isEnable) {
        setHookEnableState(Context.TELEPHONY_SERVICE, isEnable);
        return this;
    }

    private void setHookEnableState(String serviceName, boolean state) {
        ensureNoPlaying();
        ensureIsManualMode();
        PlayConfig.getInstance().getModuleStateMap().get(serviceName).isHookEnable = state;
    }


    public PlayController setGpsMockFilePath(String filepath) {
        ensureNoPlaying();
        ensureIsManualMode();
        PlayConfig.getInstance().setGpsRecordFilePath(filepath);
        CenterServiceManager.getInstance().getServiceFetcher(Context.LOCATION_SERVICE)
                .getPlayer().notifyDataSourceChanged();
        return this;

    }

    public PlayController setWifiMockFilePath(String filepath) {
        ensureNoPlaying();
        ensureIsManualMode();
        PlayConfig.getInstance().setWifiRecordFilePath(filepath);
        CenterServiceManager.getInstance().getServiceFetcher(Context.WIFI_SERVICE)
                .getPlayer().notifyDataSourceChanged();
        return this;

    }

    public PlayController setCellMockFilePath(String filepath) {
        ensureNoPlaying();
        ensureIsManualMode();
        PlayConfig.getInstance().setCellRecordFilePath(filepath);
        CenterServiceManager.getInstance().getServiceFetcher(Context.TELEPHONY_SERVICE)
                .getPlayer().notifyDataSourceChanged();

        return this;
    }

    private void ensureNoPlaying() {
        if (isPlaying) {
            throw new RuntimeException("you should stop the playing before setting ");
        }
    }

    private void ensureIsManualMode() {
        if (PlayConfig.getInstance().isAutoMode()) {
            throw new RuntimeException("you should not in auto mode ");
        }
    }
}

