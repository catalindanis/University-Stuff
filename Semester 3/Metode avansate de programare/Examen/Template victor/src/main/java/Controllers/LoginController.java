package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import Service.Service;
import java.io.IOException;

public class LoginController {
    private Service service;

    @FXML private TextField txtUser;
    @FXML private PasswordField txtPass;

    public void setService(Service s) {
        this.service = s;
    }

    @FXML
    public void onLogin() {
        String username = txtUser.getText();
        String password = txtPass.getText();

        // LOGICA DE LOGIN: O poți adapta (căutare în DB prin service)
        // Exemplu simplu pentru test:
        if (!username.isEmpty()) {
            openMainView(username);
            // Închidem fereastra de login (opțional)
            ((Stage) txtUser.getScene().getWindow()).close();
        } else {
            new Alert(Alert.AlertType.ERROR, "Username invalid!").show();
        }
    }

    private void openMainView(String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/main-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Aplicație Practică - Logat ca: " + username);

            MainController mainCtrl = loader.getController();
            mainCtrl.setService(service);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Nu s-a putut deschide fereastra principală!").show();
        }
    }
}