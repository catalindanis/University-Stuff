package org.example.tema;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Duck;
import models.Event;
import models.Lane;
import models.RaceEvent;
import services.EventsService;
import services.UsersService;
import utils.DuckTaskResult;
import utils.Observer;
import utils.SessionData;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class EventsController implements Observer<DuckTaskResult> {
    private SessionData data;

    @FXML ListView<Event> eventsListView;
    @FXML ObservableList<Event> eventsList = FXCollections.observableArrayList();

    @FXML VBox adminControls;
    @FXML VBox personControls;
    @FXML VBox duckControls;

    @FXML VBox lanesBox;
    @FXML TextField eventName;
    @FXML Button addLaneButton;
    @FXML Button removeLaneButton;
    @FXML Button addEventButton;
    @FXML Button removeEventButton;
    @FXML Button startEventButton;
    @FXML Button subscribeEventButton;
    @FXML Button joinEventButton;
    @FXML ComboBox<String> eventTypeComboBox;

    private ExecutorService executors;

    @FXML
    private void initialize() {
        executors = Executors.newCachedThreadPool();

        hide(adminControls);
        hide(personControls);
        hide(duckControls);

        hide(subscribeEventButton);
        hide(joinEventButton);

        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Race");
        eventTypeComboBox.setItems(list);
        eventTypeComboBox.getSelectionModel().selectFirst();

        EventsService.getInstance().subscribe(this);

        addLaneButton.setOnAction(event -> {
            executors.submit(() -> {
                int currentLaneIndex = lanesBox.getChildren().size() / 2 + 1;

                Text label = new Text();
                label.setText("Lane #" + currentLaneIndex);

                TextField textField = new TextField();
                textField.setPromptText("Buoy distance");

                Platform.runLater(() -> {
                    lanesBox.getChildren().addAll(label, textField);
                });
            });
        });

        removeLaneButton.setOnAction(event -> {
            if(lanesBox.getChildren().size() == 2)
                return;

            Platform.runLater(() -> {
                lanesBox.getChildren().remove(lanesBox.getChildren().getLast());
                lanesBox.getChildren().remove(lanesBox.getChildren().getLast());
            });
        });

        addEventButton.setOnAction(event -> {
            if(eventName.getText().isBlank())
                return;

            executors.submit(() -> {
                boolean invalidData = lanesBox.getChildren().parallelStream()
                        .filter(node -> node instanceof TextField)
                        .map(node -> (TextField) node)
                        .anyMatch(tf -> tf.getText().isBlank());

                if(invalidData)
                    return;

                List<Lane> lanes = lanesBox.getChildren().parallelStream()
                        .filter(node -> node instanceof TextField)
                        .map(node -> (TextField) node)
                        .map(textField -> Integer.parseInt(textField.getText()))
                        .map(Lane::new)
                        .collect(Collectors.toList());

                EventsService.getInstance().add(
                        eventName.getText(),
                        lanes
                );

                reloadData();
            });
        });

        removeEventButton.setOnAction(event -> {
            if(eventsListView.getSelectionModel().getSelectedItem() == null)
                return;

            executors.submit(() -> {
                EventsService.getInstance().remove(
                        eventsListView.getSelectionModel().getSelectedItem().getId()
                );

                reloadData();
            });
        });

        startEventButton.setOnAction(event -> {
            if(eventsListView.getSelectionModel().getSelectedItem() == null)
                return;

            EventsService.getInstance().startAsync(eventsListView.getSelectionModel().getSelectedItem().getId());
        });

        subscribeEventButton.setOnAction(event -> {
            if(eventsListView.getSelectionModel().getSelectedItem() == null)
                return;

            executors.submit(() -> {
                boolean alreadySubscribed = eventsListView.getSelectionModel().getSelectedItem().getSubscribers().parallelStream().anyMatch(s -> Objects.equals(s.getId(), data.getUserId()));

                if(alreadySubscribed)
                    EventsService.getInstance().unsubscribe(data.getUserId(), eventsListView.getSelectionModel().getSelectedItem().getId());
                else
                    EventsService.getInstance().subscribe(data.getUserId(), eventsListView.getSelectionModel().getSelectedItem().getId());

                int selectedIndex = eventsListView.getSelectionModel().getSelectedIndex();

                reloadData(selectedIndex);
            });
        });

        joinEventButton.setOnAction(event -> {
            if(eventsListView.getSelectionModel().getSelectedItem() == null)
                return;

            executors.submit(() -> {
                boolean alreadyJoined = ((Duck) UsersService.getInstance().getById(data.getUserId())).getGroup() == ((RaceEvent) eventsListView.getSelectionModel().getSelectedItem()).getGroupId();

                if(alreadyJoined)
                    EventsService.getInstance().unjoin(data.getUserId(), eventsListView.getSelectionModel().getSelectedItem().getId());
                else
                    EventsService.getInstance().join(data.getUserId(), eventsListView.getSelectionModel().getSelectedItem().getId());

                int selectedIndex = eventsListView.getSelectionModel().getSelectedIndex();

                reloadData(selectedIndex);
            });
        });

        eventsListView.setItems(eventsList);
        eventsListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);

                if (empty || event == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                executors.submit(() -> {
                    boolean bold = event.getSubscribers().parallelStream().anyMatch(s -> Objects.equals(s.getId(), data.getUserId())) ||
                            UsersService.getInstance().getById(data.getUserId()) instanceof Duck duck &&
                                    ((RaceEvent) event).getGroupId() == duck.getGroup();

                    Platform.runLater(() -> {
                        Text title = new Text(event.toString());
                        TextFlow textFlow = new TextFlow(title);
                        if(bold)
                            title.setStyle("-fx-font-weight: bold");
                        setText(null);
                        setGraphic(textFlow);
                    });
                });
            }
        });

        eventsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null)
                return;

            show(subscribeEventButton);
            show(joinEventButton);

            executors.submit(() -> {
                boolean subscribe = !eventsListView.getSelectionModel().getSelectedItem().getSubscribers().parallelStream().anyMatch(s -> Objects.equals(s.getId(), data.getUserId()));
                Platform.runLater(() -> {
                    subscribeEventButton.setText(subscribe ? "Subscribe" : "Unsubscribe");
                });
            });

            executors.submit(() -> {
                if(UsersService.getInstance().getById(data.getUserId()) instanceof Duck duck) {
                    boolean join = !(((RaceEvent) eventsListView.getSelectionModel().getSelectedItem()).getGroupId() == duck.getGroup());
                    Platform.runLater(() -> {
                        joinEventButton.setText(join ? "Join" : "Unjoin");
                    });
                }
            });
        });
    }

    public void setData(SessionData data) {
        this.data = data;

        setComponentsVisibilityState();
        reloadData();
    }

    private void reloadData() {
        executors.submit(() -> {
            List<Event> events = EventsService.getInstance().getEvents();

            Platform.runLater(() -> {
                eventsList.setAll(events);
            });
        });
    }

    private void reloadData(int itemIndex) {
        executors.submit(() -> {
            List<Event> events = EventsService.getInstance().getEvents();

            Platform.runLater(() -> {
                eventsList.setAll(events);
                eventsListView.getSelectionModel().select(itemIndex);
            });
        });
    }

    private void setComponentsVisibilityState() {
        executors.submit(() -> {
            if(data.isAdmin())
                show(adminControls);
            else if (data.isDuck())
                show(duckControls);
            else
                show(personControls);
        });
    }

    private void hide(Node node) {
        Platform.runLater(() -> {
            node.setVisible(false);
            node.setManaged(false);
        });
    }

    private void show(Node node) {
        Platform.runLater(() -> {
            node.setVisible(true);
            node.setManaged(true);
        });
    }

    @Override
    public void update(DuckTaskResult result) {
        executors.submit(() -> {

            if(result != null && result.event.getSubscribers().parallelStream().anyMatch(s -> Objects.equals(s.getId(), data.getUserId()))) {
                StringBuilder contentText = new StringBuilder();
                contentText.append(String.format("The event with name \"%s\" has finished!\n\nRanking\n", result.event.toString()));
                for(int i = 0; i < result.ducks.size(); i++) {
                    contentText.append(String.format("%s - %.1f\n", result.ducks.get(i).getUsername(), result.elapsedTimes.get(i)));
                }
                contentText.append("Total race time: " + result.endTime);

                Platform.runLater(() -> {
                    Stage stage = new Stage();
                    stage.initModality(Modality.NONE);
                    stage.setAlwaysOnTop(true);

                    VBox root = new VBox(10);
                    root.setPadding(new Insets(10));

                    Label headerLabel = new Label("Hey, " + UsersService.getInstance().getById(data.getUserId()).getUsername() + "!");
                    headerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

                    Label contentLabel = new Label("The event with name \"" + result.event + "\" has finished!\n\nRanking\n");

                    StringBuilder rankingText = new StringBuilder();
                    for (int i = 0; i < result.ducks.size(); i++) {
                        rankingText.append(String.format("%s - %.1f\n",
                                result.ducks.get(i).getUsername(),
                                result.elapsedTimes.get(i)));
                    }
                    rankingText.append(String.format("Total race time: %.1f", result.endTime));
                    contentLabel.setText(contentLabel.getText() + rankingText.toString());

                    root.getChildren().addAll(headerLabel, contentLabel);

                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Notification for user #" + data.getUserId());
                    stage.show();
                });
            }

            reloadData();
        });
    }
}
