module com.example.habitstask {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.java;
    requires jbcrypt;

    opens com.example.habitstask to javafx.fxml;
    exports com.example.habitstask;
}