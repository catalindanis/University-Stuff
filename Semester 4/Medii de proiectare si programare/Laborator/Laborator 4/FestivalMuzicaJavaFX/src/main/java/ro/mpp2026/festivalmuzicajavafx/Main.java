package ro.mpp2026.festivalmuzicajavafx;

import javafx.application.Application;
import javafx.stage.Stage;
import ro.mpp2026.festivalmuzicajavafx.utils.Navigator;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Navigator.setMainStage(stage);
        Navigator.navigateTo("auth.fxml", "Authenticate");
    }
}
