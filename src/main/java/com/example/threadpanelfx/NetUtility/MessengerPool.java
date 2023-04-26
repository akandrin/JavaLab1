package com.example.threadpanelfx.NetUtility;

import com.example.threadpanelfx.Model.LocalGameModel;

import java.io.IOException;
import java.net.Socket;

public class MessengerPool {
    public enum MessengerType
    {
        asyncBroadcast,
        asyncSingle
    }

    private static final MessengerPool instance = new MessengerPool();

    private IMessenger m_asyncSingleMessenger;

    private MessengerPool()
    {
    }

    public static MessengerPool Instance()
    {
        return instance;
    }

    public IMessenger GetMessenger(MessengerPool.MessengerType messengerType, Object ... args)
    {
        IMessenger messenger = null;
        switch (messengerType)
        {
            case asyncBroadcast:
                messenger = AsyncBroadcastMessenger.Instance();
                break;
            case asyncSingle:
                if (args.length == 0) {
                    if (m_asyncSingleMessenger == null) {
                        try {
                            m_asyncSingleMessenger = new AsyncSingleMessenger(new Socket(NetConstants.serverHostname, NetConstants.serverPort));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    messenger = m_asyncSingleMessenger;
                }
                else
                {
                    assert args[0] instanceof Socket;
                    Socket socket = (Socket)args[0];
                    messenger = AsyncBroadcastMessenger.Instance().GetMessenger(socket);
                }
                break;
            default:
                throw new RuntimeException("Unknown messenger type");
        }
        assert messenger != null;
        return messenger;
    }
}
