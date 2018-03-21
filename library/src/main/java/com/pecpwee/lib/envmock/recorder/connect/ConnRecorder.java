package com.pecpwee.lib.envmock.recorder.connect;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import com.pecpwee.lib.envmock.AbsConfig;
import com.pecpwee.lib.envmock.RecordConfig;
import com.pecpwee.lib.envmock.hook.CenterServiceManager;
import com.pecpwee.lib.envmock.model.connect.GetActiveNetwork;
import com.pecpwee.lib.envmock.model.connect.GetAllNetworks;
import com.pecpwee.lib.envmock.recorder.AbsRecorder;
import com.pecpwee.lib.envmock.utils.TimerJob;

/**
 * Created by pw on 2017/9/4.
 */

public class ConnRecorder extends AbsRecorder {

    private ConnectivityManager connectivityManager;
    private TimerJob connTimerJob;

    public ConnRecorder() {
        connectivityManager = (ConnectivityManager) CenterServiceManager.getInstance().getServiceFetcher(Context.CONNECTIVITY_SERVICE).getOrigManagerObj();
        connTimerJob = new TimerJob()
                .setInterval(RecordConfig.getInstance().getSampleInterval())
                .setRunnable(new Runnable() {
                    @Override
                    public void run() {

                        Network activeNetwork = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            activeNetwork = connectivityManager.getActiveNetwork();
                        }

                        doRecord(new GetActiveNetwork(activeNetwork
                                , connectivityManager.getActiveNetworkInfo()));


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            Network[] networks = connectivityManager.getAllNetworks();
                            NetworkInfo[] networkinfos = getNetworkInfoArray(networks);
                            NetworkCapabilities[] capacities = getNetworkCapabilitiesArray(networks);
                            doRecord(new GetAllNetworks(networks, networkinfos, capacities));
                        }
                    }
                });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NetworkCapabilities[] getNetworkCapabilitiesArray(Network[] networks) {

        NetworkCapabilities[] results = new NetworkCapabilities[networks.length];
        for (int i = 0; i < networks.length; i++) {
            results[i] = connectivityManager.getNetworkCapabilities(networks[i]);
        }
        return results;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NetworkInfo[] getNetworkInfoArray(Network[] networks) {

        NetworkInfo[] results = new NetworkInfo[networks.length];
        for (int i = 0; i < networks.length; i++) {
            results[i] = connectivityManager.getNetworkInfo(networks[i]);
        }
        return results;
    }

    @Override
    public void onStart() {
        connTimerJob.setInterval(RecordConfig.getInstance().getSampleInterval());
        connTimerJob.start();

    }

    @Override
    public void onStop() {
        connTimerJob.stop();

    }

    @Override
    public String getDefaultFilePath() {
        return AbsConfig.DEFAULT_FILEPATH_CONN;
    }
}
