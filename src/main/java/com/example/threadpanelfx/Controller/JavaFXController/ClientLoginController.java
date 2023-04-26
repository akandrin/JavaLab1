package com.example.threadpanelfx.Controller.JavaFXController;

import com.example.threadpanelfx.Controller.ClientController;
import com.example.threadpanelfx.Controller.IController;
import com.example.threadpanelfx.Controller.MessageHandler.ClientMessageHandlerRunnable;
import com.example.threadpanelfx.Controller.MessageHandler.MessageHandlerRunnable;
import com.example.threadpanelfx.Model.GameEvent.GameEvent;
import com.example.threadpanelfx.Model.GameEvent.NewPlayerAdded;
import com.example.threadpanelfx.Model.IObserver;
import com.example.threadpanelfx.Model.PlayerSettings;
import com.example.threadpanelfx.NetUtility.MessengerPool;
import com.example.threadpanelfx.View.ClientFrame;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientLoginController implements IObserver {
    @FXML
    protected Button m_setNameButton, m_readyForGameButton;

    @FXML
    protected TextField m_nameTextField;

    @FXML
    protected AnchorPane m_pane;

    protected IController controller;

    private final ClientMessageHandlerRunnable m_messageHandlerRunnable;

    public ClientLoginController()
    {
        controller = new ClientController();
        m_messageHandlerRunnable = new ClientMessageHandlerRunnable(MessengerPool.Instance().GetMessenger(MessengerPool.MessengerType.asyncSingle));
        m_messageHandlerRunnable.AddObserver(this);
        new Thread(m_messageHandlerRunnable).start();
    }

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
        controller.OnNewPlayerAdded(playerName);
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

    private void HandleEvent(NewPlayerAdded newPlayerAdded)
    {
        String playerName = newPlayerAdded.GetPlayerName();
        boolean isMyName = playerName.equals(PlayerSettings.GetPlayerName());

        if (isMyName)
        {
            if (newPlayerAdded.GetStatus())
            {
                // удалось добавить
                m_messageHandlerRunnable.Stop();
                switchSceneToGame();

                //m_readyForGameButton.setDisable(false);

            }
            else
            {
                PlayerSettings.SetPlayerName(null);
                // Не удалось добавить
                m_setNameButton.setDisable(false);

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setContentText("Игрок с таким именем уже существует");
                alert.showAndWait();
            }
        }
    }

    @Override
    public void Update(Object event) {
        var gameEvent = (GameEvent)event;
        if (gameEvent.GetType() == GameEvent.Type.newPlayerAdded)
        {
            HandleEvent((NewPlayerAdded) gameEvent);
        }
    }

    @Override
    public boolean CanImmediatelyUpdate() {
        return false;
    }
}
