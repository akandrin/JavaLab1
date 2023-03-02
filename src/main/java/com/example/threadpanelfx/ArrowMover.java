package com.example.threadpanelfx;

import javafx.scene.layout.Pane;
public class ArrowMover {
    final private Pane arrow;
    final private double speed;
    final private double startPos;

    public ArrowMover(Pane arrow, double speed)
    {
        this.arrow = arrow;
        this.speed = speed;
        this.startPos = arrow.getLayoutX();
    }

    public void ResetPosition()
    {
        arrow.setLayoutX(startPos);
    }

    public void Move()
    {
        double currentPos = arrow.getLayoutX();
        arrow.setLayoutX(currentPos + speed);
    }
}
