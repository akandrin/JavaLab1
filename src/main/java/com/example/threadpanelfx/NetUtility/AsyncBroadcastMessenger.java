package com.example.threadpanelfx.NetUtility;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Занимается приемом/отправкой *всех* сообщений со стороны сервера.
public class AsyncBroadcastMessenger implements IMessenger {
    private ServerSocket m_serverSocket;

    private final List<IMessenger> m_messengers = Collections.synchronizedList(new ArrayList<IMessenger>());

    private static IMessenger m_instance;

    private AsyncBroadcastMessenger() {
        try {
            m_serverSocket = new ServerSocket(NetConstants.serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // поток, принимающий входящие соединения
        new Thread(() -> {
            int acceptedCount = 0;
            while (true) {
                try {
                    Socket socket = m_serverSocket.accept();
                    acceptedCount++;

                    IMessenger singleMessenger = new AsyncSingleMessenger(socket);
                    m_messengers.add(singleMessenger);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Count of successfully accepted connections = " + acceptedCount);
                }
            }
        }).start();
    }

    protected static IMessenger Instance() {
        if (m_instance == null) {
            m_instance = new AsyncBroadcastMessenger();
        }
        return m_instance;
    }

    @Override
    public void SendMessage(Message message) {
        synchronized (m_messengers)
        {
            for (IMessenger messenger : m_messengers)
            {
                messenger.SendMessage(message);
            }
        }
    }

    @Override
    public boolean HasMessage() {
        synchronized (m_messengers)
        {
            for (IMessenger messenger : m_messengers)
            {
                if (messenger.HasMessage())
                    return true;
            }
        }
        return false;
    }

    @Override
    public Message ReceiveMessage() {
        synchronized (m_messengers)
        {
            for (IMessenger messenger : m_messengers)
            {
                if (messenger.HasMessage())
                    return messenger.ReceiveMessage();
            }
        }
        return null;
    }
}
