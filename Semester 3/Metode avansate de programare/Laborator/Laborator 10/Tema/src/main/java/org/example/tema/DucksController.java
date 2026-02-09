package org.example.tema;

import dto.DuckDTO;
import dto.UsersFilterDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import models.Duck;
import models.DuckType;
import models.User;
import services.UsersService;
import utils.Encryption;
import utils.paging.Page;
import utils.paging.Pageable;

import java.util.Arrays;
import java.util.Optional;

public class DucksController {

    @FXML private TableView<User> table;

    @FXML private TableColumn<Duck, Long> idCol;
    @FXML private TableColumn<Duck, String> usernameCol;
    @FXML private TableColumn<Duck, String> emailCol;
    @FXML private TableColumn<Duck, String> passwordCol;
    @FXML private TableColumn<Duck, DuckType> typeCol;
    @FXML private TableColumn<Duck, Double> speedCol;
    @FXML private TableColumn<Duck, Double> resistanceCol;
//    @FXML private TableColumn<Duck, Long> groupCol;

    @FXML private ComboBox<String> filterDuckTypeComboBox;

    @FXML private TextFlow textFlow;

    @FXML private TextField email;
    @FXML private TextField password;
    @FXML private TextField username;
    @FXML private TextField speed;
    @FXML private TextField resistance;
    @FXML private ComboBox<String> duckTypeComboBox;

    private final UsersFilterDTO filter = new UsersFilterDTO();

    private final ObservableList<User> data = FXCollections.observableArrayList();
    private int pageNumber = 0;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        speedCol.setCellValueFactory(new PropertyValueFactory<>("speed"));
        resistanceCol.setCellValueFactory(new PropertyValueFactory<>("resistance"));
//        groupCol.setCellValueFactory(new PropertyValueFactory<>("group"));

        filterDuckTypeComboBox.getItems().add("All");
        filterDuckTypeComboBox.getSelectionModel().selectFirst();
        duckTypeComboBox.getItems().addAll(Arrays.stream(DuckType.values()).map(Enum::toString).toList());
        filterDuckTypeComboBox.getItems().addAll(Arrays.stream(DuckType.values()).map(Enum::toString).toList());
        duckTypeComboBox.getSelectionModel().selectFirst();
        filter.setUserType(Optional.of(Duck.class));

        speed.setTextFormatter(new TextFormatter<>(change -> {
            return change.getText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));

        resistance.setTextFormatter(new TextFormatter<>(change -> {
            return change.getText().matches("\\d*(\\.\\d*)?") ? change : null;
        }));

        table.setItems(data);

        reloadData();
    }

    @FXML
    public void onFilterDuckTypeChanged() {
        pageNumber = 0;
        filter.setType(Optional.ofNullable(DuckType.fromString(filterDuckTypeComboBox.getSelectionModel().getSelectedItem())));
        reloadData();
    }

    public void reloadData() {
        int pageSize = 5;
        Page<User> page = UsersService.getInstance().getUsers(new Pageable(pageNumber, pageSize), filter);
        int maxPage = Math.max((int) Math.ceil(1.0 * page.getTotalNumberOfElements() / pageSize) - 1, 0);

        if(pageNumber > maxPage) {
            pageNumber = maxPage;
            reloadData();
            return;
        }

        if(pageNumber < 0) {
            pageNumber = 0;
            reloadData();
            return;
        }

        data.setAll(page.getElements());
//        int totalElements = page.getTotalNumberOfElements();

        String pageText = (pageNumber + 1) +
                " / " +
                (maxPage + 1);
        Text t = new Text(pageText);
        textFlow.getChildren().setAll(t);
    }

    @FXML
    public void onNextPage() {
        pageNumber++;
        reloadData();
    }

    @FXML
    public void onPreviousPage() {
        if(pageNumber == 0)
            return;

        pageNumber--;
        reloadData();
    }

    @FXML
    public void onAddDuck() {
        try {
            UsersService.getInstance().add(new DuckDTO(
                    username.getText(),
                    email.getText(),
                    Encryption.encrypt(password.getText()),
                    DuckType.fromString(duckTypeComboBox.getSelectionModel().getSelectedItem()),
                    Double.parseDouble(speed.getText()),
                    Double.parseDouble(resistance.getText()),
                    0
            ));

            username.setText(null);
            email.setText(null);
            password.setText(null);
            speed.setText(null);
            resistance.setText(null);
            reloadData();
        } catch (Exception exception) {
        }
    }

    @FXML
    public void onDeleteDuck() {
        if(table.getSelectionModel().getSelectedItem() == null)
            return;

        UsersService.getInstance().removeById(table.getSelectionModel().getSelectedItem().getId());
        reloadData();
    }
}