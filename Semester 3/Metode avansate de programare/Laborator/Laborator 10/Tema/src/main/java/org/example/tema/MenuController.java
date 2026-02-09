package org.example.tema;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import services.EventsService;
import utils.SessionData;

import java.io.IOException;

public class MenuController {
    @FXML Button ducksButton;
    @FXML Button personsButton;
    @FXML Button friendshipsButton;
    @FXML Button pageButton;
    @FXML Button addFriendsButton;
    @FXML Button chatsButton;
    @FXML Button eventsButton;

    SessionData data;

    public void setData(SessionData data) {
        this.data = data;
        setComponentsVisibilityState();
    }

    @FXML
    public void initialize() {
        ducksButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ducks.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Ducks");
                stage.setScene(new Scene(root, 650, 450));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        personsButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("persons.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Persons");
                stage.setScene(new Scene(root, 900, 450));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        friendshipsButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("friendships.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Friendships");
                stage.setScene(new Scene(root, 650, 450));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        pageButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("page.fxml"));
                Parent root = loader.load();

                PageController controller = loader.getController();
                controller.setData(data);

                Stage stage = new Stage();
                stage.setTitle("Profile page for user #" + data.getUserId());
                stage.setScene(new Scene(root, 350, 450));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        addFriendsButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("findfriends.fxml"));
                Parent root = loader.load();

                FindFriendsController controller = loader.getController();
                controller.setData(data);

                Stage stage = new Stage();
                stage.setTitle("Find friends for user #" + data.getUserId());
                stage.setScene(new Scene(root, 650, 450));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        chatsButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("userfriends.fxml"));
                Parent root = loader.load();

                UserFriendsController controller = loader.getController();
                controller.setData(data);

                Stage stage = new Stage();
                stage.setTitle("Friends list for user #" + data.getUserId());
                stage.setScene(new Scene(root, 650, 450));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        eventsButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("events.fxml"));
                Parent root = loader.load();

                EventsController controller = loader.getController();
                controller.setData(data);

                Stage stage = new Stage();
                stage.setTitle("Events list for user #" + data.getUserId());
                stage.setScene(new Scene(root, 650, 450));
                stage.show();

                stage.setOnCloseRequest((e) -> {
                    EventsService.getInstance().unsubscribe(controller);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setComponentsVisibilityState() {
        if(data.isAdmin())
            return;

        hide(personsButton);
        hide(ducksButton);
        hide(friendshipsButton);
    }

    private void hide(Node node) {
        node.setVisible(false);
        node.setManaged(false);
    }

    private void show(Node node) {
        node.setVisible(true);
        node.setManaged(true);
    }
}
