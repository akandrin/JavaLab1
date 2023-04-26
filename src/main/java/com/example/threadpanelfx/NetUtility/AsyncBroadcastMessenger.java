package com.example.threadpanelfx.NetUtility;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

// Занимается приемом/отправкой *всех* сообщений со стороны сервера.
public class AsyncBroadcastMessenger implements IMessenger {
    private ServerSocket m_serverSocket;

    private final List<IMessenger> m_messengers = Collections.synchronizedList(new ArrayList<IMessenger>());
    private final Map<Socket, IMessenger> m_socketToMessenger = new HashMap<>();

    private static AsyncBroadcastMessenger m_instance;

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
                    m_socketToMessenger.put(socket, singleMessenger);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Count of successfully accepted connections = " + acceptedCount);
                }
            }
        }).start();
    }

    protected static AsyncBroadcastMessenger Instance() {
        if (m_instance == null) {
            m_instance = new AsyncBroadcastMessenger();
        }
        return m_instance;
    }

    public IMessenger GetMessenger(Socket socket)
    {
        return m_socketToMessenger.get(socket);
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
