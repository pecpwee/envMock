package com.pecpwee.lib.envMock;

import android.content.Context;

import com.pecpwee.lib.envMock.hook.CenterServiceManager;
import com.pecpwee.lib.envMock.utils.reflect.PlayUtils;

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

    public synchronized void startPlay() {
        if (isPlaying) {
            throw new RuntimeException("you are playing!you should stop the replay before you call play method again");
        }
        ensureIsManualMode();

        long time = System.currentTimeMillis();

        PlayUtils.doStartPlay();
        isPlaying = true;
    }

    public synchronized void stopPlay() {

        ensureIsManualMode();

        if (!isPlaying) {
            throw new RuntimeException("there is no module is playing!");
        }
        PlayUtils.doStopPlay();
        isPlaying = false;
    }

    //between 0 to 1.
    public synchronized void setBeginOffsetPercent(float percentOffset) {
        ensureNoPlaying();
        ensureIsManualMode();
        PlayConfig.getInstance().setStartPlayPercentage(percentOffset);
    }


    public PlayController setWifiPlayerEnable(boolean isEnable) {
        ensureNoPlaying();
        ensureIsManualMode();
        PlayConfig.getInstance().getModuleStateMap().put(Context.WIFI_SERVICE, isEnable);
        return this;
    }

    public PlayController setGpsPlayerEnable(boolean isEnable) {
        ensureNoPlaying();
        ensureIsManualMode();
        PlayConfig.getInstance().getModuleStateMap().put(Context.LOCATION_SERVICE, isEnable);
        return this;

    }

    public PlayController setCellPlayerEnable(boolean isEnable) {
        ensureNoPlaying();
        ensureIsManualMode();
        PlayConfig.getInstance().getModuleStateMap().put(Context.TELEPHONY_SERVICE, isEnable);
        return this;

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

