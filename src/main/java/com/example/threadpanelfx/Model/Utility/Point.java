package com.example.threadpanelfx.Model.Utility;

import javafx.geometry.Point2D;

import java.io.Serializable;

public class Point implements Serializable {
    private Double x;
    private Double y;

    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public Point(Point2D point)
    {
        this.x = point.getX();
        this.y = point.getY();
    }

    public Point2D ToPoint2D()
    {
        return new Point2D(x, y);
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public double setY()
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
    }
}
