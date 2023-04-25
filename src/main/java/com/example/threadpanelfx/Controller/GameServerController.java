package com.example.threadpanelfx.Controller;

import com.example.threadpanelfx.Controller.MessageHandler.MessageHandlerRunnable;
import com.example.threadpanelfx.Controller.MessageHandler.ServerMessageHandlerRunnable;
import com.example.threadpanelfx.Model.GameModelPool;
import com.example.threadpanelfx.Model.IObservable;
import com.example.threadpanelfx.NetUtility.EventSender;
import com.example.threadpanelfx.NetUtility.MessengerPool;
import javafx.fxml.FXML;

public class GameServerController extends GameFrameController {

    public GameServerController()
    {
        var messageHandlerRunnable = new ServerMessageHandlerRunnable(MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncBroadcast));
        new Thread(messageHandlerRunnable).start();
    }

    @FXML
    public void initialize() {
        controller = new ServerController(GameModelPool.Instance().GetModel(GameModelPool.ModelType.local), circle1, circle2);
        CurrentControllerHolder.Set(controller);
        ((IObservable)m_model).AddObserver(EventSender.Instance());
    }
}
