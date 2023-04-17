package com.example.threadpanelfx.Model;

import com.example.threadpanelfx.NetUtility.IMessenger;
import com.example.threadpanelfx.NetUtility.Message;
import com.example.threadpanelfx.NetUtility.MessengerPool;

public class EventSender implements IObserver {
    private final String m_addresser;
    private final String m_destination;

    public EventSender(String addresser, String destination)
    {
        this.m_addresser = addresser;
        this.m_destination = destination;
    }

    @Override
    public void Update(Object event) {
        IMessenger messenger = MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncBroadcast);
        Message message = new Message(m_addresser, m_destination, Message.DataType.event, event);
        messenger.SendMessage(message);
    }

    @Override
    public boolean CanImmediatelyUpdate() {
        return true;
    }
}
