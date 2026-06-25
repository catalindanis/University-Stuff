package ro.mpp2026.festivalmuzicajavafx;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.repository.*;
import ro.mpp2026.festivalmuzicajavafx.service.FestivalGrpcServiceImpl;
import ro.mpp2026.festivalmuzicajavafx.service.ServiceImpl;

import java.io.IOException;
import java.util.Properties;

public class ServerMain {
    private static final int DEFAULT_PORT = 50051;
    private static final Logger logger = LogManager.getLogger(ServerMain.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        Properties props = new Properties();

        try (var inStream = ServerMain.class.getResourceAsStream(
                "/ro/mpp2026/festivalmuzicajavafx/db.properties")) {
            if (inStream == null)
                throw new RuntimeException("Cannot find db.properties");
            props.load(inStream);
        }

        JdbcUtils jdbcUtils = new JdbcUtils(props);
        UsersDBRepository usersRepository = new UsersDBRepository(jdbcUtils);
        ShowsRepository showsRepository = new ShowsDBRepository(jdbcUtils);
        TicketsRepository ticketsRepository = new TicketsDBRepository(jdbcUtils);
        String encryptionAESKey = props.getProperty("jdbc.eckey");

        ServiceImpl serviceImpl = new ServiceImpl(
                usersRepository, showsRepository, ticketsRepository, encryptionAESKey
        );

        FestivalGrpcServiceImpl grpcService = new FestivalGrpcServiceImpl(serviceImpl);

        int serverPort = DEFAULT_PORT;
        try (var inStream = ServerMain.class.getResourceAsStream(
                "/ro/mpp2026/festivalmuzicajavafx/app.properties")) {
            if (inStream != null) {
                props.load(inStream);
                serverPort = Integer.parseInt(props.getProperty("server.port", String.valueOf(DEFAULT_PORT)));
            }
        } catch (NumberFormatException e) {
            logger.warn("Invalid server.port in app.properties, using default {}", DEFAULT_PORT);
        }

        Server server = ServerBuilder.forPort(serverPort)
                .addService(grpcService)
                .build()
                .start();

        logger.info("gRPC Server pornit pe portul {}", serverPort);
        System.out.println("gRPC Server pornit pe portul " + serverPort);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Oprire server gRPC...");
            server.shutdown();
        }));

        server.awaitTermination();
    }
}
