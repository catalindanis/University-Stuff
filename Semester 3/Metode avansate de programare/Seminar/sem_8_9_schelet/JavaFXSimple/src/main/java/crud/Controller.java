package crud;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {

    @FXML private TableView<Person> table;
    @FXML private TableColumn<Person, String> nameCol;
    @FXML private TableColumn<Person, String> emailCol;

    @FXML private TextField nameField;
    @FXML private TextField emailField;

    private ObservableList<Person> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name1"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        table.setItems(data);

        data.add(new Person("Ana", "ana@example.com"));
        data.add(new Person("Ion", "ion@example.com"));
    }

    @FXML
    public void onAdd() {
        data.add(new Person(nameField.getText(), emailField.getText()));
        nameField.clear();
        emailField.clear();
    }

    @FXML
    public void onEdit() {
        Person p = table.getSelectionModel().getSelectedItem();
        if (p != null) {
            p.setName(nameField.getText());
            p.setEmail(emailField.getText());
            table.refresh();
        }
    }

    @FXML
    public void onDelete() {
        Person p = table.getSelectionModel().getSelectedItem();
        if (p != null) {
            data.remove(p);
        }
    }
}