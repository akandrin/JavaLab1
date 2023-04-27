package com.example.threadpanelfx.Controller.JavaFXController;

import com.example.threadpanelfx.Controller.IController;
import com.example.threadpanelfx.Model.GameEvent.GameEvent;
import com.example.threadpanelfx.Model.GameEvent.NewPlayerAdded;
import com.example.threadpanelfx.Model.PlayerSettings;
import com.example.threadpanelfx.View.View;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public abstract class GameFrameController extends View {
    @FXML
    protected HBox m_buttons;

    protected IController controller;
}
