package com.example.threadpanelfx.Model.GameEvent;

import com.example.threadpanelfx.Model.Utility.Point;
import javafx.geometry.Point2D;

import java.io.Serializable;

public class TargetChanged extends GameEvent {
    private int m_targetNumber; // номер мишени (нулевая/первая мишены)
    private Point m_newCoordsAbs;

    public TargetChanged(int targetNumber, Point2D newCoords)
    {
        this.m_targetNumber = targetNumber;
        this.m_newCoordsAbs = new Point(newCoords);
    }

    public int GetTargetNumber()
    {
        return m_targetNumber;
    }

    public Point GetCoordsAbs()
    {
        return m_newCoordsAbs;
    }

    @Override
    public Type GetType() {
        return Type.targetChanged;
    }
}
