package ro.mpp2026.client.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;
import ro.mpp2026.client.utils.Navigator;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;
import ro.mpp2026.festivalmuzicajavafx.domain.User;
import ro.mpp2026.festivalmuzicajavafx.service.AuthService;
import ro.mpp2026.festivalmuzicajavafx.service.ShowsService;
import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

import java.time.LocalDate;
import java.util.*;

public class HomeController implements PropsReceiver, Observer {

    @FXML
    private TableView<Show> showsTable;
    @FXML
    private TableColumn<Show, String> artistColumn;
    @FXML
    private TableColumn<Show, String> dateColumn;
    @FXML
    private TableColumn<Show, String> locationColumn;
    @FXML
    private TableColumn<Show, Integer> remainingSeatsColumn;
    @FXML
    private TableColumn<Show, Integer> soldSeatsColumn;
    @FXML
    private TextField artistSearchField;
    @FXML
    private DatePicker datePickerField;
    @FXML
    private Button searchButton;
    @FXML
    private TextField clientsNameField;
    @FXML
    private TextField numberOfSeatsField;
    @FXML
    private Button buyButton;
    @FXML
    private Button viewAllTicketsButton;
    @FXML
    private Label messageLabel;
    @FXML
    private Button logoutButton;

    private Optional<Map<String, Object>> props;
    private long userId;

    private final AuthService authService;
    private final ShowsService showsService;

    public HomeController(AuthService authService, ShowsService showsService) {
        this.authService = authService;
        this.showsService = showsService;
    }

    private void initialize() {
        showsService.subscribe(this);

        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        remainingSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("remainingSeats"));
        soldSeatsColumn.setCellValueFactory(cellData -> {
            Show show = cellData.getValue();
            int soldSeats = showsService.getNumberOfSoldSeatsForShow(show.getId());
            return new javafx.beans.property.SimpleIntegerProperty(soldSeats).asObject();
        });

        showsTable.setRowFactory(tableView -> new TableRow<>() {
            @Override
            protected void updateItem(Show show, boolean empty) {
                super.updateItem(show, empty);
                if (show == null || empty) {
                    setStyle("");
                } else if (show.getRemainingSeats() == 0) {
                    setStyle("-fx-background-color: #ffcccc;");
                } else {
                    setStyle("");
                }
            }
        });

        searchButton.setOnAction(this::searchButtonClick);
        buyButton.setOnAction(this::buyButtonClick);
        viewAllTicketsButton.setOnAction(this::viewAllTicketsButtonClick);
        logoutButton.setOnAction(this::logoutClick);

        loadData();
    }

    private void buyButtonClick(ActionEvent event) {
        try {
            validateBuyInputs();

            Show show = showsTable.getSelectionModel().getSelectedItem();
            String client = clientsNameField.getText();
            Integer numberOfSeats = Integer.parseInt(numberOfSeatsField.getText());

            showsService.bookTicketForShow(show.getId(), client, numberOfSeats);

            resetFields();
            messageLabel.setText("Ticket bought successfully");
        } catch(Exception exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    private void viewAllTicketsButtonClick(ActionEvent event) {
        Navigator.navigateTo("view-tickets.fxml", "Tickets", true, props.get());
    }

    private void searchButtonClick(ActionEvent event) {
        try {
            validateSearchInputs();

            Map<String, Object> props = new HashMap<>();
            props.put("user", this.props.get().get("user"));
            props.put("showsFilter", ShowFilter.builder()
                    .artistName(artistSearchField.getText())
                    .date(datePickerField.getValue())
                    .build());

            Navigator.navigateTo("filtered-shows.fxml", "Shows (filtered)", true, props);
        } catch (Exception exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    private void validateBuyInputs() {
        String client = clientsNameField.getText();
        String numberOfSeats = numberOfSeatsField.getText();
        StringBuilder errorMessage = new StringBuilder();

        if(showsTable.getSelectionModel().getSelectedItem() == null)
            errorMessage.append("You must select a show first\n");

        if(client.isBlank())
            errorMessage.append("Client name cannot be empty\n");

        try {
            Integer tempValue = Integer.parseInt(numberOfSeats);
        } catch (NumberFormatException exception) {
            errorMessage.append("Please enter a valid integer\n");
        }

        if(!errorMessage.isEmpty())
            throw new RuntimeException(errorMessage.toString());
    }

    private void validateSearchInputs() {
        String artist = artistSearchField.getText();
        LocalDate date = datePickerField.getValue();
        StringBuilder errorMessage = new StringBuilder();

        if(!errorMessage.isEmpty())
            throw new RuntimeException(errorMessage.toString());
    }

    private void logoutClick(ActionEvent actionEvent) {
        this.authService.logout(((User) props.get().get("user")).getId(), this);

        List<Window> windows = new ArrayList<>(Window.getWindows());

        for (Window window : windows) {
            if (window.isShowing()) {
                window.hide();
            }
        }

        Navigator.navigateTo("auth.fxml", "Authenticate", true, true);
    }

    private void loadData() {
        ObservableList<Show> shows = FXCollections.observableArrayList(showsService.findAll());
        showsTable.setItems(shows);

        if(props.isPresent()) {
            this.userId = ((User) props.get().get("user")).getId();
        }
    }

    @Override
    public void setProps(Map<String, Object> props) {
        this.props = Optional.ofNullable(props);

        initialize();
    }

    private void resetFields() {
        messageLabel.setText("");
    }

    @Override
    public void update() {
        Platform.runLater(this::loadData);
    }
}
