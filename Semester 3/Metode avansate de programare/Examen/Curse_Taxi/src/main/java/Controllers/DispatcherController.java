package Controllers;

import Service.Service;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;

public class DispatcherController {
    @FXML
    private TextField txtPickup;
    @FXML
    private TextField txtDest;
    @FXML
    private TextField txtClient;

    private Service service;

    public void setService(Service service){
        this.service=service;
    }

    @FXML
    public void onAdd() {
        String pickup=txtPickup.getText();
        String dest=txtDest.getText();
        String client=txtClient.getText();
        service.addOrder(pickup,dest,client);

        txtPickup.setText("");
        txtDest.setText("");
        txtClient.setText("");
    }
}
