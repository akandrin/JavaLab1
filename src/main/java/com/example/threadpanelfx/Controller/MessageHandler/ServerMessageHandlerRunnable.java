package com.example.threadpanelfx.Controller.MessageHandler;

import com.example.threadpanelfx.Controller.IController;
import com.example.threadpanelfx.Model.GameEvent.GameEvent;
import com.example.threadpanelfx.NetUtility.IMessenger;
import com.example.threadpanelfx.NetUtility.Invoker.RequestCall;

import java.lang.reflect.InvocationTargetException;

public class ServerMessageHandlerRunnable extends MessageHandlerRunnable{

    public ServerMessageHandlerRunnable(IMessenger messenger) {
        super(messenger);
    }

    @Override
    protected void Handle(GameEvent event)
    {
        System.err.println("Error: Server does not receive events");
    }

    @Override
    protected void Handle(RequestCall.Call requestCall)

    {
        var objectForInvokeGetter = requestCall.objectForInvokeGetter;
        var objectClass = objectForInvokeGetter.m_class;
        var getInstanceMethodName = objectForInvokeGetter.m_getInstanceMethodName;
        var args = objectForInvokeGetter.m_args;

        Object resultObject = null;

        try {
            resultObject = objectClass.getMethod(getInstanceMethodName).invoke(null, args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (resultObject instanceof IController) {
            IController controller = (IController) resultObject;
            try {
                IController.class.getMethod(requestCall.methodName, requestCall.methodArgsType).invoke(controller, requestCall.args);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Unknown class : " + resultObject);
        }
    }
}
