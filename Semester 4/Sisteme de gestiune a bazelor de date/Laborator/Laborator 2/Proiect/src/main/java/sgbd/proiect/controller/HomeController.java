package sgbd.proiect.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import sgbd.proiect.service.BankAccountService;

public class HomeController {
    @FXML
    private Button dirtyReadsButton;
    @FXML
    private Button nonRepeatableReadsButton;
    @FXML
    private Button simulatePhantomReadButton;
    @FXML
    private Button lostUpdatesButton;
    @FXML
    private Button deadlockButton;
    @FXML
    private Button oneTransactionPerCommitButton;
    @FXML
    private Button commitEvery100InsertsButton;
    @FXML
    private Button insertWithOneCommitButton;

    private BankAccountService service;

    public void initialize() {
        dirtyReadsButton.setOnAction(this::dirtyReadsButtonClick);
        nonRepeatableReadsButton.setOnAction(this::nonRepeatableReadsButtonClick);
        simulatePhantomReadButton.setOnAction(this::simulatePhantomReadButtonClick);
        lostUpdatesButton.setOnAction(this::lostUpdatesButtonClick);
        deadlockButton.setOnAction(this::deadlockButtonClick);
        oneTransactionPerCommitButton.setOnAction(this::oneTransactionPerCommitButtonClick);
        commitEvery100InsertsButton.setOnAction(this::commitEvery100Inserts);
        insertWithOneCommitButton.setOnAction(this::insertWithOneCommitClick);

        service = new BankAccountService();
    }

    public void dirtyReadsButtonClick(ActionEvent event) {
        service.simulateDirtyRead();
    }

    private void nonRepeatableReadsButtonClick(ActionEvent event) {
        service.simulateNonRepeatableRead();
    }

    private void simulatePhantomReadButtonClick(ActionEvent event) {
        service.simulatePhantomRead();
    }

    private void lostUpdatesButtonClick(ActionEvent event) {
        service.simulateLostUpdate();
    }

    private void deadlockButtonClick(ActionEvent event) {
        service.simulateDeadlock();
    }

    private void oneTransactionPerCommitButtonClick(ActionEvent event) {
        service.insertWithAutoCommit();
    }

    private void commitEvery100Inserts(ActionEvent event) {
        service.insertWithBatchCommit();
    }

    private void insertWithOneCommitClick(ActionEvent event) {
        service.insertWithOneCommit();
    }
}
