package org.example.tema;

import dto.FriendshipDTO;
import dto.FriendshipsFilterDTO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import models.Friendship;
import services.FriendshipsService;
import utils.Observer;
import utils.paging.Page;
import utils.paging.Pageable;

public class FriendshipsController implements Observer<FriendshipsService> {
    @FXML private TableView<Friendship> table;

    @FXML private TableColumn<Friendship, Long> idCol;
    @FXML private TableColumn<Friendship, Long> firstUserCol;
    @FXML private TableColumn<Friendship, Long> secondUserCol;
    @FXML private TableColumn<Friendship, Long> statusCol;

    @FXML private TextFlow textFlow;

    @FXML private TextField firstUser;
    @FXML private TextField secondUser;

    private final FriendshipsFilterDTO filter = new FriendshipsFilterDTO();

    private final ObservableList<Friendship> data = FXCollections.observableArrayList();
    private int pageNumber = 0;

    @FXML
    public void initialize() {
        FriendshipsService.getInstance().subscribe(this);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        firstUserCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().getUsers()[0]));
        secondUserCol.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue().getUsers()[1]));

        firstUser.setTextFormatter(new TextFormatter<>(change -> {
            return change.getText().matches("\\d*?") ? change : null;
        }));

        secondUser.setTextFormatter(new TextFormatter<>(change -> {
            return change.getText().matches("\\d*?") ? change : null;
        }));

        table.setItems(data);

        reloadData();
    }

    public void reloadData() {
        int pageSize = 5;
        Page<Friendship> page = FriendshipsService.getInstance().getFriendships(new Pageable(pageNumber, pageSize), filter);
        int maxPage = Math.max((int) Math.ceil(1.0 * page.getTotalNumberOfElements() / pageSize) - 1, 0);

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

        data.setAll(page.getElements());
//        int totalElements = page.getTotalNumberOfElements();

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

    @FXML
    public void onAddFriendship() {
        try {
            FriendshipsService.getInstance().add(new FriendshipDTO(
                    Long.parseLong(firstUser.getText()),
                    Long.parseLong(secondUser.getText())
            ));

            firstUser.setText(null);
            secondUser.setText(null);
            reloadData();
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    @FXML
    public void onDeleteFriendship() {
        if(table.getSelectionModel().getSelectedItem() == null)
            return;

        FriendshipsService.getInstance().removeById(table.getSelectionModel().getSelectedItem().getId());
        reloadData();
    }

    @Override
    public void update(FriendshipsService friendshipsService) {
        reloadData();
    }

    public void onNumberOfCommunities() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Number of communities");
        alert.setHeaderText(null);
        String text = "Number of communities: " + String.valueOf(FriendshipsService.getInstance().getNumberOfCommunities());
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void onMostSociableCommunity() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Most sociable community");
        alert.setHeaderText(null);
        String text = "Most sociable community length: " + String.valueOf(FriendshipsService.getInstance().getMostSociableCommunity());
        alert.setContentText(text);
        alert.showAndWait();
    }
}
