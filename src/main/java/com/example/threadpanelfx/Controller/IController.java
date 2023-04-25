package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Controller.Animation.ArrowAnimationRunnable;

import java.util.concurrent.CompletableFuture;

public interface IController {
    void OnStartGame();
    void OnStopGame();
    void OnShot(String playerName);
    void OnNewPlayerAdded(String playerName);
}
