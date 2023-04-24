package com.example.threadpanelfx.Controller.Animation;

import com.example.threadpanelfx.Model.IGameModel;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
public class ArrowMover {
    private final IGameModel m_gameModel;
    private final String m_playerName;

    private double m_arrowOffset;
    final private Point2D m_arrowHeadStartPositionAbs;
    final private double speed;
    final private double startOffset;

    public ArrowMover(IGameModel model, String playerName, double arrowOffset, Point2D arrowHeadStartPositionAbs, double speed)
    {
        this.m_gameModel = model;
        this.m_playerName = playerName;
        this.m_arrowOffset = arrowOffset;
        this.m_arrowHeadStartPositionAbs = arrowHeadStartPositionAbs;
        this.speed = speed;
        this.startOffset = m_arrowOffset;
    }

    public double GetArrowOffset()
    {
        return m_arrowOffset;
    }

    public Point2D GetArrowHeadStartPositionAbs()
    {
        return m_arrowHeadStartPositionAbs;
    }

    public void ResetPosition()
    {
        m_arrowOffset = startOffset;
        m_gameModel.SetArrowOffset(m_playerName, false, m_arrowOffset);
    }

    public void Move()
    {
        m_arrowOffset += speed;
        m_gameModel.SetArrowOffset(m_playerName, true, m_arrowOffset);
    }
}
