package ro.mpp2026.client;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.festivalmuzicajavafx.service.AuthServiceProxy;
import ro.mpp2026.festivalmuzicajavafx.service.ServiceProxy;
import ro.mpp2026.festivalmuzicajavafx.service.ShowsServiceProxy;
import ro.mpp2026.client.utils.Navigator;

import java.io.IOException;
import java.util.Properties;

public class ClientMain extends Application {
    private static final int defaultPort = 55555;
    private static final String defaultHost = "localhost";
    private static final Logger logger = LogManager.getLogger(ClientMain.class);

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Properties props = new Properties();

        try {
            var inStream = ClientMain.class.getResourceAsStream("/ro/mpp2026/client/app.properties");
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
            logger.error("Wrong Port Number {}", numberFormatException.getMessage());
            logger.debug("Using default port " + defaultPort);
        }
        String serverHost = props.getProperty("server.host");

        logger.debug("Server port {}", serverPort);
        logger.debug("Server host {}", serverHost);
        Navigator.setService(new ServiceProxy(serverPort, serverHost));

        Navigator.setMainStage(stage);
        Navigator.navigateTo("auth.fxml", "Authenticate");
    }
}
