package org.example.template.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.template.domain.Coin;
import org.example.template.domain.User;
import org.example.template.observer.Observer;
import org.example.template.observer.events.EntityChangeEvent;
import org.example.template.observer.events.EntityChangeEventType;
import org.example.template.service.CoinsService;
import org.example.template.service.UsersService;

public class UserController implements Observer {

    @FXML private TableView<Coin> table;
    @FXML private javafx.scene.control.TableColumn<Coin, String> nameCol;
    @FXML private javafx.scene.control.TableColumn<Coin, Double> priceCol;

    @FXML private Label budgetLabel;

    @FXML private Button buyButton, sellButton;

    private User user;

    private final ObservableList<Coin> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        table.setItems(data);

        buyButton.setOnAction((event) -> {
            if(table.getSelectionModel().getSelectedItem() != null) {
                UsersService.getInstance().placeBuyOrder(user.getId(), table.getSelectionModel().getSelectedItem().getId());
            }
        });

        sellButton.setOnAction((event) -> {
            if(table.getSelectionModel().getSelectedItem() != null) {
                UsersService.getInstance().placeSellOrder(user.getId(), table.getSelectionModel().getSelectedItem().getId());
            }
        });

        reloadData();

        UsersService.getInstance().addObserver(this);
        CoinsService.getInstance().addObserver(this);
    }

    private void reloadData() {
        Platform.runLater(() -> {
            int oldSelectedItem = -1;

            if(table.getSelectionModel().getSelectedItem() != null) {
                oldSelectedItem = table.getSelectionModel().getSelectedIndex();
            }
            data.setAll(CoinsService.getInstance().findAll());

            if(oldSelectedItem != -1)
                table.getSelectionModel().select(oldSelectedItem);

            if(user != null)
                updateUserBalance();
        });
    }

    public void setData(Object data) {
        user = (User) data;

        updateUserBalance();
    }

    public void updateUserBalance() {
        budgetLabel.setText("Balance = " + UsersService.getInstance().getBudgetForUser(user.getId()));
    }

    @Override
    public void update(EntityChangeEvent event) {
        if(event.getType() == EntityChangeEventType.PLACEHOLDER_UPDATED || ((Integer) event.getData()) == user.getId())
            reloadData();
    }
}
