package com.example.threadpanelfx.Model.GameEvent;

import java.io.Serializable;

public class NewPlayerAdded extends GameEvent {
    private String m_playerName;
    private Boolean m_status;

    public NewPlayerAdded(String playerName, boolean status){
        this.m_playerName = playerName;
        this.m_status = status;
    }

    public String GetPlayerName()
    {
        return m_playerName;
    }

    public boolean GetStatus()
    {
        return m_status;
    }

    @Override
    public Type GetType() {
        return Type.newPlayerAdded;
    }
}
