package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Controller.Animation.ArrowAnimationRunnable;
import com.example.threadpanelfx.Controller.Animation.TargetsAnimationRunnable;
import com.example.threadpanelfx.Model.*;
import javafx.scene.shape.Circle;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ServerController implements IController {
    private IGameModel m_model;

    private final TargetsAnimationRunnable m_targetsAnimation;
    private final List<ArrowAnimationRunnable> m_arrowAnimations = Collections.synchronizedList(new LinkedList<>());

    public ServerController(IGameModel model, Circle circle1, Circle circle2) {
        this.m_model = model;

        m_targetsAnimation = new TargetsAnimationRunnable(m_model, circle1, circle2);
        new Thread(m_targetsAnimation).start();
    }

    private void StartGame()
    {
        m_model.SetGameStarted();
        m_model.AddReadyPlayers();
        m_model.ResetPlayerInfo();
        m_targetsAnimation.ResetCircles();
        m_targetsAnimation.PlayCircles();
    }

    private void ContinueGame()
    {
        m_model.SetGameStarted();
        m_targetsAnimation.PlayCircles();
        synchronized (m_arrowAnimations)
        {
            for (var arrowAnimation : m_arrowAnimations)
            {
                arrowAnimation.PlayArrow();
            }
        }
    }

    private Timer m_startGameTimer;

    private void onReadyForGameImpl(String playerName, Runnable action, long delay)
    {
        m_model.UpdatePlayerState(playerName, true);
        if (m_model.GetPlayerStatesCopy().size() == PlayerSettings.GetMaxPlayerCount() && m_model.PlayersReady())
        {
            // если число игроков достигло максимума - стартуем немедленно
            action.run();
        }
        else {
            m_startGameTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    while (!m_model.PlayersReady()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    action.run();
                }
            }, delay);
        }
    }

    @Override
    public void OnReadyForGame(String playerName) {
        synchronized (this) {
            if (m_startGameTimer != null)
            {
                m_startGameTimer.cancel();
            }
            m_startGameTimer = new Timer();
        }
        IGameModel.GameState gameState = m_model.GetGameState();
        if (gameState == IGameModel.GameState.stopped) {
            onReadyForGameImpl(playerName, this::StartGame, 3500);
        }
        else if (gameState == IGameModel.GameState.paused)
        {
            onReadyForGameImpl(playerName, this::ContinueGame, 2500);
        }
    }

    @Override
    public void OnPauseGame(String playerName) {
        m_model.SetGamePaused(playerName);
        m_targetsAnimation.PauseCircles();
        synchronized (m_arrowAnimations)
        {
            for (var arrowAnimation : m_arrowAnimations)
            {
                arrowAnimation.PauseArrow();
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
}
