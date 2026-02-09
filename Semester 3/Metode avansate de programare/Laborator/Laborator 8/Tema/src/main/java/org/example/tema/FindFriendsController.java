package org.example.tema;

import dto.FriendshipDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import models.Friendship;
import models.FriendshipStatus;
import models.User;
import services.FriendshipsService;
import services.UsersService;
import utils.Observer;
import utils.SessionData;

import java.util.List;

public class FindFriendsController implements Observer<FriendshipsService> {

    private SessionData data;
    private boolean firstDataLoad = true;

    @FXML
    ListView<User> myPendingFriendsList;
    private final ObservableList<User> myPendingFriends = FXCollections.observableArrayList();

    @FXML
    ListView<User> othersPendingFriendsList;
    private final ObservableList<User> othersPendingFriends = FXCollections.observableArrayList();

    @FXML
    ListView<User> nonFriendsList;
    private final ObservableList<User> nonFriends = FXCollections.observableArrayList();

    @FXML
    private Button cancelRequestButton;
    @FXML
    private Button rejectRequestButton;
    @FXML
    private Button acceptRequestButton;
    @FXML
    private Button sendRequestButton;

    @FXML
    public void initialize() {
        FriendshipsService.getInstance().subscribe(this);

        myPendingFriendsList.setItems(myPendingFriends);
        othersPendingFriendsList.setItems(othersPendingFriends);
        nonFriendsList.setItems(nonFriends);

        myPendingFriendsList.setCellFactory(lv -> {
            ListCell<User> cell = new ListCell<>() {
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);

                    if (empty || user == null) {
                        setText(null);
                    } else {
                        setText(user.getUsername());
                    }
                }
            };

            return cell;
        });
        othersPendingFriendsList.setCellFactory(lv -> {
            ListCell<User> cell = new ListCell<>() {
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);

                    if (empty || user == null) {
                        setText(null);
                    } else {
                        setText(user.getUsername());
                    }
                }
            };

            return cell;
        });
        nonFriendsList.setCellFactory(lv -> {
            ListCell<User> cell = new ListCell<>() {
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);

                    if (empty || user == null) {
                        setText(null);
                    } else {
                        setText(user.getUsername());
                    }
                }
            };

            return cell;
        });

        cancelRequestButton.setOnAction(e -> {
            if(myPendingFriendsList.getSelectionModel().getSelectedItem() == null)
                return;

            FriendshipsService.getInstance().removeByUsers(data.getUserId(), myPendingFriendsList.getSelectionModel().getSelectedItem().getId());
            reloadData();
        });

        rejectRequestButton.setOnAction(e -> {
            if(othersPendingFriendsList.getSelectionModel().getSelectedItem() == null)
                return;

            FriendshipsService.getInstance().rejectByUsers(data.getUserId(), othersPendingFriendsList.getSelectionModel().getSelectedItem().getId());
            reloadData();
        });

        acceptRequestButton.setOnAction(e -> {
            if(othersPendingFriendsList.getSelectionModel().getSelectedItem() == null)
                return;

            FriendshipsService.getInstance().acceptByUsers(data.getUserId(), othersPendingFriendsList.getSelectionModel().getSelectedItem().getId());
            reloadData();
        });

        sendRequestButton.setOnAction(e -> {
            if(nonFriendsList.getSelectionModel().getSelectedItem() == null)
                return;

            FriendshipDTO friendshipDTO = new FriendshipDTO(
                    data.getUserId(),
                    nonFriendsList.getSelectionModel().getSelectedItem().getId(),
                    FriendshipStatus.WAITING,
                    data.getUserId()
            );

            FriendshipsService.getInstance().add(friendshipDTO);
            reloadData();
        });
    }

    public void setData(SessionData data) {
        this.data = data;
        reloadData();
    }

    private void reloadData() {
        myPendingFriends.setAll(FriendshipsService.getInstance().getMyPendingFriendsForUser(data.getUserId()));
        int oldLength = othersPendingFriendsList.getItems().size();
        othersPendingFriends.setAll(FriendshipsService.getInstance().getOthersPendingFriendsForUser(data.getUserId()));
        nonFriends.setAll(FriendshipsService.getInstance().getNonFriendsForUser(data.getUserId()));
        if(!firstDataLoad && othersPendingFriendsList.getItems().size() > oldLength) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification for user #" + data.getUserId());
            alert.setHeaderText("Hey, " + UsersService.getInstance().getById(data.getUserId()).getUsername() + "!");
            alert.setContentText("You have a new friend request!");
            alert.show();
        }
        firstDataLoad = false;
    }

    @Override
    public void update(FriendshipsService friendshipsService) {
        reloadData();
    }
}
