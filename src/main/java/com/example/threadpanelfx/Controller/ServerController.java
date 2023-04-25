package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Controller.Animation.ArrowAnimationRunnable;
import com.example.threadpanelfx.Controller.Animation.TargetsAnimationRunnable;
import com.example.threadpanelfx.Model.*;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ServerController implements IController {
    private IGameModel m_model;

    private TargetsAnimationRunnable m_targetsAnimation;
    private List<ArrowAnimationRunnable> m_arrowAnimations = Collections.synchronizedList(new LinkedList<>());

    public ServerController(IGameModel model, Circle circle1, Circle circle2) {
        this.m_model = model;

        m_targetsAnimation = new TargetsAnimationRunnable(m_model, circle1, circle2);
        new Thread(m_targetsAnimation).start();
    }

    @Override
    public void OnStartGame() {
        m_model.ResetPlayerInfo();
        m_targetsAnimation.ResetCircles();
        m_targetsAnimation.PlayCircles();
    }

    @Override
    public void OnStopGame() {
        m_targetsAnimation.StopCircles();
        synchronized (m_arrowAnimations)
        {
            for (var arrowAnimation : m_arrowAnimations)
            {
                arrowAnimation.StopArrow();
            }
        }
    }

    @Override
    public void OnShot(String playerName) {
        int shots = m_model.GetShots(playerName);
        m_model.SetShots(playerName, shots + 1);
        ArrowAnimationRunnable arrowAnimation = new ArrowAnimationRunnable(m_model, playerName, 25, m_targetsAnimation);
        m_arrowAnimations.add(arrowAnimation);
        CompletableFuture.runAsync(arrowAnimation).thenRun(()->{
            boolean result = m_arrowAnimations.remove(arrowAnimation);
            assert result;
        });
    }

    @Override
    public void OnNewPlayerAdded(String playerName)
    {
        m_model.AddPlayer(playerName);
    }
}
