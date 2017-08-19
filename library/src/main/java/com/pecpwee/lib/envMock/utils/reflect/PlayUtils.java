package com.pecpwee.lib.envMock.utils.reflect;

import com.pecpwee.lib.envMock.PlayConfig;
import com.pecpwee.lib.envMock.hook.AbsServiceFetcher;
import com.pecpwee.lib.envMock.hook.CenterServiceManager;
import com.pecpwee.lib.envMock.player.AbsPlayer;

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
            if (PlayConfig.getInstance().getModuleStateMap().get(sName)) {
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
