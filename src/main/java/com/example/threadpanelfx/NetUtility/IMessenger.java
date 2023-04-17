package com.example.threadpanelfx.NetUtility;

public interface IMessenger {
    void SendMessage(Message message);
    boolean HasMessage();
    Message ReceiveMessage();
}
