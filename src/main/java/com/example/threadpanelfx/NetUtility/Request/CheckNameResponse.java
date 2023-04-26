package com.example.threadpanelfx.NetUtility.Request;

public class CheckNameResponse extends Response {
    private Boolean m_status;

    public CheckNameResponse(Request request, boolean isOk)
    {
        super(request);
        m_status = isOk;
    }

    public boolean GetStatus()
    {
        return m_status;
    }

    @Override
    public ResponseType GetType() {
        return ResponseType.checkName;
    }
}
