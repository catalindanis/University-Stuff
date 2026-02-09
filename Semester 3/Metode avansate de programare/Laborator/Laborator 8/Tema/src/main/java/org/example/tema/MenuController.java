package org.example.tema;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import lombok.Setter;
import utils.SessionData;

import java.io.IOException;

public class MenuController {
//    @FXML Button ducksButton;
//    @FXML Button personsButton;
//    @FXML Button friendshipsButton;
    @FXML Button addFriendsButton;
    @FXML Button chatsButton;

    @Setter
    SessionData data;

    @FXML
    public void initialize() {
//        ducksButton.setOnAction(event -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("ducks.fxml"));
//                Parent root = loader.load();
//
//                Stage stage = new Stage();
//                stage.setTitle("Ducks");
//                stage.setScene(new Scene(root, 650, 450));
//                stage.show();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        personsButton.setOnAction(event -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("persons.fxml"));
//                Parent root = loader.load();
//
//                Stage stage = new Stage();
//                stage.setTitle("Persons");
//                stage.setScene(new Scene(root, 900, 450));
//                stage.show();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//
//        friendshipsButton.setOnAction(event -> {
//            try {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("friendships.fxml"));
//                Parent root = loader.load();
//
////                FriendshipsController controller = loader.getController();
////                controller.setSessionData(data);
//
//                Stage stage = new Stage();
//                stage.setTitle("Friendships");
//                stage.setScene(new Scene(root, 650, 450));
//                stage.show();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });

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
    }
}
