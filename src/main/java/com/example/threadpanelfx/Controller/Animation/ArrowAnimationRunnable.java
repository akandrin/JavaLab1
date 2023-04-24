package com.example.threadpanelfx.Controller.Animation;

import com.example.threadpanelfx.Model.IGameModel;
import javafx.geometry.Point2D;

import java.util.concurrent.atomic.AtomicBoolean;

public class ArrowAnimationRunnable implements Runnable {
    final IGameModel m_gameModel;
    final ArrowMover m_arrowMover;
    final TargetsAnimationRunnable m_targetsAnimation; // ссылка на m_targetsAnimation
    final String m_playerName;

    private final AtomicBoolean m_isArrowShot = new AtomicBoolean(true);

    public ArrowAnimationRunnable(IGameModel gameModel, String playerName, Point2D arrowHeadStartPositionAbs, double speed, TargetsAnimationRunnable targetsAnimation) {
        this.m_gameModel = gameModel;
        this.m_playerName = playerName;
        this.m_targetsAnimation = targetsAnimation;

        double arrowStartOffset = m_gameModel.GetArrowOffset(playerName);
        this.m_arrowMover = new ArrowMover(m_gameModel, m_playerName, arrowStartOffset, arrowHeadStartPositionAbs, speed);
    }

    public void StopArrow()
    {
        m_isArrowShot.set(false);
    }

    private boolean isArrowMissing() {
        double arrowX = m_arrowMover.GetArrowOffset();
        return arrowX >= 450;
    }

    @Override
    public void run() {
        while (m_isArrowShot.get()) {
            m_arrowMover.Move();
            if (isArrowMissing()) {
                break;
            } else {
                double arrowOffset = m_arrowMover.GetArrowOffset();
                Point2D arrowHeadStartPositionAbs = m_arrowMover.GetArrowHeadStartPositionAbs();
                Point2D arrowHeadPositionAbs = new Point2D(arrowHeadStartPositionAbs.getX() + arrowOffset, arrowHeadStartPositionAbs.getY());
                int hitTarget = m_targetsAnimation.GetHitTarget(arrowHeadPositionAbs);
                if (hitTarget != -1) {
                    int scores = m_gameModel.GetScores(m_playerName);
                    if (hitTarget == 0)
                        scores += 1;
                    else if (hitTarget == 1)
                        scores += 2;
                    else {
                        throw new RuntimeException("Unknown target number");
                    }
                    m_gameModel.SetScores(m_playerName, scores);
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        m_gameModel.SetArrowOffset(m_playerName, false, 0);
    }
}
