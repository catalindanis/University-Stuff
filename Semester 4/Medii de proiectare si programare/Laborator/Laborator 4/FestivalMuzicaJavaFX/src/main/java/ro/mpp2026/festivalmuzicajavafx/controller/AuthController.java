package ro.mpp2026.festivalmuzicajavafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.service.AuthService;
import ro.mpp2026.festivalmuzicajavafx.utils.Navigator;

import java.util.HashMap;
import java.util.Map;

public class AuthController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        loginButton.setOnAction(this::handleLoginClick);
        registerButton.setOnAction(this::handleRegisterClick);
        messageLabel.setText("");
    }

    private void handleLoginClick(ActionEvent event) {
        try {
            validateInputs();
            User user = AuthService.getInstance().login(emailField.getText(), passwordField.getText());
            resetFields();

            Map<String, Object> props = new HashMap<>();
            props.put("user", user);
            Navigator.navigateTo("home.fxml", "Dashboard", true, props);
        } catch (RuntimeException exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    private void handleRegisterClick(ActionEvent event) {
        try {
            validateInputs();
            AuthService.getInstance().register(emailField.getText(), passwordField.getText());
            resetFields();
            messageLabel.setText("Account created! Please log in");
        } catch (RuntimeException exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    private void resetFields() {
        emailField.setText("");
        passwordField.setText("");
        messageLabel.setText("");
    }

    private void validateInputs() {
        String email = emailField.getText();
        String password = passwordField.getText();
        StringBuilder errorMessage = new StringBuilder();

        if(email.isBlank())
            errorMessage.append("Email field cannot be empty!\n");
        else if(!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            errorMessage.append("Email format is invalid!\n");

        if(password.isBlank())
            errorMessage.append("Password field cannot be empty\n");

        if(!errorMessage.isEmpty())
            throw new RuntimeException(errorMessage.toString());
    }
}
