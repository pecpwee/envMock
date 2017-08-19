package com.pecpwee.lib.envMock.player.telephony;

import com.pecpwee.lib.envMock.PlayConfig;
import com.pecpwee.lib.envMock.middle.telephony.ITelephonyPlayerListener;
import com.pecpwee.lib.envMock.model.telephony.CONST;
import com.pecpwee.lib.envMock.model.telephony.GetCellLocation;
import com.pecpwee.lib.envMock.model.telephony.GetNetworkType;
import com.pecpwee.lib.envMock.player.AbsPlayer;
import com.pecpwee.lib.envMock.utils.GsonFactory;
import com.pecpwee.lib.envMock.utils.TimerJob;

/**
 * Created by pw on 2017/6/15.
 */

public class TelephonyPlayer extends AbsPlayer<ITelephonyPlayerListener> {

    private TimerJob wifiTimer = new TimerJob();

    public TelephonyPlayer(ITelephonyPlayerListener listener) {
        super(listener);
    }

    @Override
    public String getDefaultFilePath() {
        return PlayConfig.DEFAULT_FILEPATH_CELL;
    }

    @Override
    public String getConfigFilePath() {
        return PlayConfig.getInstance().getCellRecordFilePath();
    }

    @Override
    protected ILineDataParser getParser() {
        return new AbsParser() {
            @Override
            protected void parseValue(int type, String line) {
                switch (type) {
                    case CONST.GET_CELL_LOCATION: {
                        final GetCellLocation getCellLocationObj = GsonFactory.getGson().fromJson(line, GetCellLocation.class);
                        addTimedAction(new TimedRunnable(getCellLocationObj.getTime()) {
                            @Override
                            public void run() {
                                mListener.setCellLocation(getCellLocationObj.getBundle());
                            }
                        });
                        break;
                    }

                    case CONST.GET_NETWORK_TYPE: {
                        final GetNetworkType networkType = GsonFactory.getGson().fromJson(line, GetNetworkType.class);
                        addTimedAction(new TimedRunnable(networkType.getTime()) {
                            @Override
                            public void run() {
                                mListener.setNetworkType(networkType.getNetworkType());
                            }
                        });
                        break;
                    }
                }
            }
        };
    }
}
