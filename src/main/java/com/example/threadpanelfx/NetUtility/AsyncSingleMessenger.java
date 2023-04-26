package com.example.threadpanelfx.NetUtility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AsyncSingleMessenger implements IMessenger {
    private final Socket m_socket;

    private List<Message> m_messageToSend = Collections.synchronizedList(new ArrayList<>());
    private List<Message> m_receivedMessages = Collections.synchronizedList(new ArrayList<>());

    public AsyncSingleMessenger(Socket socket) {
        this.m_socket = socket;

        // поток по приему сообщений
        new Thread(() ->
        {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = null;
                try {
                    var reader = new ObjectInputStream(m_socket.getInputStream());
                    System.out.println("Receiving message...");
                    message = (Message) reader.readObject();
                    message.addresser = m_socket;
                    System.out.println("... received: " + message);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (message != null) {
                    m_receivedMessages.add(message);
                }
            }
        }
        ).start();

        // поток по передаче сообщений
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    if (!m_messageToSend.isEmpty()) {
                        Message message = m_messageToSend.get(0);
                        m_messageToSend.remove(0);
                        var writer = new ObjectOutputStream(m_socket.getOutputStream());
                        System.out.println("Sending message " + message);
                        writer.writeObject(message);
                        System.out.println("... success");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void SendMessage(Message message) {
        if (message != null) {
            m_messageToSend.add(message);
        }
    }

    @Override
    public boolean HasMessage() {
        return !m_receivedMessages.isEmpty();
    }

    @Override
    public Message ReceiveMessage() {
        Message message = null;
        synchronized (m_receivedMessages) {
            if (!m_receivedMessages.isEmpty()) {
                message = m_receivedMessages.get(0);
                m_receivedMessages.remove(0);
            }
        }
        return message;
    }
}
