package org.example.template;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.template.domain.Placeholder;
import org.example.template.observer.Observer;
import org.example.template.observer.events.EntityChangeEvent;
import org.example.template.service.Service;

public class Controller implements Observer {
    @FXML private TableView<Placeholder> tableView;

    @FXML private TableColumn<Placeholder, Integer> tableColumn1;
    @FXML private TableColumn<Placeholder, String> tableColumn2;

    private Service srv;

    private final ObservableList<Placeholder> users = FXCollections.observableArrayList();

    public void setService(Service srv) {
        this.srv = srv;
        this.srv.addObserver(this);
    }

    @FXML private void initialize() {
        tableColumn1.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tableColumn2.setCellValueFactory(new PropertyValueFactory<>("placeholder"));

        tableView.setItems(users);
    }

    @Override
    public void update(EntityChangeEvent event) {

    }
}