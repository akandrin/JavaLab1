package com.example.threadpanelfx.NetUtility.MessengerRunnable;

import com.example.threadpanelfx.NetUtility.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MessengerRunnable implements Runnable {
    private final Socket m_socket;
    private final int m_currentSocketNumber;
    private final List<Message> m_messagesToSend;
    private AtomicInteger m_currentMessageToBeSent;
    protected final List<Message> m_receivedMessages;

    private ObjectInputStream m_reader;
    private ObjectOutputStream m_writer;

    public MessengerRunnable(Socket socket, int currentSocketNumber, List<Message> messagesToSend, List<Message> receivedMessages) {
        this.m_socket = socket;
        this.m_currentSocketNumber = currentSocketNumber;
        this.m_messagesToSend = messagesToSend;
        this.m_currentMessageToBeSent.set(0);
        this.m_receivedMessages = receivedMessages;

        try {
            this.m_reader = new ObjectInputStream(m_socket.getInputStream());
            this.m_writer = new ObjectOutputStream(m_socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            printInfo();
        }
    }

    public void SetCurrentMessageToBeSent(int currentMessageToBeSent)
    {
        m_currentMessageToBeSent.set(currentMessageToBeSent);
    }

    public int GetCurrentMessageToBeSent()
    {
        return m_currentMessageToBeSent.get();
    }

    private void printInfo()
    {
        System.err.println("Current socket number = " + m_currentSocketNumber);
    }

    protected void recvMessage()
    {
        Message receivedMessage = null;
        try {
            receivedMessage = (Message) m_reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            printInfo();
        }
        System.out.println("Socket " + m_currentSocketNumber + " read message:");
        System.out.println(receivedMessage);
        m_receivedMessages.add(receivedMessage);
    }

    protected void sendMessage()
    {
        Message messageToSend = null;
        int currentMessageToBeSent = m_currentMessageToBeSent.get();

        if (currentMessageToBeSent < m_messagesToSend.size()) {
            messageToSend = m_messagesToSend.get(currentMessageToBeSent);
        }
        try {
            m_writer.writeObject(messageToSend);
        } catch (IOException e) {
            e.printStackTrace();
        }

        m_currentMessageToBeSent.set(currentMessageToBeSent + 1);
    }

    @Override
    public void run() {
        while (true) {
            recvMessage();
            sendMessage();
        }
    }
}
