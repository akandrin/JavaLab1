package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Model.GameEvent.ArrowChanged;
import com.example.threadpanelfx.Model.GameEvent.GameEvent;
import com.example.threadpanelfx.Model.GameModelPool;
import com.example.threadpanelfx.View.View;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GameClientController extends View {
    @FXML
    Button startButton, stopButton, shotButton;

    private ClientController controller;

    @FXML
    public void initialize() {
        controller = new ClientController(GameModelPool.Instance().GetModel(GameModelPool.ModelType.local), circle1, circle2);
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

    int counter = 0;

    @FXML
    public void OnStartGame()
    {
        counter++;
        controller.OnNewPlayerAdded("Some name" + counter); // todo : change
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
