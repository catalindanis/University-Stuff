package org.example.template;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.template.domain.Car;
import org.example.template.domain.CarStatus;
import org.example.template.domain.User;
import org.example.template.domain.UserRole;
import org.example.template.repository.CarsRepository;
import org.example.template.repository.UsersRepository;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root, 650, 450));
            stage.show();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}