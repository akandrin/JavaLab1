package com.example.threadpanelfx.NetUtility.Request;

public class GetHighScoresRequest extends Request {
    public GetHighScoresRequest()
    {
    }

    @Override
    public RequestType GetType() {
        return RequestType.getHighScores;
    }
}
