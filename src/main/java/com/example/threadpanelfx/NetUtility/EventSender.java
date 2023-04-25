package com.example.threadpanelfx.NetUtility;

import com.example.threadpanelfx.Model.IObserver;

public class EventSender implements IObserver {

    private EventSender()
    {
    }

    static private EventSender instance = new EventSender();

    static public IObserver Instance()
    {
        return instance;
    }

    @Override
    public void Update(Object event) {
        Message message = new Message("server", "all", Message.DataType.event, event);
        MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncBroadcast).SendMessage(message);
    }

    @Override
    public boolean CanImmediatelyUpdate() {
        return true;
    }
}
