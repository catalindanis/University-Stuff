package ro.mpp2026.festivalmuzicajavafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ro.mpp2026.festivalmuzicajavafx.domain.Show;
import ro.mpp2026.festivalmuzicajavafx.domain.ShowFilter;
import ro.mpp2026.festivalmuzicajavafx.service.AuthService;
import ro.mpp2026.festivalmuzicajavafx.service.ShowsService;
import ro.mpp2026.festivalmuzicajavafx.utils.Observer;

import java.util.Map;
import java.util.Optional;

public class FilteredShowsController implements PropsReceiver, Observer {

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

    private Optional<Map<String, Object>> props;

    private void initialize() {
        ShowsService.getInstance().subscribe(this);

        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        remainingSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("remainingSeats"));
        soldSeatsColumn.setCellValueFactory(cellData -> {
            Show show = cellData.getValue();
            int soldSeats = ShowsService.getInstance().getNumberOfSoldSeatsForShow(show.getId());
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

        loadData();
    }

    private void loadData() {
        if(props.isEmpty())
            return;

        ShowFilter showFilter = (ShowFilter) props.get().get("showsFilter");
        ObservableList<Show> shows = FXCollections.observableArrayList(ShowsService.getInstance().findAll(showFilter));
        showsTable.setItems(shows);
    }

    @Override
    public void setProps(Map<String, Object> props) {
        this.props = Optional.ofNullable(props);

        initialize();
    }

    @Override
    public void update() {
        loadData();
    }
}
