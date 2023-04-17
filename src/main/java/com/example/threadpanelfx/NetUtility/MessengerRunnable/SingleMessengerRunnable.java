package com.example.threadpanelfx.NetUtility.MessengerRunnable;

import com.example.threadpanelfx.NetUtility.Message;

import java.net.Socket;
import java.util.List;

public class SingleMessengerRunnable extends MessengerRunnable {
    public SingleMessengerRunnable(Socket socket, int currentSocketNumber, List<Message> messagesToSend, List<Message> receivedMessages) {
        super(socket, currentSocketNumber, messagesToSend, receivedMessages);
    }

    @Override
    protected void sendMessage() {
        super.sendMessage();
        synchronized (this.m_receivedMessages)
        {
            int currentMessageToBeSent = GetCurrentMessageToBeSent();
            m_receivedMessages.remove(currentMessageToBeSent);
            SetCurrentMessageToBeSent(currentMessageToBeSent - 1);
        }
    }
}
