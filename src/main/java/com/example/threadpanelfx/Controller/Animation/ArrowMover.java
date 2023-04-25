package com.example.threadpanelfx.Controller.Animation;

import com.example.threadpanelfx.Model.IGameModel;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
public class ArrowMover {
    private final IGameModel m_gameModel;
    private final String m_playerName;

    final private double speed;

    public ArrowMover(IGameModel model, String playerName, double speed)
    {
        this.m_gameModel = model;
        this.m_playerName = playerName;
        this.speed = speed;
    }

    public void Move()
    {
        double arrowOffset = m_gameModel.GetArrowOffset(m_playerName);
        arrowOffset += speed;
        m_gameModel.SetArrowOffset(m_playerName, true, arrowOffset);
    }
}
