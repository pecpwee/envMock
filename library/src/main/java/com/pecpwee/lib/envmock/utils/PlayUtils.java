package com.pecpwee.lib.envmock.utils;

import com.pecpwee.lib.envmock.PlayConfig;
import com.pecpwee.lib.envmock.hook.AbsServiceFetcher;
import com.pecpwee.lib.envmock.hook.CenterServiceManager;
import com.pecpwee.lib.envmock.player.AbsPlayer;

import java.util.Collection;
import java.util.Set;

/**
 * Created by pw on 2017/8/21.
 */

public class PlayUtils {

    public static void doStartPlay(){
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

    public static void doStopPlay(){
        Collection<AbsServiceFetcher> fetchers = CenterServiceManager.getInstance().getInstalledServiceCollection();

        for (AbsServiceFetcher fetcher : fetchers) {
            fetcher.getPlayer().stopPlay();
        }
    }
}
