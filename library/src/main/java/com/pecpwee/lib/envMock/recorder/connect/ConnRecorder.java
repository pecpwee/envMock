package com.pecpwee.lib.envMock.recorder.connect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

import com.pecpwee.lib.envMock.AbsConfig;
import com.pecpwee.lib.envMock.RecordConfig;
import com.pecpwee.lib.envMock.hook.CenterServiceManager;
import com.pecpwee.lib.envMock.model.connect.GetActiveNetwork;
import com.pecpwee.lib.envMock.model.connect.GetAllNetworks;
import com.pecpwee.lib.envMock.recorder.AbsRecorder;
import com.pecpwee.lib.envMock.utils.TimerJob;

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

                        doRecord(new GetActiveNetwork(connectivityManager.getActiveNetworkInfo()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            doRecord(new GetAllNetworks(connectivityManager.getAllNetworks()));
                        }
                    }
                });


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
