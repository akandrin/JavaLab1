package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Model.GameModelPool;
import com.example.threadpanelfx.NetUtility.Invoker.RequestCall;
import com.example.threadpanelfx.NetUtility.Message;
import com.example.threadpanelfx.NetUtility.MessageSplitterByType;
import com.example.threadpanelfx.NetUtility.MessengerPool;
import javafx.fxml.FXML;

import java.lang.reflect.InvocationTargetException;

public class GameServerController extends GameFrameController {

    public GameServerController()
    {
        MessageSplitterByType.Initialize(MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncBroadcast));
        var messageSplitterByType = MessageSplitterByType.Instance();
        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (messageSplitterByType.HasMessage(Message.DataType.requestCall)) {
                    Message message = messageSplitterByType.GetMessage(Message.DataType.requestCall);
                    if (message == null)
                    {
                        System.err.println("Error: message was null");
                        continue;
                    }
                    var requestCall = (RequestCall.Call)message.data;
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
        }).start();
    }

    @FXML
    public void initialize() {
        controller = new ServerController(GameModelPool.Instance().GetModel(GameModelPool.ModelType.local), circle1, circle2);
        CurrentControllerHolder.Set(controller);
    }
}
