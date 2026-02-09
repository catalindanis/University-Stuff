module crud {
    requires javafx.controls;
    requires javafx.fxml;

    opens crud to javafx.fxml, javafx.base;
    exports crud;
}