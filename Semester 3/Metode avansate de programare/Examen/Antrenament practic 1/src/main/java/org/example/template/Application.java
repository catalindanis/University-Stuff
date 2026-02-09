package org.example.template;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.template.controllers.DriverController;
import org.example.template.service.DriversService;
import org.example.template.service.OrdersService;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage primaryStage) {
        DriversService.getInstance().findAllAsync().thenAccept(drivers -> Platform.runLater(() -> { drivers.forEach(driver -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("driver.fxml"));
                    Parent root = loader.load();

                    DriverController controller = loader.getController();
                    controller.setData(driver);

                    Stage stage = new Stage();
                    stage.setTitle("Driver " + driver.getId());
                    stage.setScene(new Scene(root, 650, 450));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }));

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("manager.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Manager");
            stage.setScene(new Scene(root, 650, 450));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        System.out.println(OrdersService.getInstance().getLatestOrderDate(1));
//        System.out.println(OrdersService.getInstance().getLatestOrderDate(2));

//        System.out.println(args);

        launch();
    }
}