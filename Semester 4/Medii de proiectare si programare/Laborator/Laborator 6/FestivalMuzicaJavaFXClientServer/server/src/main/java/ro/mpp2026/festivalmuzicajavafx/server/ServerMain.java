package ro.mpp2026.festivalmuzicajavafx.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.repository.*;
import ro.mpp2026.festivalmuzicajavafx.service.ServiceImpl;
import ro.mpp2026.festivalmuzicajavafx.service.Service;
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
        UsersDBRepository usersRepository = new UsersDBRepository(jdbcUtils);
        ShowsRepository showsRepository = new ShowsDBRepository(jdbcUtils);
        TicketsRepository ticketsRepository = new TicketsDBRepository(jdbcUtils);
        String encryptionAESKey = props.getProperty("jdbc.eckey");

//        AuthServiceImpl authServiceImpl = new AuthServiceImpl(usersRepository, encryptionAESKey);
//        ShowsServiceImpl showsServiceImpl = new ShowsServiceImpl(authServiceImpl, showsRepository, ticketsRepository);
        Service serviceImpl = new ServiceImpl(usersRepository, showsRepository, ticketsRepository, encryptionAESKey);

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

        AbstractServer server = new MusicFestivalObjectConcurrentServer(serverPort, serviceImpl);
        try {
            server.start();
        } catch (ServerException e) {
            logger.error("Error starting the server" + e.getMessage());
        }
    }
}
