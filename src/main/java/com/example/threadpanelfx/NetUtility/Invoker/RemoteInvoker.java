package com.example.threadpanelfx.NetUtility.Invoker;

import com.example.threadpanelfx.Model.IObserver;
import com.example.threadpanelfx.Model.PlayerSettings;
import com.example.threadpanelfx.NetUtility.IMessenger;
import com.example.threadpanelfx.NetUtility.Message;
import com.example.threadpanelfx.NetUtility.MessengerPool;

import java.lang.invoke.CallSite;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RemoteInvoker implements IInvoker {
    private final ObjectForInvokeGetter m_objectForInvokeGetter;

    public RemoteInvoker(ObjectForInvokeGetter objectForInvokeGetter)
    {
        this.m_objectForInvokeGetter = objectForInvokeGetter;
    }

    public Object Invoke(String methodName, Class[] methodArgsType, Object ... args) {
        Message message = RequestCall.CreateRequestCallMessage(PlayerSettings.GetPlayerName(), "server", m_objectForInvokeGetter, methodName, methodArgsType, args);
        IMessenger messenger = MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncSingle);
        messenger.SendMessage(message);
        return null; // todo: принимать сообщение-ответ, извлекать результат, возвращать его в качестве возвращаемого значения (обернуть в CompletableFuture)
    }
}