package com.example.threadpanelfx.NetUtility.Request;

public class CheckNameResponse extends Response {
    public enum Status
    {
        alreadyExisted(0),
        ok(1),
        limitExceeded(2);

        private final int value;
        private Status(int value)
        {
            this.value = value;
        }

        public int GetValue()
        {
            return value;
        }
    }
    private Status m_status;

    public CheckNameResponse(Request request, Status status)
    {
        super(request);
        m_status = status;
    }

    public Status GetStatus()
    {
        return m_status;
    }

    @Override
    public ResponseType GetType() {
        return ResponseType.checkName;
    }
}
