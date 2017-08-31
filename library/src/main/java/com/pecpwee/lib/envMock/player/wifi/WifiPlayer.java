package com.pecpwee.lib.envMock.player.wifi;

import android.net.wifi.ScanResult;
import android.os.SystemClock;

import com.pecpwee.lib.envMock.PlayConfig;
import com.pecpwee.lib.envMock.middle.wifi.IWifiPlayerListener;
import com.pecpwee.lib.envMock.model.wifi.CONST;
import com.pecpwee.lib.envMock.model.wifi.GetConnectionInfo;
import com.pecpwee.lib.envMock.model.wifi.GetScanResult;
import com.pecpwee.lib.envMock.player.AbsPlayer;
import com.pecpwee.lib.envMock.utils.GsonFactory;

import java.util.List;

/**
 * Created by pw on 2017/6/12.
 */

public class WifiPlayer extends AbsPlayer<IWifiPlayerListener> {


    public WifiPlayer(IWifiPlayerListener listenr) {
        super(listenr);
    }


    @Override
    public String getDefaultFilePath() {
        return PlayConfig.DEFAULT_FILEPATH_WIFI;
    }

    @Override
    public String getConfigFilePath() {
        return PlayConfig.getInstance().getWifiRecordFilePath();
    }

    @Override
    protected ILineDataParser getParser() {
        return new AbsParser() {
            @Override
            protected void parseValue(int type, String line) {
                switch (type) {
                    case CONST.GET_SCAN_RESULT:


                        final GetScanResult getScanResultObj = GsonFactory.getGson().fromJson(line, GetScanResult.class);
                        addTimedAction(new TimedRunnable(getScanResultObj.getTime()) {
                            @Override
                            public void run() {
                                List<ScanResult> scanResults = getScanResultObj.getScanResults();

                                for (ScanResult sc : scanResults) {
                                    sc.timestamp = SystemClock.elapsedRealtime() * 1000L;//mock scan time
                                }
                                mListener.setScanWifiList(getScanResultObj.getScanResults());
                            }
                        });


                        break;
                    case CONST.GET_CONNECTION_INFO:
                        final GetConnectionInfo getConnectionInfoObj = GsonFactory.getGson().fromJson(line, GetConnectionInfo.class);
                        addTimedAction(new TimedRunnable(getConnectionInfoObj.getTime()) {
                            @Override
                            public void run() {
                                mListener.setConnectionWifiInfo(getConnectionInfoObj.getWifiInfo());
                            }
                        });
                }
            }
        };
    }

}
