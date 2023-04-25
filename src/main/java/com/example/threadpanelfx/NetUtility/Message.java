package com.example.threadpanelfx.NetUtility;

import java.io.Serializable;

public class Message implements Serializable {
    public enum DataType
    {
        event,
        requestCall
    }

    public String addresser; // отправитель
    public String destination; // получатель

    public DataType dataType;
    public Object data;

    public Message(String addresser, String destination, DataType dataType, Object data)
    {
        this.addresser = addresser;
        this.destination = destination;
        this.dataType = dataType;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "addresser='" + addresser + '\'' +
                ", destination='" + destination + '\'' +
                ", dataType=" + dataType +
                ", data=" + data +
                '}';
    }
}
