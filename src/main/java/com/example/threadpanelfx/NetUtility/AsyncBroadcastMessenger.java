package com.example.threadpanelfx.NetUtility;

import com.example.threadpanelfx.NetUtility.MessengerRunnable.BroadcastMessengerRunnable;
import com.example.threadpanelfx.NetUtility.MessengerRunnable.MessengerRunnable;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

// Занимается приемом/отправкой *всех* сообщений со стороны сервера.
public class AsyncBroadcastMessenger implements IMessenger {

    // Collections.synchronizedList - для синхронизированных операций add/remove.
    private final List<Message> m_messagesToSend = Collections.synchronizedList(new ArrayList<Message>());
    // Все потоки-читатели (MessengerRunnable) читают сообщения начиная с начала массива из m_messagesToSend и отправляют (блокировка чтения).
    // Один поток-писатель очищает сообщения m_messagesToSend, начиная с начала (блокировка записи).
    private final ReentrantReadWriteLock m_lock = new ReentrantReadWriteLock(true);
    private final List<Message> m_receivedMessages = Collections.synchronizedList(new ArrayList<Message>());

    private ServerSocket m_serverSocket;
    // сделано синхронизированным на будущее (когда захотим в другом потоке отсоединять сокеты)
    private final List<MessengerRunnable> m_messengers = Collections.synchronizedList(new ArrayList<MessengerRunnable>());

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

                    MessengerRunnable messenger = new BroadcastMessengerRunnable(socket, acceptedCount, m_messagesToSend, m_receivedMessages, m_lock);
                    // поток по приему/отправке сообщений для одного сокета
                    new Thread(messenger).start();
                    m_messengers.add(messenger);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Count of successfully accepted connections = " + acceptedCount);
                }
            }
        }).start();

        // очистка сообщений из m_messagesToSend
        new Thread(new MessagesToSendCleaner()).start();
    }

    protected static IMessenger Instance() {
        if (m_instance == null) {
            m_instance = new AsyncBroadcastMessenger();
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

    // очищает из m_messagesToSend сообщения, которые уже были отправлены
    // И уменьшает m_currentMessageToBeSent у MessengerRunnable
    class MessagesToSendCleaner implements Runnable
    {
        @Override
        public void run() {
            while (true)
            {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (m_messagesToSend.isEmpty())
                    continue;

                m_lock.writeLock().lock();

                int minimalCurrentMessageToBeSent = Integer.MAX_VALUE;
                for (var messenger : m_messengers)
                {
                    int currentMessageToBeSent = messenger.GetCurrentMessageToBeSent();
                    if (currentMessageToBeSent < minimalCurrentMessageToBeSent)
                        minimalCurrentMessageToBeSent = currentMessageToBeSent;
                }

                for (var messenger : m_messengers)
                {
                    int currentMessageToBeSent = messenger.GetCurrentMessageToBeSent();
                    messenger.SetCurrentMessageToBeSent(currentMessageToBeSent - minimalCurrentMessageToBeSent);
                }

                m_messagesToSend.subList(0, minimalCurrentMessageToBeSent).clear();

                m_lock.writeLock().unlock();
            }
        }
    }
}
