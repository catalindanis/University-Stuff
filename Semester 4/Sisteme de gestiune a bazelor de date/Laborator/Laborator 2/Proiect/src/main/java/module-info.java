module sgbd.proiect {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.sql;


    opens sgbd.proiect to javafx.fxml;
    opens sgbd.proiect.controller to javafx.fxml;
    exports sgbd.proiect;
}