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


    protected void UpdateArrowCoords()
    {
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
}
