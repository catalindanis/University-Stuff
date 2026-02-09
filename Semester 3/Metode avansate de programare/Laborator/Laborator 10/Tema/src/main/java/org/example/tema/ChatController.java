package org.example.tema;

import dto.MessageDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.input.ScrollEvent;
import lombok.Setter;
import models.Message;
import services.MessagesService;
import services.UsersService;
import utils.ChatData;
import utils.Observer;

import java.time.LocalDateTime;

public class ChatController implements Observer<MessagesService> {
    ChatData data;

    @FXML
    private ListView<Message> messagesList;
    private final ObservableList<Message> messages = FXCollections.observableArrayList();

    @FXML
    private TextField messageField;

    @FXML
    public void initialize() {
        MessagesService.getInstance().subscribe(this);

        messagesList.setScaleY(-1);

        messagesList.setCellFactory(lv -> {
            ListCell<Message> cell = new ListCell<>() {
                @Override
                protected void updateItem(Message msg, boolean empty) {
                    super.updateItem(msg, empty);

                    if (empty || msg == null) {
                        setText(null);
                    } else {
                        if(msg.getReply() == null)
                            setText(UsersService.getInstance().getById(msg.getFromUser()).getUsername() + ": " + msg.getMessage());
                        else
                            setText(UsersService.getInstance().getById(msg.getFromUser()).getUsername() + " (reply to: \"" + MessagesService.getInstance().getById(msg.getReply()).getMessage() + "\"): " + msg.getMessage());
                    }
                }
            };

            cell.setScaleY(-1);
            return cell;
        });

        Platform.runLater(() -> {
            VirtualFlow<?> flow = (VirtualFlow<?>) messagesList.lookup(".virtual-flow");

            messagesList.addEventFilter(ScrollEvent.SCROLL, event -> {
                if (flow != null) {
                    flow.setPosition(flow.getPosition() + event.getDeltaY() / 500);
                    event.consume();
                }
            });
        });

        messagesList.setItems(messages);
    }

    public void setData(ChatData data) {
        this.data = data;
        reloadData();
    }

    @FXML
    private void sendMessage() {
        MessageDTO messageDTO = new MessageDTO(
            data.getOwnerId(),
            data.getUsersId(),
                messageField.getText(),
                LocalDateTime.now(),
                null
        );

        if(messagesList.getSelectionModel().getSelectedItem() != null) {
            messageDTO.setReply(
                    messagesList.getSelectionModel().getSelectedItem().getId()
            );
        }

        MessagesService.getInstance().add(messageDTO);

        reloadData();
        messageField.setText("");
    }

    private void reloadData() {
        messages.setAll(MessagesService.getInstance().getAllByFriendship(data.getFriendshipId()).reversed());
    }

    @Override
    public void update(MessagesService messagesService) {
        reloadData();
    }
}
