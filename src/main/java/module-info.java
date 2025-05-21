module com.example.var17 {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;


    opens com.example.var17 to javafx.fxml;
    exports com.example.var17;
}