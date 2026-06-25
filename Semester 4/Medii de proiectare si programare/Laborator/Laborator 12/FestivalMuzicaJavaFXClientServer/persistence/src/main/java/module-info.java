module ro.mpp2026.festivalmuzicajavafx.persistence {
    requires ro.mpp2026.festivalmuzicajavafx.model;
    requires org.apache.logging.log4j;
    requires org.xerial.sqlitejdbc;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires spring.context;

    exports ro.mpp2026.festivalmuzicajavafx.repository;
}