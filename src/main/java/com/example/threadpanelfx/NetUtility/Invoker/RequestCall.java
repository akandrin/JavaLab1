package com.example.threadpanelfx.NetUtility.Invoker;

import com.example.threadpanelfx.NetUtility.Message;

import java.io.Serializable;

public class RequestCall implements Serializable
{
    static public class Call implements Serializable
    {
        public ObjectForInvokeGetter objectForInvokeGetter;
        public String methodName;
        public Class[] methodArgsType;
        public Object[] args;

        public Call(ObjectForInvokeGetter objectForInvokeGetter, String methodName, Class[] methodArgsType, Object[] args)
        {
            this.objectForInvokeGetter = objectForInvokeGetter;
            this.methodName = methodName;
            this.methodArgsType = methodArgsType;
            this.args = args;
        }
    }

    static public Message CreateRequestCallMessage(String adresser, String destination, ObjectForInvokeGetter objectForInvokeGetter, String methodName, Class[] methodArgsType, Object[] args)
    {
        return new Message(adresser, destination, Message.DataType.requestCall, new Call(objectForInvokeGetter, methodName, methodArgsType, args));
    }
}
