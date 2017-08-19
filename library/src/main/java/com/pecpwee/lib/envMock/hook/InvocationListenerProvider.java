package com.pecpwee.lib.envMock.hook;

import java.lang.reflect.Method;

/**
 * Created by pw on 2017/6/15.
 */

public interface InvocationListenerProvider {

    void addListener(InvocationListener listener);

    void removeListener(InvocationListener listener);

    void clearListener();


    interface InvocationListener {
        Object onMethodInvoke(Object proxy, Method method, Object[] args);
    }

}
