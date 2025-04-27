module com.example.listview {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.listview to javafx.fxml;
    exports com.example.listview;
}
