package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Model.IGameModel;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AnimationThread implements Runnable {

    private AtomicBoolean areCirclesMoving = new AtomicBoolean(false);
    private AtomicBoolean isArrowShot = new AtomicBoolean(false);

    final private List<CircleMover> circleMovers = new ArrayList<>();
    final ArrowMover arrowMover;

    final IGameModel m_gameModel;

    public AnimationThread(IGameModel model, Circle circle1, Circle circle2, Pane arrow)
    {
        this.m_gameModel = model;
        circleMovers.add(new CircleMover(model, 0, circle1, 5));
        circleMovers.add(new CircleMover(model,1, circle2, 10));
        arrowMover = new ArrowMover(model, arrow, 25);
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

    public void StopCircles()
    {
        areCirclesMoving.set(false);
    }

    public void PlayArrow()
    {
        isArrowShot.set(true);
    }

    public void StopArrow()
    {
        isArrowShot.set(false);
    }

    public void ResetArrowPosition()
    {
        arrowMover.ResetPosition();
    }

    private boolean isArrowMissing()
    {
        double arrowX = arrowMover.GetArrowPosition().getX() + arrowMover.GetArrowWidth();
        return arrowX >= 450;
    }

    private boolean isArrowHitTarget(CircleMover circleMover, ArrowMover arrowMover)
    {
        double arrowX = arrowMover.GetArrowPosition().getX() + arrowMover.GetArrowWidth();
        double arrowY = arrowMover.GetArrowPosition().getY() + arrowMover.GetArrowHeight() / 2;
        double centerX = circleMover.GetCenter().getX();
        double centerY = circleMover.GetCenter().getY();
        double r = circleMover.GetRadius();
        return Math.pow(arrowX - centerX, 2) + Math.pow(arrowY - centerY, 2) <= Math.pow(r, 2);
    }

    private int GetHitTarget()
    {
        int currentMover = 0;
        for (var circleMover : circleMovers)
        {
            if (isArrowHitTarget(circleMover, arrowMover))
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

            if (isArrowShot.get()) {
                arrowMover.Move();
                if (isArrowMissing())
                {
                    isArrowShot.set(false);
                    m_gameModel.SetArrowPosition("", false, null);
                    ResetArrowPosition();
                }
                else
                {
                    int hitTarget = GetHitTarget();
                    if (hitTarget != -1)
                    {
                        isArrowShot.set(false);
                        m_gameModel.SetArrowPosition("", false, null);
                        int scores = m_gameModel.GetScores("");
                        if (hitTarget == 0)
                            scores += 1;
                        else if (hitTarget == 1)
                            scores += 2;
                        else
                        {
                            throw new RuntimeException("Unknown target number");
                        }
                        m_gameModel.SetScores("", scores);
                        ResetArrowPosition();
                    }
                }
            }
        }
    }
}
