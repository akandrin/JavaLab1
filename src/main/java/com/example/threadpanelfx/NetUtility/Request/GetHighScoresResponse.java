package com.example.threadpanelfx.NetUtility.Request;

import com.example.threadpanelfx.Model.Database.Entry;

import java.util.List;

public class GetHighScoresResponse extends Response {
    private List<Entry> m_entries;

    public GetHighScoresResponse(Request request, List<Entry> entries)
    {
        super(request);
        this.m_entries = entries;
    }

    public List<Entry> GetHighScoresList()
    {
        return m_entries;
    }

    @Override
    public ResponseType GetType() {
        return ResponseType.getHighScores;
    }
}
