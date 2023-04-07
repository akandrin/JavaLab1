package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Model.GameEvent.ArrowChanged;
import com.example.threadpanelfx.Model.GameEvent.GameEvent;
import com.example.threadpanelfx.Model.GameEvent.ScoresChanged;
import com.example.threadpanelfx.Model.GameEvent.TargetChanged;
import com.example.threadpanelfx.Model.IGameModel;
import com.example.threadpanelfx.Model.IObserver;
import com.example.threadpanelfx.Model.Observable;
import com.example.threadpanelfx.Model.PlayerInfo;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class LocalController {
    private IGameModel m_model;

    private AnimationThread animationThread;

    public LocalController(IGameModel model, Circle circle1, Circle circle2, Pane pane)
    {
        this.m_model = model;
        m_model.AddPlayer("");
        animationThread = new AnimationThread(m_model, circle1, circle2, pane);
        new Thread(animationThread).start();
    }

    public void OnStartGame()
    {
        m_model.SetScores("", 0);
        m_model.SetShots("", 0);
        animationThread.ResetCircles();
        animationThread.PlayCircles();
        animationThread.ResetArrowPosition();
    }

    public void OnStopGame()
    {
        animationThread.StopCircles();
        animationThread.StopArrow();
    }

    public void OnShot(String playerName)
    {
        int shots = m_model.GetShots(playerName);
        m_model.SetShots(playerName, shots + 1);
        animationThread.PlayArrow();
    }
}
