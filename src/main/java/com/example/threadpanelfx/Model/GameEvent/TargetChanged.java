package com.example.threadpanelfx.Model.GameEvent;

import javafx.geometry.Point2D;

public class TargetChanged implements GameEvent {
    private int m_targetNumber; // номер мишени (нулевая/первая мишены)
    private Point2D m_newCoords;

    public TargetChanged(int targetNumber, Point2D newCoords)
    {
        this.m_targetNumber = targetNumber;
        this.m_newCoords = newCoords;
    }

    public int GetTargetNumber()
    {
        return m_targetNumber;
    }

    public Point2D GetCoords()
    {
        return m_newCoords;
    }

    @Override
    public Type GetType() {
        return Type.targetChanged;
    }
}
