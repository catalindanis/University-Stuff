package org.example.tema;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ListDucks extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ListDucks.class.getResource("list-ducks.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 360);
        stage.setTitle("ListDucks");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}