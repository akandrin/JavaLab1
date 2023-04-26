package com.example.threadpanelfx.Controller.MessageHandler;

import com.example.threadpanelfx.Controller.IController;
import com.example.threadpanelfx.Model.GameEvent.GameEvent;
import com.example.threadpanelfx.Model.IObservable;
import com.example.threadpanelfx.Model.IObserver;
import com.example.threadpanelfx.NetUtility.IMessenger;
import com.example.threadpanelfx.NetUtility.Invoker.RequestCall;
import com.example.threadpanelfx.NetUtility.Message;
import com.example.threadpanelfx.NetUtility.Request.Request;
import com.example.threadpanelfx.NetUtility.Request.Response;
import javafx.application.Platform;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class ClientMessageHandlerRunnable extends MessageHandlerRunnable implements IObservable {

    private ArrayList<IObserver> m_observers = new ArrayList<>();

    public ClientMessageHandlerRunnable(IMessenger messenger) {
        super(messenger);
    }

    @Override
    protected void Handle(GameEvent event)
    {
        Notify(event);
    }

    @Override
    protected void HandleRequest(Message requestMessage) {
        System.err.println("Error: Client does not receive requests");
    }

    @Override
    protected void HandleResponse(Message responseMessage) {
        assert responseMessage.dataType == Message.DataType.response;
    }

    @Override
    protected void Handle(RequestCall.Call requestCall)
    {
        System.err.println("Error: Client does not receive request calls");
    }

    @Override
    public void AddObserver(IObserver observer) {
        m_observers.add(observer);
    }

    public void RemoveObserver(IObserver observer)
    {
        m_observers.remove(observer);
    }

    public void Notify(Object event)
    {
        for (var observer : m_observers)
        {
            if (observer.CanImmediatelyUpdate())
                observer.Update(event);
            else
                Platform.runLater(()->{observer.Update(event);});
        }
    }
}
