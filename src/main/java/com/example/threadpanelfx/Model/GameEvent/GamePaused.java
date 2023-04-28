package com.example.threadpanelfx.Model.GameEvent;

public class GamePaused extends GameEvent {
    private String m_playerName;

    public GamePaused(String playerName)
    {
        this.m_playerName = playerName;
    }

    public String GetPlayerName()
    {
        return m_playerName;
    }

    @Override
    public Type GetType() {
        return Type.gamePaused;
    }
}
