module ro.mpp2026.festivalmuzicajavafx.persistence {
    requires ro.mpp2026.festivalmuzicajavafx.model;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires org.xerial.sqlitejdbc;

    exports ro.mpp2026.festivalmuzicajavafx.repository;
}