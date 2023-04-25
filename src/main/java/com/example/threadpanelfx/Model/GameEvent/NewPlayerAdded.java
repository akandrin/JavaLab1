package com.example.threadpanelfx.Model.GameEvent;

import java.io.Serializable;

public class NewPlayerAdded extends GameEvent {
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
