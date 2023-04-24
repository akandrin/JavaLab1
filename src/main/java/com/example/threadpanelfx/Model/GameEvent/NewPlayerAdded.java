package com.example.threadpanelfx.Model.GameEvent;

public class NewPlayerAdded implements GameEvent {
    private String m_playerName;

    public NewPlayerAdded(String playerName){
        this.m_playerName = playerName;
    }

    public String GetPlayerName()
    {
        return m_playerName;
    }

    @Override
    public Type GetType() {
        return Type.newPlayerAdded;
    }
}
