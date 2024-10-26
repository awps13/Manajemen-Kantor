module com.example.bookflight {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.desktop;


    opens com.example.bookflight to javafx.fxml;
    exports com.example.bookflight;
    exports com.example.bookflight.Controller;
    opens com.example.bookflight.Controller to javafx.fxml;
    exports com.example.bookflight.Model;
    opens com.example.bookflight.Model to javafx.fxml;
}