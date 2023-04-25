package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Controller.MessageHandler.MessageHandlerRunnable;
import com.example.threadpanelfx.Controller.MessageHandler.ServerMessageHandlerRunnable;
import com.example.threadpanelfx.Model.GameModelPool;
import com.example.threadpanelfx.Model.IObservable;
import com.example.threadpanelfx.NetUtility.EventSender;
import com.example.threadpanelfx.NetUtility.MessengerPool;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;

public class GameServerController extends GameFrameController {

    public GameServerController()
    {
        var messageHandlerRunnable = new ServerMessageHandlerRunnable(MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncBroadcast));
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


    @Override
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

    @FXML
    public void initialize() {
        controller = new ServerController(GameModelPool.Instance().GetModel(GameModelPool.ModelType.local), circle1, circle2);
        CurrentControllerHolder.Set(controller);
        ((IObservable)m_model).AddObserver(EventSender.Instance());
    }
}
