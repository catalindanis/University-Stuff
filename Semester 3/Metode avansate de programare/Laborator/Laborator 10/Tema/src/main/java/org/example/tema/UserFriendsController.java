package org.example.tema;

import dto.FriendshipsFilterDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import models.Friendship;
import models.FriendshipStatus;
import models.User;
import services.FriendshipsService;
import services.UsersService;
import utils.ChatData;
import utils.Observer;
import utils.SessionData;
import utils.paging.Page;
import utils.paging.Pageable;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class UserFriendsController implements Observer<FriendshipsService> {
    SessionData data;

    @FXML private TableView<User> friendsTable;
    @FXML private TableColumn<User, String> usernameCol;
    private final ObservableList<User> friendsData = FXCollections.observableArrayList();

    @FXML private TextFlow textFlow;

    private FriendshipsFilterDTO filterFirst, filterSecond;
    private int pageNumber = 0;

    public void initialize() {
        FriendshipsService.getInstance().subscribe(this);

        filterFirst = new FriendshipsFilterDTO();
        filterSecond = new FriendshipsFilterDTO();

        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        friendsTable.setItems(friendsData);
    }

    public void setData(SessionData data) {
        this.data = data;

        filterFirst.setUser1(Optional.of(data.getUserId()));
        filterFirst.setStatus(Optional.of(FriendshipStatus.APPROVED));
        filterSecond.setUser2(Optional.of(data.getUserId()));
        filterSecond.setStatus(Optional.of(FriendshipStatus.APPROVED));

        reloadData();

        friendsTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    User clickedUser = row.getItem();
                    handleRowDoubleClick(clickedUser);
                }
            });

            return row;
        });
    }

    private void handleRowDoubleClick(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("chat.fxml"));
            Parent root = loader.load();

            ChatController controller = loader.getController();
            FriendshipsFilterDTO filter = new FriendshipsFilterDTO();
            filter.setUser1(Optional.of(Math.min(this.data.getUserId(), user.getId())));
            filter.setUser2(Optional.of(Math.max(this.data.getUserId(), user.getId())));
            Page<Friendship> friendshipPage = FriendshipsService.getInstance().getFriendships(new Pageable(0, 1), filter);
            ChatData data = new ChatData(this.data.getUserId(), List.of(user.getId()), friendshipPage.getElements().getFirst().getId());
            controller.setData(data);

            Stage stage = new Stage();
            stage.setTitle("Chat between user #" + this.data.getUserId() + " and #" + user.getId());
            stage.setScene(new Scene(root, 300, 450));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadData() {
        int pageSize = 5;
        Page<Friendship> pageFirst = FriendshipsService.getInstance().getFriendships(new Pageable(pageNumber, pageSize), filterFirst);
        Set<User> elements = new HashSet<>();

        pageFirst.getElements().forEach(friendship -> elements.add(UsersService.getInstance().getById(friendship.getUsers()[1])));

        if(pageFirst.getElements().size() < pageSize) {
            Page<Friendship> pageSecond = FriendshipsService.getInstance().getFriendships(new Pageable(pageNumber, pageSize - pageFirst.getElements().size()), filterSecond);
            pageSecond.getElements().forEach(friendship -> elements.add(UsersService.getInstance().getById(friendship.getUsers()[0])));
        }

        int maxPage = Math.max((int) Math.ceil(1.0 * pageFirst.getTotalNumberOfElements() / pageSize) - 1, 0);

        if(pageNumber > maxPage) {
            pageNumber = maxPage;
            reloadData();
            return;
        }

        if(pageNumber < 0) {
            pageNumber = 0;
            reloadData();
            return;
        }

        friendsData.setAll(elements);

        String pageText = (pageNumber + 1) +
                " / " +
                (maxPage + 1);
        Text t = new Text(pageText);
        textFlow.getChildren().setAll(t);
    }

    @FXML
    public void onNextPage() {
        pageNumber++;
        reloadData();
    }

    @FXML
    public void onPreviousPage() {
        if(pageNumber == 0)
            return;

        pageNumber--;
        reloadData();
    }

    @Override
    public void update(FriendshipsService friendshipsService) {
        reloadData();
    }
}
