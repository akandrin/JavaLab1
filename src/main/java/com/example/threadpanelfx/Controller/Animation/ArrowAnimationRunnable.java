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

    public ArrowAnimationRunnable(IGameModel gameModel, String playerName, double speed, TargetsAnimationRunnable targetsAnimation) {
        this.m_gameModel = gameModel;
        this.m_playerName = playerName;
        this.m_targetsAnimation = targetsAnimation;
        this.m_arrowMover = new ArrowMover(m_gameModel, m_playerName, speed);
    }

    public void PlayArrow()
    {
        m_isArrowShot.set(true);
    }

    public void PauseArrow()
    {
        m_isArrowShot.set(false);
    }

    private boolean isArrowMissing() {
        double arrowX = m_gameModel.GetArrowOffset(m_playerName);
        return arrowX >= 450;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!m_isArrowShot.get())
                continue;
            m_arrowMover.Move();
            if (isArrowMissing()) {
                break;
            } else {
                double arrowOffset = m_gameModel.GetArrowOffset(m_playerName);
                Point2D arrowHeadStartPositionAbs = m_gameModel.GetArrowHeadStartPositionAbs(m_playerName);
                if (arrowHeadStartPositionAbs != null) {
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
                }
            }
        }
        m_gameModel.SetArrowOffset(m_playerName, false, 0);
    }
}
