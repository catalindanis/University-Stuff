package org.example.template.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.template.service.OrdersService;

public class ManagerController {
    @FXML
    private TextField pickupAdressTextField, destinationAddressTextField, clientNameTextField;

    @FXML
    private Button addButton;

    @FXML
    public void initialize() {
        addButton.setOnAction(e -> {
           if(pickupAdressTextField.getText().isBlank() ||
                destinationAddressTextField.getText().isBlank() ||
                   clientNameTextField.getText().isBlank())
               return;

            OrdersService.getInstance().add(pickupAdressTextField.getText(), destinationAddressTextField.getText(), clientNameTextField.getText());
        });
    }
}
