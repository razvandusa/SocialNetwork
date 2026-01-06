package org.example.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.domain.Friendship;
import org.example.domain.Message;
import org.example.domain.User;
import org.example.repository.MessageDataBaseRepository;
import org.example.repository.Repository;
import org.example.repository.UserDataBaseRepository;
import org.example.validation.ValidatorContext;

import java.time.LocalDateTime;
import java.util.List;

public class MessageService extends AbstractService<Long, Message> {
    private final Repository<Long, Message> messageRepository;
    private final ObservableList<Message> messages;

    public MessageService(Repository<Long, Message> messageRepository) {
        super(messageRepository);
        this.messageRepository = messageRepository;
        this.messages = FXCollections.observableArrayList();
    }

    @Override
    public void add(String... fields) {
        Long id = generateID();
        String text = fields[0];
        LocalDateTime timestamp = LocalDateTime.now();
        Long fromId = Long.parseLong(fields[1]);
        Long toId = Long.parseLong(fields[2]);

        Message message = new Message(id, text, timestamp ,fromId, toId);
        messageRepository.add(message);
        messages.add(message);
    }

    public ObservableList<Message> getMessagesBetweenUsers(Long userId1, Long userId2) {
        List<Message> messageList = ((MessageDataBaseRepository)messageRepository).getMessagesBetweenUsers(userId1, userId2);
        messages.setAll(messageList);
        return messages;
    }

    @Override
    public void remove(String id) {}

    @Override
    public Long generateID() {
        long maxNumber = 0;
        for (Message message : super.findAll()) {
            if (message.getId() > maxNumber) {
                maxNumber = message.getId();
            }
        }
        return maxNumber + 1;
    }
}
