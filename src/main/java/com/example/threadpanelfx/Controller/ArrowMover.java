package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Model.IGameModel;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
public class ArrowMover {
    private Point2D m_arrowPosition;
    final private double m_width;
    final private double m_height;
    final private double speed;
    final private Point2D startPos;

    private IGameModel m_gameModel;

    public ArrowMover(IGameModel model, Pane arrow, double speed)
    {
        this.m_gameModel = model;
        this.m_arrowPosition = new Point2D(arrow.getLayoutX(), arrow.getLayoutY());
        this.m_width = arrow.getWidth();
        this.m_height = arrow.getHeight();
        this.speed = speed;
        this.startPos = m_arrowPosition;
    }

    public Point2D GetArrowPosition()
    {
        return m_arrowPosition;
    }

    public double GetArrowWidth()
    {
        return m_width;
    }

    public double GetArrowHeight()
    {
        return m_height;
    }

    public void ResetPosition()
    {
        m_arrowPosition = startPos;
        m_gameModel.SetArrowPosition("", false, m_arrowPosition);
    }

    public void Move()
    {
        double currentPos = m_arrowPosition.getX();
        currentPos += speed;
        m_arrowPosition = new Point2D(currentPos, m_arrowPosition.getY());
        m_gameModel.SetArrowPosition("", true, m_arrowPosition);
    }
}
