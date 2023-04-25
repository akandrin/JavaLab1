package com.example.threadpanelfx.NetUtility;

import com.example.threadpanelfx.Model.LocalGameModel;

public class MessengerPool {
    public enum MessengerType
    {
        asyncBroadcast,
        asyncSingle
    }

    private static MessengerPool instance;

    private MessengerPool()
    {
    }

    public static MessengerPool Instance()
    {
        if (instance == null)
        {
            instance = new MessengerPool();
        }
        return instance;
    }

    public IMessenger GetMessenger(MessengerPool.MessengerType messengerType)
    {
        IMessenger messenger = null;
        switch (messengerType)
        {
            case asyncBroadcast:
                messenger = AsyncBroadcastMessenger.Instance();
                break;
            case asyncSingle:
                messenger = AsyncSingleMessenger.Instance();
                break;
            default:
                throw new RuntimeException("Unknown messenger type");
        }
        return messenger;
    }
}
