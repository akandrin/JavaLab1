package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Controller.Animation.ArrowAnimationRunnable;
import com.example.threadpanelfx.Controller.Animation.TargetsAnimationRunnable;
import com.example.threadpanelfx.Model.*;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerController implements IController {
    private IGameModel m_model;

    private TargetsAnimationRunnable m_targetsAnimation;
    private List<ArrowAnimationRunnable> m_arrowAnimations = Collections.synchronizedList(new LinkedList<>());

    public ServerController(IGameModel model, Circle circle1, Circle circle2) {
        this.m_model = model;

        m_targetsAnimation = new TargetsAnimationRunnable(m_model, circle1, circle2);
        new Thread(m_targetsAnimation).start();
    }

    private AtomicBoolean m_gameStarted = new AtomicBoolean(false);
    private void StartGame()
    {
        m_gameStarted.set(true);
        m_model.AddReadyPlayers();
        m_model.ClearPlayersBeforeGameStarts();
        m_model.ResetPlayerInfo();
        m_targetsAnimation.ResetCircles();
        m_targetsAnimation.PlayCircles();
    }

    private Timer m_startGameTimer;

    @Override
    public void OnReadyForGame(String playerName) {
        synchronized (this) {
            if (m_startGameTimer != null)
            {
                m_startGameTimer.cancel();
            }
            m_startGameTimer = new Timer();
        }
        if (!m_gameStarted.get()) {
            m_model.UpdatePlayerBeforeGameStarts(playerName, true);
            m_startGameTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    while (!m_model.PlayersReady())
                    {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    StartGame();
                }
            }, 3500);
        }
    }

    @Override
    public void OnStopGame() {
        m_gameStarted.set(false);
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
