package com.example.threadpanelfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameController {
    @FXML
    Circle circle1, circle2;

    @FXML
    Pane arrow;

    @FXML
    Label scoresLabel, shotsLabel;

    @FXML
    Button startButton, stopButton, shotButton;

    private AnimationThread animationThread = null;
    private Timeline arrowCheckTimer = null;


    @FXML
    public void initialize() {
        animationThread = new AnimationThread(circle1, circle2, arrow);
        new Thread(animationThread).start();

        arrowCheckTimer = new Timeline(
                new KeyFrame(Duration.millis(100),
                        event -> {
                            if (ArrowHandler()) {
                                arrowCheckTimer.stop();
                                shotButton.setDisable(false);
                            }
                        }));
        arrowCheckTimer.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    public void OnStartGame()
    {
        scoresLabel.setText("0");
        shotsLabel.setText("0");
        animationThread.ResetCircles();
        animationThread.PlayCircles();
        animationThread.ResetArrowPosition();
        startButton.setDisable(true);
        stopButton.setDisable(false);
        shotButton.setDisable(false);
    }

    @FXML
    public void OnStopGame()
    {
        arrowCheckTimer.stop();
        animationThread.StopCircles();
        animationThread.StopArrow();
        startButton.setDisable(false);
        stopButton.setDisable(true);
        shotButton.setDisable(true);
    }

    @FXML
    public void OnShot()
    {
        shotButton.setDisable(true);
        AddValueToLabel(shotsLabel, 1);
        animationThread.PlayArrow();
        arrowCheckTimer.play();
    }


    private boolean IsArrowHitTarget(Circle circle)
    {
        double arrowX = arrow.getLayoutX() + arrow.getWidth();
        double arrowY = arrow.getLayoutY() + arrow.getHeight() / 2;
        double centerX = circle.getCenterX() + circle.getLayoutX();
        double centerY = circle.getCenterY() + circle.getLayoutY();
        double r = circle.getRadius();
        return Math.pow(arrowX - centerX, 2) + Math.pow(arrowY - centerY, 2) <= Math.pow(r, 2);
    }


    private boolean IsArrowMissing()
    {
        double arrowX = arrow.getLayoutX() + arrow.getWidth();
        return arrowX >= 450;
    }


    private void AddValueToLabel(Label label, int value)
    {
        String currentValueStr = label.getText();
        int currentValue = Integer.parseInt(currentValueStr);
        currentValue += value;
        label.setText(Integer.toString(currentValue));
    }


    private boolean ArrowHandler()
    {
        boolean isHandled = false;
        if (IsArrowHitTarget(circle1))
        {
            AddValueToLabel(scoresLabel, 1);
            isHandled = true;
        }
        else if (IsArrowHitTarget(circle2))
        {
            AddValueToLabel(scoresLabel, 2);
            isHandled = true;
        }
        else if (IsArrowMissing())
        {
            isHandled = true;
        }

        if (isHandled)
        {
            animationThread.StopArrow();
            animationThread.ResetArrowPosition();
        }

        return isHandled;
    }
}
