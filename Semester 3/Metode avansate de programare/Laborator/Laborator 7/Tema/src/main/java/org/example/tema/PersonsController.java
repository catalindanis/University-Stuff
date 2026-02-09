package org.example.tema;

import dto.PersonDTO;
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
import models.Person;
import models.User;
import services.UsersService;
import utils.paging.Page;
import utils.paging.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public class PersonsController {

    @FXML private TableView<User> table;

    @FXML private TableColumn<Duck, Long> idCol;
    @FXML private TableColumn<Duck, String> usernameCol;
    @FXML private TableColumn<Duck, String> emailCol;
    @FXML private TableColumn<Duck, String> passwordCol;
    @FXML private TableColumn<Duck, DuckType> firstNameCol;
    @FXML private TableColumn<Duck, Double> lastNameCol;
    @FXML private TableColumn<Duck, Double> dateOfBirthCol;
    @FXML private TableColumn<Duck, Double> occupationCol;
    @FXML private TableColumn<Duck, Long> empathyLevelCol;

    @FXML private TextFlow textFlow;

    @FXML private TextField email;
    @FXML private TextField password;
    @FXML private TextField username;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private TextField occupation;
    @FXML private TextField empathyLevel;
    @FXML private DatePicker dateOfBirth;

    private final UsersFilterDTO filter = new UsersFilterDTO();

    private final ObservableList<User> data = FXCollections.observableArrayList();
    private int pageNumber = 0;

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dateOfBirthCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        occupationCol.setCellValueFactory(new PropertyValueFactory<>("occupation"));
        empathyLevelCol.setCellValueFactory(new PropertyValueFactory<>("empathyLevel"));

        filter.setUserType(Optional.of(Person.class));
        dateOfBirth.setValue(LocalDate.now());

        empathyLevel.setTextFormatter(new TextFormatter<>(change -> {
            return change.getText().matches("\\d*") ? change : null;
        }));

        table.setItems(data);
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
    public void onAddPerson() {
        try {
            UsersService.getInstance().add(new PersonDTO(
                    username.getText(),
                    email.getText(),
                    password.getText(),
                    firstName.getText(),
                    lastName.getText(),
                    dateOfBirth.getValue(),
                    occupation.getText(),
                    Integer.parseInt(empathyLevel.getText())
            ));

            username.setText(null);
            email.setText(null);
            password.setText(null);
            firstName.setText(null);
            lastName.setText(null);
            occupation.setText(null);
            empathyLevel.setText(null);
            dateOfBirth.setValue(LocalDate.now());
            reloadData();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    public void onDeletePerson() {
        if(table.getSelectionModel().getSelectedItem() == null)
            return;

        UsersService.getInstance().removeById(table.getSelectionModel().getSelectedItem().getId());
        reloadData();
    }
}