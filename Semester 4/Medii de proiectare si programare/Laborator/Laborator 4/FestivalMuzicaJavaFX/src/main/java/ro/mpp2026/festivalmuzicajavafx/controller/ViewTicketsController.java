package ro.mpp2026.festivalmuzicajavafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.mpp2026.festivalmuzicajavafx.domain.Ticket;
import ro.mpp2026.festivalmuzicajavafx.service.ShowsService;
import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

public class ViewTicketsController implements Observer {

    @FXML
    private TableView<Ticket> ticketsTable;
    @FXML
    private TableColumn<Ticket, String> clientNameColumn;
    @FXML
    private TableColumn<Ticket, String> showNameColumn;
    @FXML
    private TableColumn<Ticket, String> numberOfSeatsColumn;
    @FXML
    private TextField numberOfSeatsField;
    @FXML
    private Button updateTicketButton;
    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {
        ShowsService.getInstance().subscribe(this);

        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        numberOfSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("noSeats"));
        showNameColumn.setCellValueFactory(cellData -> {
            Ticket ticket = cellData.getValue();
            String showName = ticket.getShow().getArtistName() + " - " +
                    ticket.getShow().getDate() + " - " +
                    ticket.getShow().getLocation();
            return new javafx.beans.property.SimpleStringProperty(showName);
        });

        updateTicketButton.setOnAction(this::updateTicketClick);

        loadData();
    }

    private void updateTicketClick(ActionEvent actionEvent) {
        try {
            validateUpdateInputs();

            Ticket ticket = ticketsTable.getSelectionModel().getSelectedItem();
            Integer numberOfSeats = Integer.parseInt(numberOfSeatsField.getText());

            ShowsService.getInstance().updateTicket(ticket.getId(), ticket.getClientName(), ticket.getShow().getId(), numberOfSeats);
        } catch (Exception exception) {
            messageLabel.setText(exception.getMessage());
        }
    }

    private void validateUpdateInputs() {
        StringBuilder errorMessage = new StringBuilder();

        if(ticketsTable.getSelectionModel().getSelectedItem() == null)
            errorMessage.append("You must select a ticket first\n");

        try {
            Integer tempValue = Integer.parseInt(numberOfSeatsField.getText());
        } catch (NumberFormatException exception) {
            errorMessage.append("Please enter a valid integer\n");
        }

        if(!errorMessage.isEmpty())
            throw new RuntimeException(errorMessage.toString());
    }

    private void loadData() {
        ObservableList<Ticket> tickets = FXCollections.observableArrayList(ShowsService.getInstance().findAllTickets());
        ticketsTable.setItems(tickets);
    }

    @Override
    public void update() {
        loadData();
    }
}
