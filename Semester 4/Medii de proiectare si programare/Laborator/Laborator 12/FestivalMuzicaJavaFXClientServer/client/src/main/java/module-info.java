module ro.mpp2026.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires services;
    requires networking;
    requires ro.mpp2026.festivalmuzicajavafx.model;
    requires static lombok;

    opens ro.mpp2026.client to javafx.fxml;
    opens ro.mpp2026.client.controller to javafx.fxml;
    exports ro.mpp2026.client;
}