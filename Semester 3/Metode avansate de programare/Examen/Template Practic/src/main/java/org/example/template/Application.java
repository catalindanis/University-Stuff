package org.example.template;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.template.controller.UserController;
import org.example.template.domain.User;
import org.example.template.service.UsersService;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage currentStage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Market Admin");
            stage.setScene(new Scene(root, 650, 450));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(User user : UsersService.getInstance().findAll()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/trader.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();

                UserController controller = loader.getController();
                controller.setData(user);

                stage.setTitle("Trader: " + user.getName());
                stage.setScene(new Scene(root, 650, 450));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}