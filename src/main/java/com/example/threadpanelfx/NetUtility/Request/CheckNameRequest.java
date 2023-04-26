package com.example.threadpanelfx.NetUtility.Request;

public class CheckNameRequest extends Request {
    private String m_name;

    public CheckNameRequest(String name)
    {
        this.m_name = name;
    }

    public String GetName()
    {
        return m_name;
    }

    @Override
    public RequestType GetType() {
        return RequestType.checkName;
    }
}
