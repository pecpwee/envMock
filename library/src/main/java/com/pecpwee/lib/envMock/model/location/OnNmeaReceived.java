package com.pecpwee.lib.envMock.model.location;

import com.pecpwee.lib.envMock.model.AbsTimeModel;

/**
 * Created by pw on 2017/6/7.
 */

public class OnNmeaReceived extends AbsTimeModel {

    private long timestamp;
    private String nmea;

    public OnNmeaReceived(long timestamp, String nmea) {
        super(timestamp, CONST.ON_NMEA_RECEIVED);
        this.timestamp = timestamp;
        this.nmea = nmea;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNmea() {
        return nmea;
    }

    public void setNmea(String nmea) {
        this.nmea = nmea;
    }
}
