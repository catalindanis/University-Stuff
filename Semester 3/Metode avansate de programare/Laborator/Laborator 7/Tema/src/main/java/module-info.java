module org.example.tema {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;
    requires java.desktop;


    opens org.example.tema to javafx.fxml;
    opens models to javafx.base;
    exports org.example.tema;
    opens dto to javafx.base;
}