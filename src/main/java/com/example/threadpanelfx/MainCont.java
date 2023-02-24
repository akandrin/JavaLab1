package com.example.threadpanelfx;

import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

public class MainCont {
    @FXML
    Circle c1;

    @FXML
    protected void runCircle(){
        new Thread(
            ()->
            {
                while (true)
                {
                    double x = c1.getLayoutX();
                    x += 5;
                    c1.setLayoutX(x);

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }

                }
            }
        ).start();

    }
}
