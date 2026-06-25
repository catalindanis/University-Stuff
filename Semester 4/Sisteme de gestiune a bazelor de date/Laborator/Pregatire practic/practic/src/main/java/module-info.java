module org.example.practic {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;


    opens org.example.practic to javafx.fxml;
    exports org.example.practic;
}