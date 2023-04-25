package com.example.threadpanelfx.NetUtility.MessengerRunnable;

import com.example.threadpanelfx.NetUtility.Message;

import java.net.Socket;
import java.util.List;

public class SingleMessengerRunnable extends MessengerRunnable {
    public SingleMessengerRunnable(Socket socket, List<Message> messagesToSend, List<Message> receivedMessages) {
        super(socket, 0, messagesToSend, receivedMessages);
    }

    @Override
    protected boolean sendMessage() {
        boolean result = super.sendMessage();
        if (result) {
            synchronized (this.m_messagesToSend) {
                int currentMessageToBeSent = GetCurrentMessageToBeSent();
                currentMessageToBeSent -= 1;
                SetCurrentMessageToBeSent(currentMessageToBeSent);
                m_messagesToSend.remove(currentMessageToBeSent);
            }
        }
        return result;
    }
}
