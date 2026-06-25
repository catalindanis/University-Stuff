package ro.mpp2026.festivalmuzicajavafx.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Setter;
import ro.mpp2026.festivalmuzicajavafx.controller.PropsReceiver;

import java.io.IOException;
import java.util.Map;

public class Navigator {
    @Setter
    private static Stage mainStage;

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

    public static Stage navigateTo(String fxmlFileName, String title, Stage stage, Map<String, Object> props) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Navigator.class.getResource("/ro/mpp2026/festivalmuzicajavafx/" + fxmlFileName));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(title);
            stage.setScene(scene);
            stage.show();
            if(props != null) {
                PropsReceiver controller = fxmlLoader.getController();
                controller.setProps(props);
            }

            return stage;
        } catch (IOException exception) {}
        return null;
    }
}
