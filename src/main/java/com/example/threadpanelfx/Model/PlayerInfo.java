package com.example.threadpanelfx.Model;

import javafx.geometry.Point2D;
import javafx.scene.effect.Light;

public class PlayerInfo {
    public String name;
    public boolean arrowIsVisible;
    public double arrowOffset;
    public Point2D arrowHeadAbs;
    public int scores;
    public int shots;

    public PlayerInfo()
    {
        name = "";
        Reset();
    }

    public void Reset()
    {
        arrowIsVisible = false;
        arrowOffset = 0;
        arrowHeadAbs = null;
        scores = 0;
        shots = 0;
    }
}
