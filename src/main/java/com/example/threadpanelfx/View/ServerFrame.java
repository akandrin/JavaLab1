package com.example.threadpanelfx.View;

import com.example.threadpanelfx.Controller.GameClientController;
import com.example.threadpanelfx.Controller.GameServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerFrame extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerFrame.class.getResource("Game.fxml"));
        fxmlLoader.setController(new GameServerController());
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Game");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
