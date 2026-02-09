package org.example.template.controller;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.template.domain.Transaction;
import org.example.template.observer.Observer;
import org.example.template.observer.events.EntityChangeEvent;
import org.example.template.observer.events.EntityChangeEventType;
import org.example.template.service.CoinsService;
import org.example.template.service.TransactionsService;
import org.example.template.service.UsersService;

public class AdminController implements Observer {
    @FXML
    private TableView<Transaction> table;
    @FXML private javafx.scene.control.TableColumn<Transaction, String> nameCol;
    @FXML private javafx.scene.control.TableColumn<Transaction, Double> typeCol;
    @FXML private javafx.scene.control.TableColumn<Transaction, Double> priceCol;
    @FXML private javafx.scene.control.TableColumn<Transaction, Double> timestampCol;

    private final ObservableList<Transaction> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(cd ->
                new ReadOnlyStringWrapper
                        (UsersService.getInstance().findById(cd.getValue().getUserId()).getName()));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        timestampCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.setItems(data);

        reloadData();

        UsersService.getInstance().addObserver(this);
    }

    private void reloadData() {
        Platform.runLater(() -> {
            data.setAll(TransactionsService.getInstance().findAll());
        });
    }

    public void setData(Object data) {

    }

    @Override
    public void update(EntityChangeEvent event) {
        if(event.getType() != EntityChangeEventType.PLACEHOLDER_UPDATED)
            reloadData();
    }
}
