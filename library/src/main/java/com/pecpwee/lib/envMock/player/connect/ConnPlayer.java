package com.pecpwee.lib.envMock.player.connect;

import com.pecpwee.lib.envMock.PlayConfig;
import com.pecpwee.lib.envMock.middle.connect.IConnectivityListener;
import com.pecpwee.lib.envMock.model.connect.CONST;
import com.pecpwee.lib.envMock.model.connect.GetActiveNetwork;
import com.pecpwee.lib.envMock.model.connect.GetAllNetworks;
import com.pecpwee.lib.envMock.player.AbsPlayer;
import com.pecpwee.lib.envMock.utils.GsonFactory;

/**
 * Created by pw on 2017/9/4.
 */

public class ConnPlayer extends AbsPlayer<IConnectivityListener> {
    public ConnPlayer(IConnectivityListener listener) {
        super(listener);
    }

    @Override
    public String getDefaultFilePath() {
        return PlayConfig.DEFAULT_FILEPATH_CONN;
    }

    @Override
    public String getConfigFilePath() {
        return PlayConfig.getInstance().getConnRecordFilePath();
    }

    @Override
    protected ILineDataParser getParser() {
        return new AbsParser() {
            @Override
            protected void parseValue(int type, String line) {
                switch (type) {
                    case CONST.GetActiveNetwork: {
                        final GetActiveNetwork getActiveNetwork = GsonFactory.getGson().fromJson(line, GetActiveNetwork.class);
                        addTimedAction(new TimedRunnable(getActiveNetwork.getTime()) {
                            @Override
                            public void run() {
                                mListener.setActiveNetwork(getActiveNetwork.getNetwork(),getActiveNetwork.getNetworkInfo());
                            }
                        });
                        break;
                    }

                    case CONST.GetAllNetworks: {
                        final GetAllNetworks getAllNetworks = GsonFactory.getGson().fromJson(line, GetAllNetworks.class);
                        addTimedAction(new TimedRunnable(getAllNetworks.getTime()) {
                            @Override
                            public void run() {
                                mListener.setAllNetworks(getAllNetworks.getAllNetworks(), getAllNetworks.getNetworkInfos(), getAllNetworks.getNetworkCapabilities());
                            }
                        });
                        break;
                    }
                }

            }
        };
    }
}
