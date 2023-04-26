package com.example.threadpanelfx.Controller.JavaFXController;

import com.example.threadpanelfx.Controller.ClientController;
import com.example.threadpanelfx.Controller.IController;
import com.example.threadpanelfx.Controller.MessageHandler.ClientMessageHandlerRunnable;
import com.example.threadpanelfx.Controller.MessageHandler.MessageHandlerRunnable;
import com.example.threadpanelfx.Model.GameEvent.GameEvent;
import com.example.threadpanelfx.Model.GameEvent.NewPlayerAdded;
import com.example.threadpanelfx.Model.IObserver;
import com.example.threadpanelfx.Model.PlayerSettings;
import com.example.threadpanelfx.NetUtility.IMessenger;
import com.example.threadpanelfx.NetUtility.Message;
import com.example.threadpanelfx.NetUtility.MessengerPool;
import com.example.threadpanelfx.NetUtility.Request.CheckNameRequest;
import com.example.threadpanelfx.NetUtility.Request.CheckNameResponse;
import com.example.threadpanelfx.NetUtility.Request.Response;
import com.example.threadpanelfx.View.ClientFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientLoginController {
    @FXML
    protected Button m_setNameButton, m_readyForGameButton;

    @FXML
    protected TextField m_nameTextField;

    @FXML
    protected AnchorPane m_pane;

    @FXML
    public void OnSetName()
    {
        String playerName = m_nameTextField.getText();
        if (playerName == null || playerName.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Message Here...");
            alert.setHeaderText("Look, an Information Dialog");
            alert.setContentText("I have a great message for you!");
            alert.showAndWait();
            return;
        }

        m_setNameButton.setDisable(true);
        PlayerSettings.SetPlayerName(playerName);
        IMessenger messenger = MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncSingle);
        messenger.SendMessage(new CheckNameRequest(playerName).CreateRequestMessage(null, "server"));

        AtomicBoolean responseStatus = new AtomicBoolean(false);

        var messageHandlerRunnable = new ClientMessageHandlerRunnable(MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncSingle))
        {
            @Override
            protected void HandleResponse(Message responseMessage)
            {
                Response response = (Response)responseMessage.data;
                if (response.GetType() == Response.ResponseType.checkName) {
                    var checkNameResponse = (CheckNameResponse) response;

                    responseStatus.set(checkNameResponse.GetStatus());
                }
                else
                {
                    super.HandleResponse(responseMessage);
                }
                Stop();
            }
        };
        CompletableFuture.runAsync(messageHandlerRunnable).thenRun(() -> {
            if (responseStatus.get()) {
                // удалось добавить
                Platform.runLater(this::switchSceneToGame);
                // m_readyForGameButton.setDisable(false);
            } else {
                // Не удалось добавить
                PlayerSettings.SetPlayerName(null);
                Platform.runLater(() -> {
                    m_setNameButton.setDisable(false);

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setContentText("Игрок с таким именем уже существует");
                    alert.showAndWait();
                });
            }
        });
    }

    @FXML
    public void OnReadyForGame()
    {
    }

    private void switchSceneToGame()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientFrame.class.getResource("Game.fxml"));
        fxmlLoader.setController(new GameClientController());

        Stage stage = (Stage)m_pane.getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Game");
        stage.setScene(scene);
        stage.show();
    }
}
