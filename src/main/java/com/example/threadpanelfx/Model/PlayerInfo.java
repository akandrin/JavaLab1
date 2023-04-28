package com.example.threadpanelfx.Model;

import javafx.geometry.Point2D;
import javafx.scene.effect.Light;

public class PlayerInfo implements Cloneable {
    public String name;
    public boolean playerIsReady;

    public boolean arrowIsVisible;
    public double arrowOffset;
    public Point2D arrowHeadAbs;
    public int scores;
    public int shots;

    public PlayerInfo()
    {
        name = "";
        playerIsReady = false;
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

    @Override
    public PlayerInfo clone() {
        try {
            PlayerInfo clone = (PlayerInfo) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
