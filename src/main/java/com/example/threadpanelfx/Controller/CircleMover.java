package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Model.IGameModel;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class CircleMover {
    private Point2D m_circleCenter;

    final private Point2D m_layoutCoords;
    final private double m_radius;
    final private double initSpeed;
    private double currentSpeed;
    final private Point2D initPos;
    final private double lowerBound; // нижняя граница по Y
    final private double upperBound; // верхняя граница
    final private int m_targetNumber;

    private final IGameModel m_gameModel;

    public CircleMover(IGameModel model, int targetNumber, Circle circle, double initSpeed)
    {
        this.m_gameModel = model;
        this.m_targetNumber = targetNumber;
        this.m_circleCenter = new Point2D(circle.getCenterX(), circle.getCenterY());
        this.m_layoutCoords = new Point2D(circle.getLayoutX(), circle.getLayoutY());
        this.m_radius = circle.getRadius();
        this.initSpeed = initSpeed;
        ResetSpeed();
        this.initPos = m_circleCenter;
        this.lowerBound = -162 + circle.getRadius();
        this.upperBound = 177 - circle.getRadius();
    }

    public Point2D GetCenter()
    {
        return m_circleCenter.add(m_layoutCoords);
    }

    public double GetRadius()
    {
        return m_radius;
    }

    public void ResetPosition()
    {
        m_circleCenter = initPos;
        m_gameModel.SetTargetPosition(m_targetNumber, m_circleCenter);
    }

    public void ResetSpeed()
    {
        currentSpeed = initSpeed;
    }

    public void Move()
    {
        double currentPos = m_circleCenter.getY();
        if (currentPos < lowerBound || currentPos > upperBound)
        {
            currentSpeed = -currentSpeed;
        }
        m_circleCenter = new Point2D(m_circleCenter.getX(), currentPos + currentSpeed);
        m_gameModel.SetTargetPosition(m_targetNumber, m_circleCenter);
    }
}
