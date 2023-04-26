package com.example.threadpanelfx.View;

import com.example.threadpanelfx.Controller.JavaFXController.GameClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientFrame extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(ClientFrame.class.getResource("Game.fxml"));
        //fxmlLoader.setController(new GameClientController());
        FXMLLoader fxmlLoader = new FXMLLoader(ClientFrame.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
