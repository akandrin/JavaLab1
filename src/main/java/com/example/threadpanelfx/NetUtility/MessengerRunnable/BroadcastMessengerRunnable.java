package com.example.threadpanelfx.NetUtility.MessengerRunnable;

import com.example.threadpanelfx.NetUtility.Message;

import java.net.Socket;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class BroadcastMessengerRunnable extends MessengerRunnable {
    private final ReentrantReadWriteLock m_lock;

    public BroadcastMessengerRunnable(Socket socket, int currentSocketNumber, List<Message> messagesToSend, List<Message> receivedMessages, ReentrantReadWriteLock lock) {
        super(socket, currentSocketNumber, messagesToSend, receivedMessages);
        this.m_lock = lock;
    }

    @Override
    protected boolean sendMessage()
    {
        m_lock.readLock().lock();
        boolean result = super.sendMessage();
        m_lock.readLock().unlock();
        return result;
    }
}
