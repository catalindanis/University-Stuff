package crud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("app.fxml"));
        stage.setScene(new Scene(root, 500, 400));
        stage.setTitle("JavaFX CRUD Example");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}