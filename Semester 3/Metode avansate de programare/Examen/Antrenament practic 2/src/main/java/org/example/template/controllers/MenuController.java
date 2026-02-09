package org.example.template.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.example.template.domain.Car;
import org.example.template.domain.User;
import org.example.template.domain.UserRole;
import org.example.template.observer.Observer;
import org.example.template.observer.events.EntityChangeEvent;
import org.example.template.observer.events.EntityChangeEventType;
import org.example.template.service.CarsService;

public class MenuController implements Observer {
    private User user;

    @FXML private TableView<Car> table;
    @FXML private TableColumn<Car, Long> idCol;
    @FXML private TableColumn<Car, String> nameCol;
    @FXML private TableColumn<Car, String> descriptionCol;
    @FXML private TableColumn<Car, Double> priceCol;
    @FXML private TableColumn<Car, String> statusCol;

    private final ObservableList<Car> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.setItems(data);
    }

    private void handleCarSelected(Car selectedCar) {
        TextField textField = new TextField();
        textField.setPromptText("Comentarii");

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label(selectedCar.toString()),
                textField
        );

        ButtonType accept = new ButtonType("Trimite", ButtonBar.ButtonData.OK_DONE);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalii masina");
        alert.setHeaderText("Detalii masina");

        alert.getDialogPane().setContent(vbox);
        alert.getButtonTypes().setAll(accept);

        alert.resultProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.getButtonData().equals(ButtonBar.ButtonData.OK_DONE)) {
                String userInput = textField.getText();
                CarsService.getInstance().sendToAdmins(selectedCar, userInput);
            }
        });

        alert.show();
    }


    public void setData(Object user) {
        this.user = (User) user;

        if(this.user.getRole() == UserRole.DEALER) {
            table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    handleCarSelected(newVal);
                }
            });
        }

        reloadData();
    }

    public void reloadData() {
        if(user.getRole() == UserRole.ADMIN) {
            this.data.setAll(CarsService.getInstance().getAllWaiting());
        }
        else {
            this.data.setAll(CarsService.getInstance().getAll());
        }
    }


    @Override
    public void update(EntityChangeEvent event) {
        if(event.getType() == EntityChangeEventType.PLACEHOLDER_UPDATED) {
            reloadData();

            if(user.getRole() == UserRole.ADMIN) {
                
            }
        }


    }
}
