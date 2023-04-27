package com.example.threadpanelfx.Controller.Animation;

import com.example.threadpanelfx.Model.IGameModel;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TargetsAnimationRunnable implements Runnable {

    private AtomicBoolean areCirclesMoving = new AtomicBoolean(false);

    final private List<CircleMover> circleMovers = new ArrayList<>();

    final IGameModel m_gameModel;

    public TargetsAnimationRunnable(IGameModel model, Circle circle1, Circle circle2)
    {
        this.m_gameModel = model;
        circleMovers.add(new CircleMover(model, 0, circle1, 5));
        circleMovers.add(new CircleMover(model,1, circle2, 10));
    }

    public void ResetCircles()
    {
        synchronized (circleMovers)
        {
            for (var circleMover : circleMovers)
            {
                circleMover.ResetSpeed();
                circleMover.ResetPosition();
            }
        }
    }

    public void PlayCircles()
    {
        areCirclesMoving.set(true);
    }

    public void PauseCircles()
    {
        areCirclesMoving.set(false);
    }

    private boolean isArrowHitTarget(CircleMover circleMover, Point2D arrowHeadAbs)
    {
        double arrowX = arrowHeadAbs.getX();
        double arrowY = arrowHeadAbs.getY();
        double centerX = circleMover.GetCenterAbs().getX();
        double centerY = circleMover.GetCenterAbs().getY();
        double r = circleMover.GetRadius();
        return Math.pow(arrowX - centerX, 2) + Math.pow(arrowY - centerY, 2) <= Math.pow(r, 2);
    }

    public int GetHitTarget(Point2D arrowHeadAbs)
    {
        int currentMover = 0;
        for (var circleMover : circleMovers)
        {
            if (isArrowHitTarget(circleMover, arrowHeadAbs))
                return currentMover;

            currentMover += 1;
        }
        return -1;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (areCirclesMoving.get()) {
                for (var circleMover : circleMovers)
                    circleMover.Move();
            }
        }
    }
}
