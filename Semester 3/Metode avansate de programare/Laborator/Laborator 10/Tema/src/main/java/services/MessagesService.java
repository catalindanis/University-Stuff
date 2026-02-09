package services;

import config.Config;
import dto.MessageDTO;
import factories.MessageFactory;
import lombok.Getter;
import models.Message;
import org.example.tema.ChatController;
import repositories.DatabaseMessageRepository;
import repositories.DatabaseRepository;
import utils.Observable;
import utils.Observer;

import java.util.ArrayList;
import java.util.List;

public class MessagesService implements Service<Message>, Observable<ChatController> {
    List<Observer<MessagesService>> observers = new ArrayList<>();

    @Getter
    private static final MessagesService instance = new MessagesService();
    DatabaseRepository<Long, Message> repository;

    private MessagesService() {
        String url = Config.getProperties().getProperty("db.url");
        String username = Config.getProperties().getProperty("db.username");
        String password = Config.getProperties().getProperty("db.password");
        repository = new DatabaseMessageRepository(url, username, password);
    }

    public Message add(MessageDTO messageDTO) {
        Message add = repository.add(MessageFactory.getInstance().createMessage(
                -1,
                messageDTO.getOwnerId(),
                messageDTO.getUsersId(),
                messageDTO.getMessage(),
                messageDTO.getDateTime(),
                messageDTO.getReply()
        ));

        notifyObservers();
        return add;
    }

    public Message remove(Message message) {
        Message remove = repository.remove(message);

        notifyObservers();
        return remove;
    }

    public List<Message> getAll() {
        return repository.getAll();
    }

    public List<Message> getAllByFriendship(Long id) {
        return ((DatabaseMessageRepository) repository).getAllByFriendship(id);
    }

    public Message getById(Long id) {
        return repository.get(id);
    }

    @Override
    public void subscribe(ChatController chatController) {
        observers.add(chatController);
    }

    @Override
    public void unsubscribe(ChatController chatController) {
        observers.remove(chatController);
    }

    @Override
    public void notifyObservers() {
        for(var observer : observers) {
            observer.update(this);
        }
    }
}
