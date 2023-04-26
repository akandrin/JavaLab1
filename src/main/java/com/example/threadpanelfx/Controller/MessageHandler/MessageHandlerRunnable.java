package com.example.threadpanelfx.Controller.MessageHandler;

import com.example.threadpanelfx.Controller.IController;
import com.example.threadpanelfx.Model.GameEvent.GameEvent;
import com.example.threadpanelfx.NetUtility.IMessenger;
import com.example.threadpanelfx.NetUtility.Invoker.RequestCall;
import com.example.threadpanelfx.NetUtility.Message;
import com.example.threadpanelfx.NetUtility.MessageSplitterByType;
import com.example.threadpanelfx.NetUtility.MessengerPool;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class MessageHandlerRunnable implements Runnable {

    private final MessageSplitterByType m_messageSplitter;
    private final AtomicBoolean m_isRunning = new AtomicBoolean(true);

    public MessageHandlerRunnable(IMessenger messenger)
    {
        MessageSplitterByType.Initialize(messenger);
        this.m_messageSplitter = MessageSplitterByType.Instance();
    }

    public void Stop()
    {
        m_isRunning.set(false);
    }

    abstract protected void Handle(GameEvent event);

    abstract protected void Handle(RequestCall.Call requestCall);

    private Message getMessage(Message.DataType dataType)
    {
        Message message = null;
        if (m_messageSplitter.HasMessage(dataType)) {
            message = m_messageSplitter.GetMessage(dataType);
            if (message == null){
                System.err.println("Error: message was null");
            }
        }
        return message;
    }

    @Override
    public void run() {
        while (m_isRunning.get()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            {
                Message eventMessage = getMessage(Message.DataType.event);
                if (eventMessage != null)
                {
                    Handle((GameEvent)eventMessage.data);
                }
            }
            {
                Message requestCallMessage = getMessage(Message.DataType.requestCall);
                if (requestCallMessage != null)
                {
                    Handle((RequestCall.Call)requestCallMessage.data);
                }
            }

        }
    }
}
