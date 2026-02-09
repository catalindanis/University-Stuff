module org.example.template {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires static lombok;


    opens org.example.template to javafx.fxml;
    exports org.example.template;
    exports org.example.template.observer.events;
    exports org.example.template.controllers;
    opens org.example.template.controllers to javafx.fxml;
    requires javafx.base;
    opens org.example.template.domain to javafx.base;
}