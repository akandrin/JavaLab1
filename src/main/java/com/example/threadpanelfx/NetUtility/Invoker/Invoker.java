package com.example.threadpanelfx.NetUtility.Invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Invoker<Interface> implements IInvoker {
    private Interface m_object;

    public Invoker(Interface object)
    {
        m_object = object;
    }

    @Override
    public Object Invoke(String methodName, Class[] methodArgsType, Object[] args) {
        Object result = null;
        try {
            result = m_object.getClass().getMethod(methodName, methodArgsType).invoke(m_object, args);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
}
