import Controllers.MainController;
import Repository.ItemRepo;
import Repository.ItemRepoAsync;
import Repository.ItemRepoPaged;
import Service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

public class Main extends Application {
    public void start(Stage primaryStage) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/mappractic";
        Service service = new Service(new ItemRepo(url, "postgres", "V1ct0r12"),
                new ItemRepoPaged(url, "postgres", "V1ct0r12"),
                new ItemRepoAsync(url, "postgres", "V1ct0r12"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main-view.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        MainController ctrl = loader.getController();
        ctrl.setService(service);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
