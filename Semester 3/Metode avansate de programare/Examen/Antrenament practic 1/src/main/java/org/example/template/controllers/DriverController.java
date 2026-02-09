package org.example.template.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.example.template.domain.Driver;
import org.example.template.domain.Order;
import org.example.template.observer.Observer;
import org.example.template.observer.events.EntityChangeEvent;
import org.example.template.observer.events.EntityChangeEventType;
import org.example.template.service.OrdersService;

import java.util.Map;

public class DriverController implements Observer {
    @FXML
    private Text driverName;
    @FXML
    private ListView<Order> inProgressOrdersListView;
    private final ObservableList<Order> inProgressOrders = FXCollections.observableArrayList();

    @FXML
    ComboBox<String> comboBox;

    @FXML
    ToggleGroup sizeGroup;
    @FXML
    RadioButton rb1;
    @FXML
    RadioButton rb2;
    @FXML
    RadioButton rb3;

    @FXML
    CheckBox checkBox;

    private Driver driver;

    public void setData(Driver driver) {
        this.driver = driver;
        this.driverName.setText(this.driver.getName());
        reloadData();
    }

    public void reloadData() {
        OrdersService.getInstance().findAllInProgressForDriver(driver.getId()).thenAccept(
                inProgressOrders::setAll
        );

        inProgressOrdersListView.setCellFactory(lv -> new ListCell<>() {
            private final Button button = new Button("Finish");
            private final Label label = new Label();
            private final HBox content = new HBox(10, label, button);

            {
                button.setOnAction(e -> {
                    Order order = getItem();
                    if (order != null) {
                        OrdersService.getInstance().finishOrder(order.getId());
                    }
                });
            }

            @Override
            protected void updateItem(Order order, boolean empty) {
                Platform.runLater(() -> {
                    super.updateItem(order, empty);

                    if (empty || order == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        label.setText(order.getClientName() + " " + order.getPickupAdress() + " -> " + order.getDestinationAdress());
                        setGraphic(content);
                    }
                });
            }
        });
    }

    @FXML
    public void initialize() {
        inProgressOrdersListView.setItems(inProgressOrders);
        OrdersService.getInstance().addObserver(this);

        comboBox.getItems().addAll("Red", "Green", "Blue");
        comboBox.setValue("Red"); // default selection

        comboBox.setOnAction(e -> {
            System.out.println("Selected color: " + comboBox.getValue());
        });

        sizeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                System.out.println("Nothing selected");
                return;
            }
            RadioButton selected = (RadioButton) newToggle;
            System.out.println("Selected size changed to: " + selected.getText());
        });

        rb1.selectedProperty().addListener((obs, was, isNow) -> {
            if (isNow) System.out.println("Small selected");
        });

        rb1.setToggleGroup(sizeGroup);
        rb2.setToggleGroup(sizeGroup);
        rb3.setToggleGroup(sizeGroup);

        rb1.setSelected(true); // default
        if (rb1.isSelected()) {
            System.out.println("Small selected");
        }

        RadioButton selected =
                (RadioButton) sizeGroup.getSelectedToggle();

        System.out.println(selected.getText());

        checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            System.out.println("Checkbox is now: " + isSelected);
        });

        if (checkBox.isSelected()) {
            System.out.println("Enabled");
        }
    }

    @Override
    public void update(EntityChangeEvent event) {
        if(event.getType() == EntityChangeEventType.UPDATED) {
            if(event.getData().equals(driver.getId()))
                reloadData();
        }

        if(event.getType() == EntityChangeEventType.ADDED) {
            Map<String, Object> data = (Map) event.getData();
            if(data.get("driverId").equals(driver.getId())) {
                Platform.runLater(() -> {
                    ButtonType accept = new ButtonType("Acceptă", ButtonBar.ButtonData.OK_DONE);
//                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Notificare driver " + data.get("driverId"));
                    alert.setHeaderText("Cursa noua!");
                    String text = data.get("order").toString();
                    alert.setContentText(text);
                    alert.resultProperty().addListener((observable, oldValue, newValue) -> {
                        if(newValue.getButtonData().equals(ButtonBar.ButtonData.OK_DONE))
                            OrdersService.getInstance().acceptOrder(((Order) data.get("order")).getId(), Integer.valueOf(data.get("driverId").toString()));
                    });
                    alert.getButtonTypes().setAll(accept);
                    alert.show();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            Platform.runLater(alert::close);
                        }
                    }).start();
                });
            }
        }
    }
}
