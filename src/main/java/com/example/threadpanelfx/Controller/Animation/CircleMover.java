package com.example.threadpanelfx.Controller.Animation;

import com.example.threadpanelfx.Model.IGameModel;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;

public class CircleMover {
    private Point2D m_circleCenterAbs;

    // final private Point2D m_layoutCoords;
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
        var circleCenter = new Point2D(circle.getCenterX(), circle.getCenterY());
        m_circleCenterAbs = circle.localToScene(circleCenter);
        // this.m_layoutCoords = new Point2D(circle.getLayoutX(), circle.getLayoutY());
        this.m_radius = circle.getRadius();
        this.initSpeed = initSpeed;
        ResetSpeed();
        this.initPos = m_circleCenterAbs;
        this.lowerBound = circle.localToScene(new Point2D(0, -162 + circle.getRadius())).getY();
        this.upperBound = circle.localToScene(new Point2D(0, 177 - circle.getRadius())).getY();
    }

    public Point2D GetCenterAbs()
    {
        return m_circleCenterAbs;/*.add(m_layoutCoords);*/
    }

    public double GetRadius()
    {
        return m_radius;
    }

    public void ResetPosition()
    {
        m_circleCenterAbs = initPos;
        m_gameModel.SetTargetPositionAbs(m_targetNumber, m_circleCenterAbs);
    }

    public void ResetSpeed()
    {
        currentSpeed = initSpeed;
    }

    public void Move()
    {
        double currentPos = m_circleCenterAbs.getY();
        if (currentPos < lowerBound || currentPos > upperBound)
        {
            currentSpeed = -currentSpeed;
        }
        m_circleCenterAbs = new Point2D(m_circleCenterAbs.getX(), currentPos + currentSpeed);
        m_gameModel.SetTargetPositionAbs(m_targetNumber, m_circleCenterAbs);
    }
}
