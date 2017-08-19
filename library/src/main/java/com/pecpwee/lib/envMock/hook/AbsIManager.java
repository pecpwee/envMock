package com.pecpwee.lib.envMock.hook;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pw on 2017/6/15.
 */

public abstract class AbsIManager implements InvocationHandler, InvocationListenerProvider {

    protected Object interfaceBinder;

    public List<InvocationListener> mListenerList = new ArrayList<>();

    public AbsIManager(Object interfaceBinder) {
        this.interfaceBinder = interfaceBinder;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        for (InvocationListener listener : mListenerList) {
            listener.onMethodInvoke(proxy, method, args);
        }
        return onInvoke(proxy, method, args);
    }


    @Override
    public final void addListener(InvocationListener listener) {
        mListenerList.add(listener);
    }

    @Override
    public final void removeListener(InvocationListener listener) {
        mListenerList.remove(listener);
    }

    @Override
    public void clearListener() {
        mListenerList.clear();
    }

    public abstract Object onInvoke(Object proxy, Method method, Object[] args) throws Throwable;

}