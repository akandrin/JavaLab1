package com.example.threadpanelfx.Controller;

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

    protected void ActionBeforeNewPlayerAdded()
    {
    }

    protected void ActionAfterNewPlayerAdded()
    {
    }


    private void HandleEvent(NewPlayerAdded newPlayerAdded)
    {
        // Выглядит как костыль.
        // Надо выполнять UpdateArrowCoords после того, как сцена будет перерисована
        // Но я не нашёл хорошего способа это сделать.
        // Поэтому просто полагаемся на то, что за 1 секунду сцена будет отрисована
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
        ActionBeforeNewPlayerAdded();
        pauseTransition.setOnFinished(e -> {
            UpdateArrowCoords();
            ActionAfterNewPlayerAdded();
        });
        pauseTransition.play();
    }

    @Override
    public void Update(Object event) {
        super.Update(event);
        var gameEvent = (GameEvent)event;
        if (gameEvent.GetType() == GameEvent.Type.newPlayerAdded)
        {
            HandleEvent((NewPlayerAdded) gameEvent);
        }
    }

    protected int counter = 2;

    public void OnStartGame()
    {
        controller.OnNewPlayerAdded("Some name" + counter); // todo : change
        controller.OnStartGame();
    }

    public void OnStopGame()
    {
        controller.OnStopGame();
    }

    public void OnShot()
    {
        controller.OnShot(PlayerSettings.GetPlayerName());
    }
}
