package com.example.threadpanelfx.Model.GameEvent;

public class ShotsChanged implements GameEvent {
    private String m_playerName;
    private int m_newShots;

    public ShotsChanged(String playerName, int newShots)
    {
        this.m_playerName = playerName;
        this.m_newShots = newShots;
    }

    public int GetShots()
    {
        return m_newShots;
    }

    public String GetPlayerName() { return m_playerName; }

    @Override
    public GameEvent.Type GetType() {
        return GameEvent.Type.shotsChanged;
    }
}
