module com.example.turbodash {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.example.turbodash to javafx.fxml;
    exports com.example.turbodash;
}
