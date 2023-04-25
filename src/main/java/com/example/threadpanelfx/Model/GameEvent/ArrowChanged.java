package com.example.threadpanelfx.Model.GameEvent;

import java.io.Serializable;


public class ArrowChanged extends GameEvent {
    private boolean m_arrowIsVisible;
    private double m_newOffset; // смещение стрелы по оси X
    private String m_playerName;

    public ArrowChanged(boolean arrowIsVisible, double newOffset, String playerName)
    {
        this.m_arrowIsVisible = arrowIsVisible;
        this.m_newOffset = newOffset;
        this.m_playerName = playerName;
    }

    public boolean IsArrowVisible()
    {
        return m_arrowIsVisible;
    }

    public double GetOffset()
    {
        return m_newOffset;
    }

    public String GetPlayerName() { return m_playerName; }

    @Override
    public Type GetType() {
        return Type.arrowChanged;
    }
}
