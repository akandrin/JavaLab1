package com.example.threadpanelfx.NetUtility.Request;

import com.example.threadpanelfx.NetUtility.Message;

import java.io.Serializable;

public abstract class Request implements Serializable {
    public enum RequestType
    {
        checkName
    }

    abstract public RequestType GetType();

    public Message CreateRequestMessage(String adresser, String destination)
    {
        return new Message(adresser, destination, Message.DataType.request, this);
    }
}
