module org.example.template {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires static lombok;


    opens org.example.template to javafx.fxml;
    exports org.example.template;
    exports org.example.template.observer.events;
    exports org.example.template.controller;
    opens org.example.template.controller to javafx.fxml;
    opens org.example.template.domain to javafx.base;
}