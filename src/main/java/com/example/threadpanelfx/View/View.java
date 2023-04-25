package com.example.threadpanelfx.View;

import com.example.threadpanelfx.Model.GameEvent.*;
import com.example.threadpanelfx.Model.GameModelPool;
import com.example.threadpanelfx.Model.IGameModel;
import com.example.threadpanelfx.Model.IObserver;
import com.example.threadpanelfx.Model.IObservable;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class View implements IObserver {
    @FXML
    protected Circle circle1, circle2;

    @FXML
    protected VBox playersVBox;

    @FXML
    protected VBox m_arrowsVBox;

    @FXML
    protected VBox m_playersInfoBox;

    protected final Map<String/*playerName*/, HBox/*arrow (HBox with stretchre and Polygon)*/> m_arrows = new ConcurrentHashMap<>();
    protected final Map<String/*playerName*/, VBox/*player info*/> m_playerInfoBoxMap = new ConcurrentHashMap<>();

    protected IGameModel m_model;

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

        double arrowOffset = event.GetOffset();
        Line stretcher = (Line)arrowBox.getChildren().get(0);
        stretcher.setEndX(arrowOffset);
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
        String playerName = event.GetPlayerName();
        VBox playerInfoBox = m_playerInfoBoxMap.get(playerName);
        Label scoresLabel = getScoresLabelFromPlayerInfoBox(playerInfoBox);
        scoresLabel.setText(String.valueOf(event.GetScores()));
    }

    private void HandleEvent(ShotsChanged event)
    {
        String playerName = event.GetPlayerName();
        VBox playerInfoBox = m_playerInfoBoxMap.get(playerName);
        Label shotsLabel = getShotsLabelFromPlayerInfoBox(playerInfoBox);
        shotsLabel.setText(String.valueOf(event.GetShots()));
    }

    private static void AlignChildBoxes(VBox box)
    {
        var height = box.getHeight();
        var boxesCount = box.getChildren().size();
        var prefHeight = height / boxesCount;
        for (var node : box.getChildren())
        {
            ((Pane)node).setPrefHeight(prefHeight);
        }
    }

    static private VBox getPlayerBox(String playerName)
    {
        VBox playerVBox = new VBox();
        playerVBox.setAlignment(Pos.CENTER_LEFT);
        Polygon playerTriangle = new Polygon();
        playerTriangle.getPoints().addAll(0.0, 24.0, 19.0, 0.0, 0.0, -24.0);
        playerVBox.getChildren().add(playerTriangle);
        playerVBox.getChildren().add(new Label(playerName));
        return playerVBox;
    }

    static private HBox getArrowBox()
    {
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

        return arrowBox;
    }

    static private VBox getPlayerInfoBox(String playerName)
    {
        Function<String, Label> getBoldLabel = (String str) -> {
            var label = new Label(str); label.setStyle("-fx-font-weight: bold;");
            return label;
        };

        HBox playerNameBox = new HBox();
        playerNameBox.getChildren().addAll(getBoldLabel.apply("Игрок: "), new Label(playerName));

        HBox playerShotsBox = new HBox();
        playerShotsBox.getChildren().addAll(getBoldLabel.apply("Счет игрока: "), new Label("0"));

        HBox playerScoresBox = new HBox();
        playerScoresBox.getChildren().addAll(getBoldLabel.apply("Выстрелов: "), new Label("0"));

        VBox playerInfoBox = new VBox();
        playerInfoBox.getChildren().addAll(playerNameBox, playerShotsBox, playerScoresBox);
        return playerInfoBox;
    }

    static private Label getScoresLabelFromPlayerInfoBox(VBox playerInfoBox)
    {
        HBox playerScoresBox = (HBox)playerInfoBox.getChildren().get(1);
        return (Label)playerScoresBox.getChildren().get(1);
    }

    static private Label getShotsLabelFromPlayerInfoBox(VBox playerInfoBox)
    {
        HBox playerScoresBox = (HBox)playerInfoBox.getChildren().get(2);
        return (Label)playerScoresBox.getChildren().get(1);
    }

    private void HandleEvent(NewPlayerAdded event)
    {
        String playerName = event.GetPlayerName();

        playersVBox.getChildren().add(getPlayerBox(playerName));
        AlignChildBoxes(playersVBox);

        HBox arrowBox = getArrowBox();
        m_arrowsVBox.getChildren().add(arrowBox);
        m_arrows.put(playerName, arrowBox);
        AlignChildBoxes(m_arrowsVBox);

        VBox playerInfoBox = getPlayerInfoBox(playerName);
        m_playerInfoBoxMap.put(playerName, playerInfoBox);
        m_playersInfoBox.getChildren().add(playerInfoBox);
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
