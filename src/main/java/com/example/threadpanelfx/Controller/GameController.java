package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Controller.AnimationThread;
import com.example.threadpanelfx.Model.GameEvent.ArrowChanged;
import com.example.threadpanelfx.Model.GameEvent.GameEvent;
import com.example.threadpanelfx.Model.GameEvent.ScoresChanged;
import com.example.threadpanelfx.Model.GameEvent.TargetChanged;
import com.example.threadpanelfx.Model.GameModelPool;
import com.example.threadpanelfx.Model.IObserver;
import com.example.threadpanelfx.View.View;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class GameController extends View {
    @FXML
    Button startButton, stopButton, shotButton;

    private LocalController controller;

    @FXML
    public void initialize() {
        controller = new LocalController(GameModelPool.Instance().GetModel(GameModelPool.ModelType.local), circle1, circle2, arrow);
    }

    private void HandleEvent(ArrowChanged arrowChanged)
    {
        boolean isArrowVisible = arrowChanged.IsArrowVisible();
        shotButton.setDisable(isArrowVisible);
    }

    @Override
    public void Update(Object event) {
        super.Update(event);
        var gameEvent = (GameEvent)event;
        if (gameEvent.GetType() == GameEvent.Type.arrowChanged) {
            HandleEvent((ArrowChanged) gameEvent);
        }
    }

    @FXML
    public void OnStartGame()
    {
        controller.OnStartGame();
        startButton.setDisable(true);
        stopButton.setDisable(false);
        shotButton.setDisable(false);
    }

    @FXML
    public void OnStopGame()
    {
        controller.OnStopGame();
        startButton.setDisable(false);
        stopButton.setDisable(true);
        shotButton.setDisable(true);
    }

    @FXML
    public void OnShot()
    {
        shotButton.setDisable(true);
        controller.OnShot("");
    }
}
