module ro.mpp2026.festivalmuzicajavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires org.apache.logging.log4j;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;


    opens ro.mpp2026.festivalmuzicajavafx to javafx.fxml;
    opens ro.mpp2026.festivalmuzicajavafx.controller to javafx.fxml;
    opens ro.mpp2026.festivalmuzicajavafx.domain to javafx.base;
    exports ro.mpp2026.festivalmuzicajavafx;
}