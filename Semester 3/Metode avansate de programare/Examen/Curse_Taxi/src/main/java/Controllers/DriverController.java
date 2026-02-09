package Controllers;

import Domain.Order;
import Observer.Observer;
import Service.Service;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class DriverController implements Observer{
    @FXML
    private TableView<Order> tableOrders;
    @FXML
    private TableColumn<Order,String> colPickup, colDest;
    @FXML
    private Label lblNotification;
    @FXML
    private Button btnAccept;
    private Service service;
    private int driverId;

    public void init(Service s, int id) {
        this.service = s;
        this.driverId = id;
        service.addObserver(this);
        colPickup.setCellValueFactory(new PropertyValueFactory<>("pickupAddress"));
        colDest.setCellValueFactory(new PropertyValueFactory<>("destinationAddress"));
        update();
    }

    @Override
    public void update() {
        javafx.application.Platform.runLater(() -> {
            tableOrders.setItems(FXCollections.observableArrayList(service.getActiveOrders(driverId)));
            Order n = service.getNotificationForDriver(driverId);
            if(n != null){
                lblNotification.setText("New order: " + n.getPickupAddress() + " -> " + n.getDestinationAddress());
                btnAccept.setVisible(true);
            } else {
                lblNotification.setText("");
                btnAccept.setVisible(false);
            }
        });
    }


    @FXML void onAccept() {
        Order n = service.getNotificationForDriver(driverId);
        if(n != null) {
            service.acceptOrder(driverId, n.getId());
            lblNotification.setText("Order accepted");
            btnAccept.setVisible(false);
        }
    }

    @FXML void onFinish() {
        Order selected = tableOrders.getSelectionModel().getSelectedItem();
        if(selected != null) {
            service.finishOrder(selected.getId(), driverId);
        }
    }


}
