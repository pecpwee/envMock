package com.pecpwee.lib.envMock.hook;

import com.pecpwee.lib.envMock.PlayConfig;

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
    public String mServiceName = null;

    public AbsIManager(Object interfaceBinder, String serviceName) {
        this.interfaceBinder = interfaceBinder;
        this.mServiceName = serviceName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        for (InvocationListener listener : mListenerList) {
            listener.onMethodInvoke(proxy, method, args);
        }

        if (PlayConfig.getInstance().getModuleStateMap().get(mServiceName).isHookEnable) {
            InvokeReturnObj invokeReturnObj = onInvoke(proxy, method, args);
            if (invokeReturnObj.hasInvoked){
                return invokeReturnObj.returnObj;
            }
        }

        return method.invoke(interfaceBinder, args);


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

    public abstract InvokeReturnObj onInvoke(Object proxy, Method method, Object[] args) throws Throwable;

    protected static class InvokeReturnObj {
        private boolean hasInvoked;
        private Object returnObj;

        public InvokeReturnObj(boolean hasInvoked, Object returnObj) {
            this.hasInvoked = hasInvoked;
            this.returnObj = returnObj;
        }
    }
}
