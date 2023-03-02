package com.example.threadpanelfx;

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

    public AnimationThread(Circle circle1, Circle circle2, Pane arrow)
    {
        circleMovers.add(new CircleMover(circle1, 5));
        circleMovers.add(new CircleMover(circle2, 10));
        arrowMover = new ArrowMover(arrow, 25);
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
            }
        }
    }
}
