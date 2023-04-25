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
    protected final List<Message> m_messagesToSend;
    private final AtomicInteger m_currentMessageToBeSent = new AtomicInteger();
    protected final List<Message> m_receivedMessages;

    public MessengerRunnable(Socket socket, int currentSocketNumber, List<Message> messagesToSend, List<Message> receivedMessages) {
        this.m_socket = socket;
        this.m_currentSocketNumber = currentSocketNumber;
        this.m_messagesToSend = messagesToSend;
        this.m_currentMessageToBeSent.set(0);
        this.m_receivedMessages = receivedMessages;
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
            var reader = new ObjectInputStream(m_socket.getInputStream());
            receivedMessage = (Message) reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            printInfo();
        }
        if (receivedMessage != null) {
            System.out.println("Socket " + m_currentSocketNumber + " read message:");
            System.out.println(receivedMessage);

            m_receivedMessages.add(receivedMessage);
        }
        else
        {
            System.out.println("Socket " + m_currentSocketNumber + " does not send any message");
        }
    }

    protected boolean sendMessage()
    {
        Message messageToSend = null;
        int currentMessageToBeSent = m_currentMessageToBeSent.get();

        if (currentMessageToBeSent < m_messagesToSend.size()) {
            messageToSend = m_messagesToSend.get(currentMessageToBeSent);
        }

        if (messageToSend != null) {
            try {
                var writer = new ObjectOutputStream(m_socket.getOutputStream());
                System.out.println("Sending message " + messageToSend);
                writer.writeObject(messageToSend);
                System.out.println("... success");
            } catch (IOException e) {
                e.printStackTrace();
            }

            m_currentMessageToBeSent.set(currentMessageToBeSent + 1);
        }
        else
        {
            System.out.println("Nothing to send");
        }

        return messageToSend != null;
    }

    @Override
    public void run() {
        new Thread(()->{
            while (true) {
                // внутри блокирующий вызов
                recvMessage();
            }
        }).start();

        while (true) {
            sendMessage();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
