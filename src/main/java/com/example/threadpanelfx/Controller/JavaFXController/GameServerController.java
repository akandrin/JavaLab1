package com.example.threadpanelfx.Controller.JavaFXController;

import com.example.threadpanelfx.Controller.CurrentControllerHolder;
import com.example.threadpanelfx.Controller.MessageHandler.ServerMessageHandlerRunnable;
import com.example.threadpanelfx.Controller.ServerController;
import com.example.threadpanelfx.Model.Database.DatabaseController;
import com.example.threadpanelfx.Model.Database.Entry;
import com.example.threadpanelfx.Model.Database.HibernateSessionFactoryUtil;
import com.example.threadpanelfx.Model.GameEvent.*;
import com.example.threadpanelfx.Model.GameModelPool;
import com.example.threadpanelfx.Model.GameSettings;
import com.example.threadpanelfx.Model.IObservable;
import com.example.threadpanelfx.NetUtility.EventSender;
import com.example.threadpanelfx.NetUtility.MessengerPool;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class GameServerController extends GameFrameController {

    private DatabaseController m_databaseController;

    public GameServerController()
    {
        HibernateSessionFactoryUtil.getSessionFactory(); // первичная инициализация
        m_databaseController = new DatabaseController();
        var messageHandlerRunnable = new ServerMessageHandlerRunnable(MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncBroadcast), m_databaseController);
        new Thread(messageHandlerRunnable).start();
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


    protected void UpdateArrowCoords()
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

    private PauseTransition m_arrowCoordsUpdater = new PauseTransition(Duration.seconds(1));

    private void HandleEvent(GameStarted gameStarted)
    {
        // Выглядит как костыль.
        // Надо выполнять UpdateArrowCoords после того, как сцена будет перерисована
        // Но я не нашёл хорошего способа это сделать.
        // Поэтому просто полагаемся на то, что за 1 секунду сцена будет отрисована
        m_arrowCoordsUpdater.stop();
        m_arrowCoordsUpdater.setOnFinished(e -> {
            UpdateArrowCoords();
        });
        m_arrowCoordsUpdater.play();
    }

    private void HandleEvent(ScoresChanged scoresChanged)
    {
        String playerName = scoresChanged.GetPlayerName();
        int scores = scoresChanged.GetScores();
        if (scores >= GameSettings.GetScoresToWin())
        {
            controller.OnStopGame(playerName);
        }
    }

    private void HandleEvent(GameStopped gameStopped)
    {
        String winnerName = gameStopped.GetWinnerName();
        Entry entry = m_databaseController.findEntry(winnerName);
        if (entry == null)
        {
            m_databaseController.saveEntry(new Entry(winnerName, 1));
        }
        else
        {
            int winCount = entry.GetWinCount();
            entry.SetWinCount(winCount + 1);
            m_databaseController.updateEntry(entry);
        }
    }

    @Override
    public void Update(Object event) {
        super.Update(event);
        var gameEvent = (GameEvent)event;
        switch (gameEvent.GetType()) {
            case gameStarted:
                HandleEvent((GameStarted) gameEvent);
                break;
            case scoresChanged:
                HandleEvent((ScoresChanged) gameEvent);
                break;
            case gameStopped:
                HandleEvent((GameStopped) gameEvent);
                break;
        }
    }

    @FXML
    public void initialize() {
        controller = new ServerController(GameModelPool.Instance().GetModel(GameModelPool.ModelType.local), circle1, circle2);
        CurrentControllerHolder.Set(controller);
        ((IObservable)m_model).AddObserver(EventSender.Instance());
    }
}
