package com.example.threadpanelfx.NetUtility.Invoker;

import java.io.Serializable;
import java.lang.reflect.Method;

// Как получить объект для вызова Invoke
public class ObjectForInvokeGetter implements Serializable {
    public Class m_class;
    public String m_getInstanceMethodName;
    public Object[] m_args;

    public ObjectForInvokeGetter(Class pClass, String getInstanceMethodName, Object ... args)
    {
        this.m_class = pClass;
        this.m_getInstanceMethodName = getInstanceMethodName;
        this.m_args = args;
    }
}
