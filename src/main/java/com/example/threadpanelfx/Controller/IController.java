package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Controller.Animation.ArrowAnimationRunnable;

import java.util.concurrent.CompletableFuture;

public interface IController {
    void OnReadyForGame(String playerName);
    void OnPauseGame(String playerName);
    void OnShot(String playerName);
    void OnStopGame(String winnerName);
}
