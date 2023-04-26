package com.example.threadpanelfx.Controller.JavaFXController;

import com.example.threadpanelfx.Controller.ClientController;
import com.example.threadpanelfx.Controller.CurrentControllerHolder;
import com.example.threadpanelfx.Controller.MessageHandler.ClientMessageHandlerRunnable;
import com.example.threadpanelfx.Model.GameEvent.ArrowChanged;
import com.example.threadpanelfx.Model.GameEvent.GameEvent;
import com.example.threadpanelfx.Model.PlayerSettings;
import com.example.threadpanelfx.NetUtility.MessengerPool;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GameClientController extends GameFrameController {
    private Button m_startButton;
    private Button m_stopButton;
    private Button m_shotButton;

    public GameClientController()
    {
        var messageHandlerRunnable = new ClientMessageHandlerRunnable(MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncSingle));
        messageHandlerRunnable.AddObserver(this);
        new Thread(messageHandlerRunnable).start();
    }

    static private Button createButton(String text, boolean isDisabled, EventHandler<ActionEvent> eventHandler)
    {
        Button button = new Button();
        button.setText(text);
        button.setMnemonicParsing(false);
        button.setOnAction(eventHandler);
        button.setDisable(isDisabled);
        button.setPrefWidth(125);
        return button;
    }

    @FXML
    public void initialize() {
        controller = new ClientController();
        CurrentControllerHolder.Set(controller);

        m_startButton = createButton("Начать игру", false, e -> OnStartGame());
        m_stopButton = createButton("Остановить игру", true, e -> OnStopGame());
        m_shotButton = createButton("Выстрел", true, e -> OnShot());
        m_buttons.getChildren().addAll(m_startButton, m_stopButton, m_shotButton);
    }

    private boolean m_shotButtonIsDisabledPrevState;

    @Override
    protected void ActionBeforeNewPlayerAdded()
    {
        m_shotButtonIsDisabledPrevState = m_shotButton.isDisabled();
        m_shotButton.setDisable(true);
    }

    @Override
    protected void ActionAfterNewPlayerAdded()
    {
        m_shotButton.setDisable(m_shotButtonIsDisabledPrevState);
    }

    private void HandleEvent(ArrowChanged arrowChanged)
    {
        String playerName = arrowChanged.GetPlayerName();
        boolean isArrowVisible = arrowChanged.IsArrowVisible();
        if (playerName.equals(PlayerSettings.GetPlayerName())) {
            // если изменилась стрела у текущего игрока
            m_shotButton.setDisable(isArrowVisible);
        }
    }

    @Override
    public void Update(Object event) {
        super.Update(event);
        var gameEvent = (GameEvent)event;
        if (gameEvent.GetType() == GameEvent.Type.arrowChanged) {
            HandleEvent((ArrowChanged) gameEvent);
        }
    }

    public void OnStartGame()
    {
        controller.OnNewPlayerAdded(PlayerSettings.GetPlayerName());
        controller.OnStartGame();
        m_startButton.setDisable(true);
        m_stopButton.setDisable(false);
        m_shotButton.setDisable(false);
    }

    public void OnStopGame()
    {
        controller.OnStopGame();
        m_startButton.setDisable(false);
        m_stopButton.setDisable(true);
        m_shotButton.setDisable(true);
    }

    public void OnShot()
    {
        m_shotButton.setDisable(true);
        controller.OnShot(PlayerSettings.GetPlayerName());
    }
}
