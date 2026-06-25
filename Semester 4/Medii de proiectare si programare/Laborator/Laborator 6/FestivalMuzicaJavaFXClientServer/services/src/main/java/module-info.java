module services {
    requires org.apache.logging.log4j;
    requires ro.mpp2026.festivalmuzicajavafx.model;
    requires ro.mpp2026.festivalmuzicajavafx.persistence;
    requires networking;

    // gRPC
    requires io.grpc;
    requires io.grpc.stub;
    requires io.grpc.protobuf;

    // Jakarta annotation (înlocuiește javax.annotation)
    requires jakarta.annotation;

    exports ro.mpp2026.festivalmuzicajavafx.service;
    exports ro.mpp2026.festivalmuzicajavafx.grpc;
}
