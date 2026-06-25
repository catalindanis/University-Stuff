package ro.mpp2026.client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2026.client.controller.*;
import ro.mpp2026.festivalmuzicajavafx.service.Service;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class Navigator {
    @Setter
    private static Stage mainStage;
    @Setter
    private static Service service;

    private static final Logger logger = LogManager.getLogger(Navigator.class);
    private static final String FXML_BASE_PATH = "/ro/mpp2026/client/";

    public static Stage navigateTo(String fxmlFileName, String title) {
        return navigateTo(fxmlFileName, title, mainStage, null);
    }

    public static Stage navigateTo(String fxmlFileName, String title, Map<String, Object> props) {
        return navigateTo(fxmlFileName, title, mainStage, props);
    }

    public static Stage navigateTo(String fxmlFileName, String title, boolean newStage) {
        if(!newStage)
            navigateTo(fxmlFileName, title);

        Stage stage = new Stage();
        return navigateTo(fxmlFileName, title, stage, null);
    }

    public static Stage navigateTo(String fxmlFileName, String title, boolean newStage, Map<String, Object> props) {
        if(!newStage)
            return navigateTo(fxmlFileName, title, props);

        Stage stage = new Stage();
        return navigateTo(fxmlFileName, title, stage, props);
    }

    public static Stage navigateTo(String fxmlFileName, String title, boolean newStage, boolean overrideMainStage) {
        return navigateTo(fxmlFileName, title, newStage, overrideMainStage, null);
    }

    public static Stage navigateTo(String fxmlFileName, String title, boolean newStage, boolean overrideMainStage, Map<String, Object> props) {
        if(!newStage)
            return navigateTo(fxmlFileName, title, props);

        mainStage = new Stage();
        return navigateTo(fxmlFileName, title, props);
    }

    public static Stage navigateTo(String fxmlFileName, String title, Stage stage, Map<String, Object> props) {
        URL fxmlUrl = Navigator.class.getResource(FXML_BASE_PATH + fxmlFileName);
        if (fxmlUrl == null) {
            throw new IllegalStateException("FXML not found: " + FXML_BASE_PATH + fxmlFileName);
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
            fxmlLoader.setControllerFactory(clazz -> {
                if (clazz == AuthController.class) {
                    return new AuthController(service);
                }
                if(clazz == HomeController.class) {
                    logger.info("Initializing Home controller");
                    return new HomeController(service, service);
                }
                if(clazz == FilteredShowsController.class) {
                    return new FilteredShowsController(service);
                }
                if(clazz == ViewTicketsController.class) {
                    return new ViewTicketsController(service);
                }
                try {
                    return clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
            if(props != null) {
                PropsReceiver controller = fxmlLoader.getController();
                controller.setProps(props);
            }

            return stage;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML file: " + fxmlFileName, e);
        }
    }
}
