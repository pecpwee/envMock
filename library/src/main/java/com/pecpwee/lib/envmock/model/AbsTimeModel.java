package com.pecpwee.lib.envmock.model;

/**
 * Created by pw on 2017/6/7.
 */

public class AbsTimeModel {
    private long time;
    private int type;

    public AbsTimeModel(long time, int type) {
        this.time = time;
        this.type = type;
    }

    public AbsTimeModel(int type) {
        this.time = System.currentTimeMillis();
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
