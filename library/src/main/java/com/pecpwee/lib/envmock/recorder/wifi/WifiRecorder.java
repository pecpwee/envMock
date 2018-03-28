package com.pecpwee.lib.envmock.recorder.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.pecpwee.lib.envmock.AbsConfig;
import com.pecpwee.lib.envmock.RecordConfig;
import com.pecpwee.lib.envmock.hook.CenterServiceManager;
import com.pecpwee.lib.envmock.model.wifi.GetConnectionInfo;
import com.pecpwee.lib.envmock.model.wifi.GetScanResult;
import com.pecpwee.lib.envmock.recorder.AbsRecorder;
import com.pecpwee.lib.envmock.utils.LogUtils;
import com.pecpwee.lib.envmock.utils.TimerJob;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by pw on 2017/6/12.
 */

public class WifiRecorder extends AbsRecorder {
    private TimerJob wifiTimedJob = new TimerJob();
    private WifiManager wifiManager;

    public WifiRecorder() {
        this.wifiManager = (WifiManager) CenterServiceManager.getInstance().getServiceFetcher(Context.WIFI_SERVICE).getOrigManagerObj();
        wifiTimedJob = new TimerJob()
                .setInterval(RecordConfig.getInstance().getSampleInterval())
                .setRunnable(new Runnable() {
                    @Override
                    public void run() {
                        wifiManager.startScan();
                        doRecord(new GetScanResult(filterList(wifiManager.getScanResults())));
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        doRecord(new GetConnectionInfo(wifiInfo));
                    }
                });

    }


    private List<ScanResult> filterList(List<ScanResult> list) {

        Collections.sort(list, new Comparator<ScanResult>() {
            public int compare(ScanResult o1, ScanResult o2) {
                if (o1.level < o2.level) {
                    return 1;
                }
                if (o1.level > o2.level) {
                    return -1;
                }
                return 0;
            }
        });
        while (list.size() > RecordConfig.getInstance().getWifiScanCountUpperLimit()) {
            list.remove(list.size() - 1);
        }
        return list;
    }

    @Override
    public void onStart() {
        wifiTimedJob.setInterval(RecordConfig.getInstance().getSampleInterval());
        wifiTimedJob.start();

    }

    @Override
    public void onStop() {
        wifiTimedJob.stop();

    }



    @Override
    public String getDefaultFilePath() {
        return AbsConfig.DEFAULT_FILEPATH_WIFI;
    }
}
