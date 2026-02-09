import Controllers.DispatcherController;
import Controllers.DriverController;
import Domain.Driver;
import Repository.DriverRepo;
import Repository.OrderRepo;
import Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        String url = "jdbc:postgresql://localhost:5432/Curse_Taxi";

        Service service = new Service(new DriverRepo(url, "postgres", "V1ct0r12"),
                new OrderRepo(url, "postgres", "V1ct0r12"));

        try {
            FXMLLoader dispatcherLoader = new FXMLLoader(getClass().getResource("/dispatcher-view.fxml"));
            Stage dispatcherStage = new Stage();
            dispatcherStage.setScene(new javafx.scene.Scene(dispatcherLoader.load()));
            dispatcherStage.setTitle("Dispatcher");

            DispatcherController dispatcherController = dispatcherLoader.getController();
            dispatcherController.setService(service);

            dispatcherStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Driver driver : service.getAllDrivers()) {
            try {
                FXMLLoader driverLoader = new FXMLLoader(getClass().getResource("/driver-view.fxml"));
                Stage driverStage = new Stage();
                driverStage.setScene(new javafx.scene.Scene(driverLoader.load()));
                driverStage.setTitle("Driver " + driver.getName());

                DriverController driverController = driverLoader.getController();
                driverController.init(service, driver.getId());

                driverStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
