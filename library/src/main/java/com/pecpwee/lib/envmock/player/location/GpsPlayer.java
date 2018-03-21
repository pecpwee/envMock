package com.pecpwee.lib.envmock.player.location;

import android.location.Location;
import android.os.Bundle;

import com.pecpwee.lib.envmock.PlayConfig;
import com.pecpwee.lib.envmock.middle.location.IGpsPlayerListener;
import com.pecpwee.lib.envmock.model.AbsTimeModel;
import com.pecpwee.lib.envmock.model.location.OnFirstFix;
import com.pecpwee.lib.envmock.model.location.OnLocationChanged;
import com.pecpwee.lib.envmock.model.location.OnNmeaReceived;
import com.pecpwee.lib.envmock.model.location.OnProviderDisable;
import com.pecpwee.lib.envmock.model.location.OnProviderEnabled;
import com.pecpwee.lib.envmock.model.location.OnStatusChanged;
import com.pecpwee.lib.envmock.model.location.OnSvStatusChanged;
import com.pecpwee.lib.envmock.player.AbsPlayer;
import com.pecpwee.lib.envmock.utils.GsonFactory;
import com.pecpwee.lib.envmock.utils.LogUtils;

import static com.pecpwee.lib.envmock.model.location.CONST.ON_FIRST_FIX;
import static com.pecpwee.lib.envmock.model.location.CONST.ON_GPS_START;
import static com.pecpwee.lib.envmock.model.location.CONST.ON_GPS_STOPPED;
import static com.pecpwee.lib.envmock.model.location.CONST.ON_LOCATION_CHANGED;
import static com.pecpwee.lib.envmock.model.location.CONST.ON_NMEA_RECEIVED;
import static com.pecpwee.lib.envmock.model.location.CONST.ON_PROVIDER_DISABLE;
import static com.pecpwee.lib.envmock.model.location.CONST.ON_PROVIDER_ENABLE;
import static com.pecpwee.lib.envmock.model.location.CONST.ON_STATUS_CHANGED;
import static com.pecpwee.lib.envmock.model.location.CONST.ON_SVSTATUS_CHANGED;

/**
 * Created by pw on 2017/6/2.
 */

public class GpsPlayer extends AbsPlayer<IGpsPlayerListener> {


    public GpsPlayer(IGpsPlayerListener listener) {
        super(listener);
    }

    @Override
    public String getDefaultFilePath() {
        return PlayConfig.DEFAULT_FILEPATH_GPS;
    }

    @Override
    public String getConfigFilePath() {
        return PlayConfig.getInstance().getGpsRecordFilePath();
    }

    @Override
    protected ILineDataParser getParser() {
        return new Parser();
    }

    class Parser extends AbsParser {

        @Override
        protected void parseValue(int type, String line) {
            switch (type) {
                case ON_LOCATION_CHANGED: {
                    OnLocationChanged obj = GsonFactory.getGson().fromJson(line, OnLocationChanged.class);
                    final Location location = obj.getLocation();
                    addTimedAction(new TimedRunnable(obj.getTime()) {
                        @Override
                        public void run() {
                            LogUtils.d("onLocationChanged");
                            mListener.onLocationChanged(location);
                        }
                    });
                    break;
                }
                case ON_NMEA_RECEIVED: {
                    final OnNmeaReceived nmeaParam = GsonFactory.getGson().fromJson(line, OnNmeaReceived.class);
                    addTimedAction(new TimedRunnable(nmeaParam.getTime()) {
                        @Override
                        public void run() {
                            mListener.onNmeaReceived(nmeaParam.getTime(), nmeaParam.getNmea());
                        }
                    });
                    break;
                }
                case ON_STATUS_CHANGED:
                    final OnStatusChanged statusChangedParam = GsonFactory.getGson().fromJson(line, OnStatusChanged.class);
                    Bundle b = statusChangedParam.getExtras();
                    long time = statusChangedParam.getTime();
                    addTimedAction(new TimedRunnable(statusChangedParam.getTime()) {
                        @Override
                        public void run() {
                            mListener.onStatusChanged(statusChangedParam.getProvider()
                                    , statusChangedParam.getStatus()
                                    , statusChangedParam.getExtras());
                        }
                    });
                    break;
                case ON_PROVIDER_ENABLE: {
                    final OnProviderEnabled model = GsonFactory.getGson().fromJson(line, OnProviderEnabled.class);

                    addTimedAction(new TimedRunnable(model.getTime()) {
                        @Override
                        public void run() {
                            mListener.onProviderEnabled(model.getProviderName());
                        }
                    });
                    break;
                }
                case ON_PROVIDER_DISABLE: {

                    final OnProviderDisable model = GsonFactory.getGson().fromJson(line, OnProviderDisable.class);
                    addTimedAction(new TimedRunnable(model.getTime()) {
                        @Override
                        public void run() {
                            mListener.onProviderDisabled(model.getProviderName());
                        }
                    });

                    break;
                }
                case ON_FIRST_FIX: {
                    final OnFirstFix firstfix = GsonFactory.getGson().fromJson(line, OnFirstFix.class);

                    addTimedAction(new TimedRunnable(firstfix.getTime()) {
                        @Override
                        public void run() {
                            mListener.onFirstFix(firstfix.getFtt());
                        }
                    });
                    break;
                }
                case ON_GPS_START: {

                    final AbsTimeModel model = GsonFactory.getGson().fromJson(line, AbsTimeModel.class);

                    addTimedAction(new TimedRunnable(model.getTime()) {
                        @Override
                        public void run() {
                            mListener.onGpsStarted();
                        }
                    });
                    break;
                }
                case ON_GPS_STOPPED: {
                    final AbsTimeModel model = GsonFactory.getGson().fromJson(line, AbsTimeModel.class);

                    addTimedAction(new TimedRunnable(model.getTime()) {
                        @Override
                        public void run() {
                            mListener.onGpsStopped();
                        }
                    });
                    break;
                }
                case ON_SVSTATUS_CHANGED: {

                    final OnSvStatusChanged onSvStatusChanged = GsonFactory.getGson().fromJson(line, OnSvStatusChanged.class);

                    addTimedAction(new TimedRunnable(onSvStatusChanged.getTime()) {
                        @Override
                        public void run() {
                            mListener.onSvStatusChanged(
                                    onSvStatusChanged.svCount
                                    , onSvStatusChanged.prns
                                    , onSvStatusChanged.snrs
                                    , onSvStatusChanged.elevations
                                    , onSvStatusChanged.azimuths
                                    , onSvStatusChanged.ephemerisMask
                                    , onSvStatusChanged.almanacMask
                                    , onSvStatusChanged.usedInFixMask
                            );
                        }
                    });
                    break;
                }
            }
        }
    }


}
