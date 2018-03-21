package com.pecpwee.lib.envmock.middle.location.LM;

/**
 * Created by pw on 2017/6/6.
 */

public class RequestLocationWrapper {
    LocationRequestWrapper locationRequestWrapper;
    ListenerTransportWrapper listenerTransportWrapper;

    public RequestLocationWrapper(LocationRequestWrapper locationRequestWrapper, ListenerTransportWrapper listenerTransportWrapper) {
        this.locationRequestWrapper = locationRequestWrapper;
        this.listenerTransportWrapper = listenerTransportWrapper;
    }

    public LocationRequestWrapper getLocationRequestWrapper() {
        return locationRequestWrapper;
    }

    public void setLocationRequestWrapper(LocationRequestWrapper locationRequestWrapper) {
        this.locationRequestWrapper = locationRequestWrapper;
    }

    public ListenerTransportWrapper getListenerTransportWrapper() {
        return listenerTransportWrapper;
    }

    public void setListenerTransportWrapper(ListenerTransportWrapper listenerTransportWrapper) {
        this.listenerTransportWrapper = listenerTransportWrapper;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RequestLocationWrapper) {
            return ((RequestLocationWrapper) obj).listenerTransportWrapper.equals(this.listenerTransportWrapper);
        }
        return false;
    }
}
