package com.example.threadpanelfx.NetUtility;

import java.util.*;

public class MessageSplitterByType {
    private IMessenger m_messenger = null;

    static final private MessageSplitterByType instance = new MessageSplitterByType();

    private Map<Message.DataType, List<Message>> m_typeToMessages = new HashMap<>();

    private MessageSplitterByType()
    {
        m_typeToMessages.put(Message.DataType.event, Collections.synchronizedList(new ArrayList<Message>()));
        m_typeToMessages.put(Message.DataType.requestCall, Collections.synchronizedList(new ArrayList<Message>()));
        new Thread(()->{
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (m_messenger == null)
                    continue;
                if (m_messenger.HasMessage()) {
                    Message message = m_messenger.ReceiveMessage();
                    var list = m_typeToMessages.get(message.dataType);
                    list.add(message);
                }
            }
        }).start();
    }

    static public void Initialize(IMessenger messenger)
    {
        if (instance.m_messenger != null)
            throw new RuntimeException("Already initialized");
        if (messenger == null)
            throw new RuntimeException("Messenger is null");

        instance.m_messenger = messenger;
    }

    static public MessageSplitterByType Instance()
    {
        return instance;
    }

    public boolean HasMessage(Message.DataType dataType)
    {
        return !m_typeToMessages.get(dataType).isEmpty();
    }

    public Message GetMessage(Message.DataType dataType)
    {
        Message message = null;
        synchronized (m_typeToMessages.get(dataType)) {
            var messages = m_typeToMessages.get(dataType);
            if (!messages.isEmpty()) {
                message = messages.get(0);
                messages.remove(0);
            }
        }
        return message;
    }
}
