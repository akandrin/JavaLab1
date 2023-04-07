package com.example.threadpanelfx.Model.GameEvent;

import javafx.geometry.Point2D;

public class ArrowChanged implements GameEvent {
    private boolean m_arrowIsVisible;
    private Point2D m_newCoords;

    public ArrowChanged(boolean arrowIsVisible, Point2D newCoords)
    {
        m_arrowIsVisible = arrowIsVisible;
        m_newCoords = newCoords;
    }

    public boolean IsArrowVisible()
    {
        return m_arrowIsVisible;
    }

    public Point2D GetCoords()
    {
        return m_newCoords;
    }

    @Override
    public Type GetType() {
        return Type.arrowChanged;
    }
}
