package Controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Observer.Observer;
import Service.Service;
import java.time.LocalDate;

public class MainController implements Observer {
    private Service service;
    private int currentPage = 0;
    private final int pageSize = 5;

    // Tabel și Coloane
    @FXML private TableView<Object> tableView; // Înlocuiește Object cu clasa ta
    @FXML private TableColumn<Object, String> col1;
    @FXML private TableColumn<Object, String> col2;
    @FXML private TableColumn<Object, LocalDate> col3;
    @FXML private TableColumn<Object, Boolean> col4;

    // Controale Filtrare/Input
    @FXML private ComboBox<String> comboBox;
    @FXML private DatePicker datePicker;
    @FXML private CheckBox checkBox;
    @FXML private RadioButton radio1, radio2;

    // Controale Paginare
    @FXML private Label lblPage;
    @FXML private Button btnPrev, btnNext;

    public void setService(Service s) {
        this.service = s;
        service.addObserver(this); // Înregistrare Observer
        initUI();
    }

    private void initUI() {
        // 1. Mapare coloane (Property name trebuie să fie numele atributului din clasa Domain)
        col1.setCellValueFactory(new PropertyValueFactory<>("col1"));
        col2.setCellValueFactory(new PropertyValueFactory<>("col2"));
        col3.setCellValueFactory(new PropertyValueFactory<>("col3"));
        col4.setCellValueFactory(new PropertyValueFactory<>("col4"));

        // 2. Populare ComboBox
        comboBox.setItems(FXCollections.observableArrayList("Opțiune 1", "Opțiune 2", "Opțiune 3"));

        // 3. Încărcare date inițiale
        update();
    }

    @FXML
    public void onFilter() {
        // Exemplu de citire valori pentru filtrare compusă
        String selectedCombo = comboBox.getValue();
        LocalDate selectedDate = datePicker.getValue();
        boolean isChecked = checkBox.isSelected();
        String selectedRadio = radio1.isSelected() ? "R1" : "R2";

        System.out.println("Filtrare după: " + selectedCombo + ", " + selectedDate + ", " + isChecked);

        // Aici ai apela: service.filter(selectedCombo, selectedDate, ...)
        update();
    }

    @FXML
    public void onAddAction() {
        // Exemplu de acțiune (Salvare/Adăugare)
        service.addData(); // Aceasta va apela notifyObservers() în Service
    }

    // --- LOGICA DE PAGINARE ---
    @FXML
    public void onNext() {
        currentPage++;
        update();
    }

    @FXML
    public void onPrevious() {
        if (currentPage > 0) {
            currentPage--;
            update();
        }
    }

    @Override
    public void update() {
        // Aici aduci datele (normal, paginat sau asincron)
        // Exemplu paginat:
        // var data = service.getPagedData(pageSize, currentPage * pageSize);
        // tableView.setItems(FXCollections.observableArrayList(data));

        // Actualizare etichetă pagină
        lblPage.setText("Pagina: " + (currentPage + 1));

        // Control butoane (Prev se dezactivează la pagina 0)
        btnPrev.setDisable(currentPage == 0);
    }
}