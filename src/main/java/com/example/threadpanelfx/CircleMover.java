package com.example.threadpanelfx;

import javafx.scene.shape.Circle;

public class CircleMover {
    final private Circle circle;
    final private double initSpeed;
    private double currentSpeed;
    final private double initPos;
    final private double lowerBound; // нижняя граница по Y
    final private double upperBound; // верхняя граница

    public CircleMover(Circle circle, double initSpeed)
    {
        this.circle = circle;
        this.initSpeed = initSpeed;
        ResetSpeed();
        this.initPos = circle.getCenterY();
        this.lowerBound = -162 + circle.getRadius();
        this.upperBound = 177 - circle.getRadius();
    }

    public void ResetPosition()
    {
        circle.setCenterY(initPos);
    }

    public void ResetSpeed()
    {
        currentSpeed = initSpeed;
    }

    public void Move()
    {
        double currentPos = circle.getCenterY();
        if (currentPos < lowerBound || currentPos > upperBound)
        {
            currentSpeed = -currentSpeed;
        }
        circle.setCenterY(currentPos + currentSpeed);
    }
}
