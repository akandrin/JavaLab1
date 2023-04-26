package com.example.threadpanelfx.NetUtility.Request;

import com.example.threadpanelfx.NetUtility.Message;

import java.io.Serializable;

public abstract class Response implements Serializable {
    public enum ResponseType
    {
        checkName
    }

    private Request m_request;

    public Response(Request request)
    {
        this.m_request = request;
    }

    public Request GetRequest()
    {
        return m_request;
    }


    abstract public ResponseType GetType();

    public Message CreateResponseMessage(String adresser, String destination)
    {
        return new Message(adresser, destination, Message.DataType.response, this);
    }
}
