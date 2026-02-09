package org.example.tema;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.AuthenticationService;
import utils.SessionData;

import java.io.IOException;

public class LoginController {
    @FXML Button loginButton;
    @FXML TextField email;
    @FXML TextField password;
    @FXML Text loginResponseText;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> {
            if(email.getText().isBlank() || password.getText().isBlank()) {
                loginResponseText.setText("Both fields need to be filled");
                return;
            }

            SessionData data = AuthenticationService.getInstance().login(email.getText(), password.getText());

            if(data == null) {
                loginResponseText.setText("Invalid email or password");
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("menu.fxml"));
                Parent root = loader.load();

                MenuController controller = loader.getController();
                controller.setData(data);

                Stage stage = new Stage();
                stage.setTitle("Menu for user #" + data.getUserId());
                if(data.isAdmin())
                    stage.setScene(new Scene(root, 250, 650));
                else
                    stage.setScene(new Scene(root, 250, 450));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
