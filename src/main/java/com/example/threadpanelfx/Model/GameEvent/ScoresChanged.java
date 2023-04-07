package com.example.threadpanelfx.Model.GameEvent;


public class ScoresChanged implements GameEvent {
    private String m_playerName;
    private int m_newScores;

    public ScoresChanged(String playerName, int newScores)
    {
        this.m_playerName = playerName;
        this.m_newScores = newScores;
    }

    public int GetScores()
    {
        return m_newScores;
    }

    public String GetPlayerName() { return m_playerName; }

    @Override
    public Type GetType() {
        return Type.scoresChanged;
    }
}
