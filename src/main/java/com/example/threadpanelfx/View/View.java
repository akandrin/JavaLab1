package com.example.threadpanelfx.View;

import com.example.threadpanelfx.Model.GameEvent.*;
import com.example.threadpanelfx.Model.GameModelPool;
import com.example.threadpanelfx.Model.IGameModel;
import com.example.threadpanelfx.Model.IObserver;
import com.example.threadpanelfx.Model.IObservable;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class View implements IObserver {
    @FXML
    protected Circle circle1, circle2;

    @FXML
    protected Label scoresLabel, shotsLabel;

    @FXML
    protected VBox playersVBox;

    @FXML
    protected VBox m_arrowsVBox;

    protected final Map<String/*playerName*/, HBox/*arrow (HBox with stretchre and Polygon)*/> m_arrows = new ConcurrentHashMap<>();

    private IGameModel m_model;

    public View()
    {
        m_model = GameModelPool.Instance().GetModel(GameModelPool.ModelType.local);
        assert m_model instanceof IObservable;
        ((IObservable) m_model).AddObserver(this);
    }

    private void HandleEvent(ArrowChanged event)
    {
        boolean isArrowVisible = event.IsArrowVisible();
        HBox arrowBox = m_arrows.get(event.GetPlayerName());

        arrowBox.setVisible(isArrowVisible);
        if (isArrowVisible)
        {
            double arrowOffset = event.GetOffset();
            Line stretcher = (Line)arrowBox.getChildren().get(0);
            stretcher.setEndX(arrowOffset);
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

        Point2D coordsAbs = event.GetCoordsAbs();
        Point2D coords = target.sceneToLocal(coordsAbs);
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

    private Point2D GetArrowHead(Polygon arrow)
    {
        var points = arrow.getPoints();
        // ищем индекс максимальной x-координаты
        double maxCoord = -Double.MAX_VALUE;
        int maxIndex = 0;
        for (int i = 0; i < points.size(); i += 2)
        {
            double xCoord = points.get(i);
            if (xCoord > maxCoord)
            {
                maxCoord = xCoord;
                maxIndex = i;
            }
        }
        return new Point2D(points.get(maxIndex), points.get(maxIndex + 1));
    }

    private void UpdateArrowCoords()
    {
        synchronized (m_arrows) {
            for (var arrowInfo : m_arrows.entrySet()) {
                String playerName = arrowInfo.getKey();
                HBox arrowBox = arrowInfo.getValue();
                Polygon arrow = (Polygon) arrowBox.getChildren().get(1);

                Point2D arrowHead = GetArrowHead(arrow);
                Point2D arrowHeadAbs = arrow.localToScene(arrowHead);
                m_model.SetArrowHeadStartPositionAbs(playerName, arrowHeadAbs);
            }
        }
    }

    private static void UpdateBoxes(VBox box)
    {
        var height = box.getHeight();
        var boxesCount = box.getChildren().size();
        var prefHeight = height / boxesCount;
        for (var node : box.getChildren())
        {
            ((Pane)node).setPrefHeight(prefHeight);
        }
    }

    private void HandleEvent(NewPlayerAdded event)
    {
        String playerName = event.GetPlayerName();

        VBox playerVBox = new VBox();
        playerVBox.setAlignment(Pos.CENTER_LEFT);
        Polygon playerTriangle = new Polygon();
        playerTriangle.getPoints().addAll(0.0, 24.0, 19.0, 0.0, 0.0, -24.0);
        playerVBox.getChildren().add(playerTriangle);
        playerVBox.getChildren().add(new Label(playerName));
        playersVBox.getChildren().add(playerVBox);
        UpdateBoxes(playersVBox);

        // контейнер для стрелы (используется для выравнивания)
        HBox arrowBox = new HBox();
        arrowBox.setAlignment(Pos.CENTER_LEFT);
        arrowBox.setVisible(true);

        // создание стрелы
        Line stretcher = new Line(); // растяжение этого отрезка перемещает стрелу
        stretcher.setVisible(false);

        Polygon arrow = new Polygon(); // стрела
        arrow.getPoints().addAll(-40.0, 0.5, 0.0, 0.5, 0.0, 5.0, 10.0, 0.0, 0.0, -5.0, 0.0, -0.5, -40.0, -0.5);
        arrowBox.getChildren().addAll(stretcher, arrow);

        m_arrowsVBox.getChildren().add(arrowBox);
        m_arrows.put(playerName, arrowBox);

        UpdateArrowCoords();
        UpdateBoxes(m_arrowsVBox);
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
            case newPlayerAdded:
                HandleEvent((NewPlayerAdded)gameEvent);
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
