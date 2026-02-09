package org.example.tema;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Duck;
import models.DuckType;
import models.Person;
import models.User;
import services.FriendshipsService;
import services.UsersService;
import utils.SessionData;

import java.util.Arrays;

public class PageController {

    private final BooleanProperty isPersonProperty = new SimpleBooleanProperty();
    private SessionData data;

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private Label numberOfFriendsLabel;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField occupationField;
    @FXML private TextField empathyLevelField;
    @FXML private DatePicker dateOfBirthField;

    @FXML private TextField speedField;
    @FXML private TextField resistanceField;
    @FXML private ComboBox<String> duckTypeComboBox;

    @FXML private VBox personFieldsVBox;
    @FXML private VBox duckFieldVBox;

    @FXML private Button saveDataButton;

    @FXML
    private void initialize() {
        personFieldsVBox.visibleProperty().bind(isPersonProperty);
        personFieldsVBox.managedProperty().bind(isPersonProperty);

        duckFieldVBox.visibleProperty().bind(isPersonProperty.not());
        duckFieldVBox.managedProperty().bind(isPersonProperty.not());

        empathyLevelField.setTextFormatter(new TextFormatter<>(change -> change.getText().matches("\\d*") ? change : null));
        speedField.setTextFormatter(new TextFormatter<>(change -> change.getText().matches("\\d*(\\.\\d*)?") ? change : null));
        resistanceField.setTextFormatter(new TextFormatter<>(change -> change.getText().matches("\\d*(\\.\\d*)?") ? change : null));

        saveDataButton.setOnAction(event -> saveUserData());
    }

    public void setData(SessionData data) {
        this.data = data;
        loadUserData();
    }

    private void loadUserData() {
        User user = UsersService.getInstance().getById(data.getUserId());
        initializeFields(user);
        isPersonProperty.set(user instanceof Person);
    }

    private void saveUserData() {
        if(isPersonProperty.get())
            savePersonData();
        else
            saveDuckData();
    }

    private void savePersonData() {
        Person person = (Person) UsersService.getInstance().getById(data.getUserId());

        person.setUsername(usernameField.getText());
        person.setEmail(emailField.getText());
        person.setFirstName(firstNameField.getText());
        person.setLastName(lastNameField.getText());
        person.setOccupation(occupationField.getText());
        person.setEmpathyLevel(Integer.parseInt(empathyLevelField.getText()));
        person.setDateOfBirth(dateOfBirthField.getValue());

        UsersService.getInstance().update(data.getUserId(), person);
        loadUserData();
    }

    private void saveDuckData() {
        Duck duck = (Duck) UsersService.getInstance().getById(data.getUserId());

        duck.setUsername(usernameField.getText());
        duck.setEmail(emailField.getText());
        duck.setSpeed(Double.parseDouble(speedField.getText()));
        duck.setResistance(Double.parseDouble(resistanceField.getText()));
        duck.setType(DuckType.fromString(duckTypeComboBox.getValue()));

        UsersService.getInstance().update(data.getUserId(), duck);
        loadUserData();
    }

    private void initializeFields(User user) {
        initializeGenericFields(user);
        if(user instanceof Person)
            initializeFieldsForPerson(user);
        else if(user instanceof Duck)
            initializeFieldsForDuck(user);
    }

    private void initializeGenericFields(User user) {
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        numberOfFriendsLabel.setText("Number of friends: " + FriendshipsService.getInstance().getNoFriendsForUser(user.getId()));
    }

    private void initializeFieldsForPerson(User user) {
        Person person = (Person) user;
        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        occupationField.setText(person.getOccupation());
        empathyLevelField.setText(String.valueOf(person.getEmpathyLevel()));
        dateOfBirthField.setValue(person.getDateOfBirth());
    }

    private void initializeFieldsForDuck(User user) {
        Duck duck = (Duck) user;

        speedField.setText(String.valueOf(duck.getSpeed()));
        resistanceField.setText(String.valueOf(duck.getResistance()));
        duckTypeComboBox.setValue(duck.getType().toString());

        duckTypeComboBox.getItems().addAll(Arrays.stream(DuckType.values()).map(Enum::toString).toList());
    }
}
