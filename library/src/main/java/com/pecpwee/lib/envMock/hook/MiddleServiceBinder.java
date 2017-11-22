package com.pecpwee.lib.envMock.hook;

import com.pecpwee.lib.envMock.PlayConfig;
import com.pecpwee.lib.envMock.utils.LogUtils;
import com.pecpwee.lib.envMock.utils.reflect.MethodUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pw on 2017/6/15.
 */

public class MiddleServiceBinder implements InvocationHandler, InvocationListenerProvider {

    protected Object origBinder;

    private List<InvocationListener> mListenerList = new ArrayList<>();
    private String mServiceName = null;
    protected Object middleManagerService = null;

    public MiddleServiceBinder(Object origBinder, String serviceName) {
        this.origBinder = origBinder;
        this.mServiceName = serviceName;
        this.middleManagerService = CenterServiceManager
                .getInstance()
                .getServiceFetcher(mServiceName)
                .getMiddleManagerService();

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        for (InvocationListener listener : mListenerList) {
            listener.onMethodInvoke(proxy, method, args);
        }

        if (PlayConfig.getInstance().getModuleStateMap().get(mServiceName).isHookEnable) {
            InvokeReturnObj invokeReturnObj = invokeMiddleService(method, args);
            if (invokeReturnObj.hasInvoked) {
                return invokeReturnObj.returnObj;
            }
        }

        return method.invoke(origBinder, args);
    }

    private InvokeReturnObj invokeMiddleService(Method method, Object[] args) {
        try {
            Object object = MethodUtils.invokeMethod(middleManagerService, method.getName(), args);
            LogUtils.d("method:" + method.getName() + " success");

            return new InvokeReturnObj(true, object);
        } catch (NoSuchMethodException e) {
            LogUtils.d("method:" + method.getName() + " not found");
        } catch (IllegalArgumentException e) {
            LogUtils.d("method:" + method.getName() + " argument error");
        } catch (Throwable t) {
            LogUtils.log(t);
        }
        return new InvokeReturnObj(false, null);
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

    protected static class InvokeReturnObj {
        private boolean hasInvoked;
        private Object returnObj;

        public InvokeReturnObj(boolean hasInvoked, Object returnObj) {
            this.hasInvoked = hasInvoked;
            this.returnObj = returnObj;
        }
    }
}
