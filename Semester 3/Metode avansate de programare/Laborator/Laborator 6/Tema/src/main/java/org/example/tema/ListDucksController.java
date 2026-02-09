package org.example.tema;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Duck;
import models.DuckType;
import services.UsersService;

import java.util.Arrays;

public class ListDucksController {

    @FXML private TableView<Duck> table;

    @FXML private TableColumn<Duck, Long> idCol;
    @FXML private TableColumn<Duck, String> usernameCol;
    @FXML private TableColumn<Duck, String> emailCol;
    @FXML private TableColumn<Duck, String> passwordCol;
    @FXML private TableColumn<Duck, DuckType> typeCol;
    @FXML private TableColumn<Duck, Double> speedCol;
    @FXML private TableColumn<Duck, Double> resistanceCol;
    @FXML private TableColumn<Duck, Long> groupCol;

    @FXML private ComboBox<String> duckTypeComboBox;

    private ObservableList<Duck> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        speedCol.setCellValueFactory(new PropertyValueFactory<>("speed"));
        resistanceCol.setCellValueFactory(new PropertyValueFactory<>("resistance"));
        groupCol.setCellValueFactory(new PropertyValueFactory<>("group"));

        duckTypeComboBox.getItems().add("Toate");
        duckTypeComboBox.getSelectionModel().selectFirst();
        duckTypeComboBox.getItems().addAll(Arrays.stream(DuckType.values()).map(Enum::toString).toList());

        table.setItems(data);

        data.setAll(UsersService.getInstance().getDucks());
    }

    @FXML
    public void onDuckTypeChanged() {
        data.setAll(UsersService.getInstance().getDucks(DuckType.fromString(duckTypeComboBox.getValue())));
    }
}