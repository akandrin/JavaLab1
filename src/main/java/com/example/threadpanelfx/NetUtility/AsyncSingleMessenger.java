package com.example.threadpanelfx.NetUtility;

import com.example.threadpanelfx.NetUtility.MessengerRunnable.BroadcastMessengerRunnable;
import com.example.threadpanelfx.NetUtility.MessengerRunnable.MessengerRunnable;
import com.example.threadpanelfx.NetUtility.MessengerRunnable.SingleMessengerRunnable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// Занимается приемом/отправкой *всех* сообщений со стороны клиента.
public class AsyncSingleMessenger implements IMessenger {

    // Collections.synchronizedList - для синхронизированных операций add/remove.
    private final List<Message> m_messagesToSend = Collections.synchronizedList(new ArrayList<Message>());
    private final List<Message> m_receivedMessages = Collections.synchronizedList(new ArrayList<Message>());

    private Socket m_clientSocket;

    private static IMessenger m_instance;

    private AsyncSingleMessenger() {
        try {
            m_clientSocket = new Socket(NetConstants.serverHostname, NetConstants.serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // поток по приему/отправке сообщений для одного сокета
        MessengerRunnable messenger = new SingleMessengerRunnable(m_clientSocket, m_messagesToSend, m_receivedMessages);
        new Thread(messenger).start();
    }

    protected static IMessenger Instance() {
        if (m_instance == null) {
            m_instance = new AsyncSingleMessenger();
        }
        return m_instance;
    }

    @Override
    public void SendMessage(Message message) {
        m_messagesToSend.add(message);
    }

    @Override
    public boolean HasMessage() {
        return !m_receivedMessages.isEmpty();
    }

    @Override
    public Message ReceiveMessage() {
        Message message = m_receivedMessages.get(0);
        m_receivedMessages.remove(0);
        return message;
    }
}
