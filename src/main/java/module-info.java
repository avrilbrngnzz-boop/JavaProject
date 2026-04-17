module com.example.javaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.example.javaproject to javafx.fxml, com.google.gson;
    exports com.example.javaproject;
}
