package com.example.threadpanelfx.Controller.JavaFXController;

import com.example.threadpanelfx.Controller.ClientController;
import com.example.threadpanelfx.Controller.CurrentControllerHolder;
import com.example.threadpanelfx.Controller.MessageHandler.ClientMessageHandlerRunnable;
import com.example.threadpanelfx.Model.GameEvent.*;
import com.example.threadpanelfx.Model.PlayerSettings;
import com.example.threadpanelfx.NetUtility.IMessenger;
import com.example.threadpanelfx.NetUtility.Message;
import com.example.threadpanelfx.NetUtility.MessengerPool;
import com.example.threadpanelfx.NetUtility.Request.CheckNameRequest;
import com.example.threadpanelfx.NetUtility.Request.GetHighScoresRequest;
import com.example.threadpanelfx.NetUtility.Request.GetHighScoresResponse;
import com.example.threadpanelfx.NetUtility.Request.Response;
import com.example.threadpanelfx.View.HighScoresTableView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameClientController extends GameFrameController {
    // private Button m_highScoresButton;
    private Button m_readyButton;
    private Button m_stopButton;
    private Button m_shotButton;

    private final Alert m_gamePausedAlert;

    public GameClientController()
    {
        m_gamePausedAlert = new Alert(Alert.AlertType.NONE);
        m_gamePausedAlert.setTitle("Игра приостановлена...");

        var messageHandlerRunnable = new ClientMessageHandlerRunnable(MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncSingle))
        {
            @Override
            protected void HandleResponse(Message responseMessage)
            {
                super.HandleResponse(responseMessage);
                Response response = (Response) responseMessage.data;
                if (response.GetType() == Response.ResponseType.getHighScores)
                    Platform.runLater(() -> {
                        onHighScoresResponse((GetHighScoresResponse) response);
                    });
            }
        };
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

        Button highScoresButton = createButton("Таблица рекордов", false, e -> OnHighScores()); // состояние этой кнопки не меняется в дальнейшем, так что не храним её как поле класса
        m_readyButton = createButton("Готов", false, e -> OnReadyForGame());
        m_stopButton = createButton("Приостановить игру", true, e -> OnPauseGame());
        m_shotButton = createButton("Выстрел", true, e -> OnShot());
        m_buttons.getChildren().addAll(highScoresButton, m_readyButton, m_stopButton, m_shotButton);
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

    private void HandleEvent(GameStarted gameStarted)
    {
        m_readyButton.setDisable(true);
        m_stopButton.setDisable(false);
        m_shotButton.setDisable(false);
    }

    private void HandleEvent(GamePaused gamePaused)
    {
        boolean gamePausedByMe = PlayerSettings.GetPlayerName().equals(gamePaused.GetPlayerName());
        // если игру остановил текущий игрок, то для него кнопку включаем
        m_readyButton.setDisable(!gamePausedByMe);

        m_stopButton.setDisable(true);
        m_shotButton.setDisable(true);

        if (!gamePausedByMe)
        {
            m_gamePausedAlert.setContentText("Игрок " + gamePaused.GetPlayerName() + " приостановил игру");
            m_gamePausedAlert.show();
        }
    }

    private void HandleEvent(GameContinued gamePaused)
    {
        if (m_gamePausedAlert != null && m_gamePausedAlert.isShowing())
        {
            m_gamePausedAlert.setResult(ButtonType.OK);
            m_gamePausedAlert.hide();
        }
        
        m_readyButton.setDisable(true);
        m_stopButton.setDisable(false);
        m_shotButton.setDisable(false);
    }

    private void HandleEvent(GameStopped gameStopped)
    {
        m_readyButton.setDisable(false);
        m_stopButton.setDisable(true);
        m_shotButton.setDisable(true);

        Alert infoAboutWinner = new Alert(Alert.AlertType.INFORMATION);
        infoAboutWinner.setTitle("Игра окончена");
        infoAboutWinner.setHeaderText("Игра окончена в связи с победой одного из игроков");
        infoAboutWinner.setContentText("Игрок " + gameStopped.GetWinnerName() + " выиграл");
        infoAboutWinner.showAndWait();
    }

    @Override
    public void Update(Object event) {
        super.Update(event);
        var gameEvent = (GameEvent)event;
        switch(gameEvent.GetType())
        {
            case arrowChanged:
                HandleEvent((ArrowChanged) gameEvent);
                break;
            case gameStarted:
                HandleEvent((GameStarted) gameEvent);
                break;
            case gamePaused:
                HandleEvent((GamePaused) gameEvent);
                break;
            case gameContinued:
                HandleEvent((GameContinued) gameEvent);
                break;
            case gameStopped:
                HandleEvent((GameStopped) gameEvent);
                break;
        }
    }

    private void onHighScoresResponse(GetHighScoresResponse response)
    {
        TableView view = new HighScoresTableView(response.GetHighScoresList());
        Pane pane = new Pane();
        pane.getChildren().add(view);

        Stage stage = new Stage();
        stage.setScene(new Scene(pane));
        stage.show();
    }

    public void OnHighScores()
    {
        String playerName = PlayerSettings.GetPlayerName();
        IMessenger messenger = MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncSingle);
        messenger.SendMessage(new GetHighScoresRequest().CreateRequestMessage(playerName, "server"));
    }

    public void OnReadyForGame()
    {
        // todo : убрать дизейбл кнопок?
        m_readyButton.setDisable(true);
        m_stopButton.setDisable(true);
        m_shotButton.setDisable(true);
        controller.OnReadyForGame(PlayerSettings.GetPlayerName());
    }

    public void OnPauseGame()
    {
        controller.OnPauseGame(PlayerSettings.GetPlayerName());
    }

    public void OnShot()
    {
        m_shotButton.setDisable(true);
        controller.OnShot(PlayerSettings.GetPlayerName());
    }
}
