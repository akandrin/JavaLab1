package com.example.threadpanelfx.View;

import com.example.threadpanelfx.Model.GameEvent.*;
import com.example.threadpanelfx.Model.GameModelPool;
import com.example.threadpanelfx.Model.IGameModel;
import com.example.threadpanelfx.Model.IObserver;
import com.example.threadpanelfx.Model.Observable;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class View implements IObserver {
    @FXML
    protected Circle circle1, circle2;

    @FXML
    protected Pane arrow;

    @FXML
    protected Label scoresLabel, shotsLabel;

    public View()
    {
        IGameModel model = GameModelPool.Instance().GetModel(GameModelPool.ModelType.local);
        assert model instanceof Observable;
        ((Observable) model).AddObserver(this);
    }

    private void HandleEvent(ArrowChanged event)
    {
        boolean isArrowVisible = event.IsArrowVisible();
        arrow.setVisible(isArrowVisible);
        if (isArrowVisible)
        {
            Point2D coords = event.GetCoords();
            arrow.setLayoutX(coords.getX());
            arrow.setLayoutY(coords.getY());
        }
    }

    private void HandleEvent(TargetChanged event)
    {
        int targetNumber = event.GetTargetNumber();
        Circle target = null;
        if (targetNumber == 0)
            target = circle1;
        else if (targetNumber == 1)
            target = circle2;

        assert target != null;

        Point2D coords = event.GetCoords();
        target.setCenterX(coords.getX());
        target.setCenterY(coords.getY());
    }

    private void HandleEvent(ScoresChanged event)
    {
        scoresLabel.setText(String.valueOf(event.GetScores()));
    }

    private void HandleEvent(ShotsChanged event)
    {
        shotsLabel.setText(String.valueOf(event.GetShots()));
    }

    @Override
    public void Update(Object event) {
        var gameEvent = (GameEvent)event;
        switch (gameEvent.GetType())
        {
            case arrowChanged:
                HandleEvent((ArrowChanged) gameEvent);
                break;
            case targetChanged:
                HandleEvent((TargetChanged) gameEvent);
                break;
            case scoresChanged:
                HandleEvent((ScoresChanged) gameEvent);
                break;
            case shotsChanged:
                HandleEvent((ShotsChanged)gameEvent);
                break;
            default:
                throw new RuntimeException("Unknown game event type" + gameEvent.GetType());
        }
    }

    @Override
    public boolean CanImmediatelyUpdate() {
        return false;
    }
}
