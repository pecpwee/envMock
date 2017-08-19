package com.pecpwee.lib.envMock.hook;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by pw on 2017/6/14.
 */

public class ProxyServiceManagerDispatcherBinder implements InvocationHandler {

    private String mServiceName;

    public ProxyServiceManagerDispatcherBinder(String serviceName) {
        this.mServiceName = serviceName;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();

        if ("queryLocalInterface".equals(methodName)) {
            return CenterServiceManager.getInstance().getServiceFetcher(mServiceName).getProxyServiceBinder();
        }

        return null;
    }

}
