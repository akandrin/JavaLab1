package com.example.threadpanelfx.Controller.MessageHandler;

import com.example.threadpanelfx.Controller.IController;
import com.example.threadpanelfx.Model.GameEvent.GameEvent;
import com.example.threadpanelfx.Model.GameModelPool;
import com.example.threadpanelfx.Model.GameSettings;
import com.example.threadpanelfx.Model.IGameModel;
import com.example.threadpanelfx.Model.PlayerSettings;
import com.example.threadpanelfx.NetUtility.IMessenger;
import com.example.threadpanelfx.NetUtility.Invoker.RequestCall;
import com.example.threadpanelfx.NetUtility.Message;
import com.example.threadpanelfx.NetUtility.MessengerPool;
import com.example.threadpanelfx.NetUtility.Request.CheckNameRequest;
import com.example.threadpanelfx.NetUtility.Request.CheckNameResponse;
import com.example.threadpanelfx.NetUtility.Request.Request;
import com.example.threadpanelfx.NetUtility.Request.Response;

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

    private Response handleRequest(CheckNameRequest request)
    {
        String playerName = request.GetName();
        IGameModel model = GameModelPool.Instance().GetModel(GameModelPool.ModelType.local);
        CheckNameResponse.Status status = null;
        if (model.GetPlayersInfoCopy().size() < GameSettings.GetMaxPlayerCount()) {
            boolean added = model.AddPlayer(playerName, false);
            status = added ? CheckNameResponse.Status.ok : CheckNameResponse.Status.alreadyExisted;
        }
        else
        {
            status = CheckNameResponse.Status.limitExceeded;
        }
        return new CheckNameResponse(request, status);
    }

    private Response handleRequest(Request request)
    {
        Response response = null;
        switch (request.GetType())
        {
            case checkName:
                response = handleRequest((CheckNameRequest) request);
                break;
            default:
                System.err.println("Unknown request type");
                break;
        }
        return response;
    }

    @Override
    protected void HandleRequest(Message requestMessage) {
        assert requestMessage.dataType == Message.DataType.request;

        Request request = (Request) requestMessage.data;
        Response response = handleRequest(request);
        IMessenger messenger = MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncSingle, requestMessage.addresser);
        Message responseMessage = response.CreateResponseMessage("server", null);
        messenger.SendMessage(responseMessage);
    }

    @Override
    protected void HandleResponse(Message response) {
        System.err.println("Server does not handle response");
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
