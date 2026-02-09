package org.example.template.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.template.domain.User;
import org.example.template.domain.UserRole;
import org.example.template.exceptions.RepositoryException;
import org.example.template.service.UsersService;

import java.io.IOException;

public class LoginController {
    @FXML
    Button loginButton;
    @FXML
    TextField email;
    @FXML TextField password;
    @FXML
    Text loginResponseText;

    @FXML
    public void initialize() {
        loginButton.setOnAction(event -> {
                    if (email.getText().isBlank() || password.getText().isBlank()) {
                        loginResponseText.setText("Both fields need to be filled");
                        return;
                    }

                    User user;
                    try {
                        user = UsersService.getInstance().login(email.getText(), password.getText());
                    } catch (RepositoryException exception) {
                        loginResponseText.setText("Account not found");
                        return;
                    }

                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/template/menu.fxml"));
                        Parent root = loader.load();

                        MenuController controller = loader.getController();
                        controller.setData(user);

                        Stage stage = new Stage();
                        stage.setTitle("Menu for user #" + user.getId());
                        if (user.getRole() == UserRole.ADMIN)
                            stage.setScene(new Scene(root, 450, 450));
                        else
                            stage.setScene(new Scene(root, 450, 450));
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
