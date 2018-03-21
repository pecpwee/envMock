package com.pecpwee.lib.envmock.model;

/**
 * Created by pw on 2017/9/4.
 */

public class AbsObjWrapperModel<T> extends AbsTimeModel {
    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    private T object;

    public AbsObjWrapperModel(long time, int type) {
        super(time, type);
    }

    public AbsObjWrapperModel(int type) {
        super(type);
    }

    public AbsObjWrapperModel(int type, T object) {
        this(type);
        this.object = object;
    }

    public AbsObjWrapperModel(long time, int type, T object) {
        this(time, type);
        this.object = object;
    }

}
