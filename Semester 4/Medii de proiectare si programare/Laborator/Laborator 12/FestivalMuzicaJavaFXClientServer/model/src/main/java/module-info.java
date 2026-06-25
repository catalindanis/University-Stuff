module ro.mpp2026.festivalmuzicajavafx.model {
    requires static lombok;
    requires jakarta.persistence;

    exports ro.mpp2026.festivalmuzicajavafx.domain;
    opens ro.mpp2026.festivalmuzicajavafx.domain to javafx.base;
}