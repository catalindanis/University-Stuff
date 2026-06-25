package ro.mpp2026.festivalmuzicajavafx;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.repository.*;
import ro.mpp2026.festivalmuzicajavafx.server.AbstractServer;
import ro.mpp2026.festivalmuzicajavafx.server.MusicFestivalObjectConcurrentServer;
import ro.mpp2026.festivalmuzicajavafx.service.AuthServiceImpl;
import ro.mpp2026.festivalmuzicajavafx.service.ShowsServiceImpl;
import ro.mpp2026.festivalmuzicajavafx.repository.JdbcUtils;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Properties;

public class ServerMain {
    private static int defaultPort = 55555;
    private static Logger logger = LogManager.getLogger(ServerMain.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            var inStream = ServerMain.class.getResourceAsStream("/ro/mpp2026/festivalmuzicajavafx/db.properties");
            if (inStream == null) {
                throw new RuntimeException("Cannot find db.properties in classpath at /ro/mpp2026/festivalmuzicajavafx/db.properties");
            }
            props.load(inStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading db.properties", e);
        }

        JdbcUtils jdbcUtils = new JdbcUtils(props);
//        UsersRepository usersRepository = new UsersDBRepository(jdbcUtils);
        UsersRepository usersRepository = new UsersJpaDBRepository();
//        ShowsRepository showsRepository = new ShowsDBRepository(jdbcUtils);
        ShowsRepository showsRepository = new ShowsJpaDBRepository();
//        TicketsRepository ticketsRepository = new TicketsDBRepository(jdbcUtils);
        TicketsRepository ticketsRepository = new TicketsJpaDBRepository();
        String encryptionAESKey = props.getProperty("jdbc.eckey");

        AuthServiceImpl authServiceImpl = new AuthServiceImpl(usersRepository, encryptionAESKey);
        ShowsServiceImpl showsServiceImpl = new ShowsServiceImpl(authServiceImpl, showsRepository, ticketsRepository);

        try {
            var inStream = ServerMain.class.getResourceAsStream("/ro/mpp2026/festivalmuzicajavafx/app.properties");
            if (inStream == null) {
                throw new RuntimeException("Cannot find db.properties in classpath at /ro/mpp2026/festivalmuzicajavafx/app.properties");
            }
            props.load(inStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading app.properties", e);
        }

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(props.getProperty("server.port"));
        } catch(NumberFormatException numberFormatException) {
            logger.error("Wrong  Port Number" + numberFormatException.getMessage());
            logger.debug("Using default port " + defaultPort);
        }

        AbstractServer server = new MusicFestivalObjectConcurrentServer(serverPort, authServiceImpl, showsServiceImpl);
        try {
            logger.debug("Server started on port {}", serverPort);
            server.start();
        } catch (ServerException e) {
            logger.error("Error starting the server" + e.getMessage());
        }
    }
}
