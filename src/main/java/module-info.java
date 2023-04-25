module com.example.threadpanelfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    exports com.example.threadpanelfx.Model;
    opens com.example.threadpanelfx.Model to javafx.fxml;
    exports com.example.threadpanelfx.Model.GameEvent;
    opens com.example.threadpanelfx.Model.GameEvent to javafx.fxml;
    exports com.example.threadpanelfx.View;
    opens com.example.threadpanelfx.View to javafx.fxml;
    exports com.example.threadpanelfx.Controller;
    opens com.example.threadpanelfx.Controller to javafx.fxml;
    exports com.example.threadpanelfx.Controller.Animation;
    opens com.example.threadpanelfx.Controller.Animation to javafx.fxml;
    exports com.example.threadpanelfx.Controller.MessageHandler;
    opens com.example.threadpanelfx.Controller.MessageHandler to javafx.fxml;
}