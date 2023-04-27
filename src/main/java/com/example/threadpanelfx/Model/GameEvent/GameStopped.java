package com.example.threadpanelfx.Model.GameEvent;

public class GameStopped extends GameEvent {

    private String m_playerName;

    public GameStopped(String winnerName)
    {
        this.m_playerName = winnerName;
    }

    public String GetWinnerName()
    {
        return m_playerName;
    }

    @Override
    public Type GetType() {
        return Type.gameStopped;
    }
}
