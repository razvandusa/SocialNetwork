package org.example.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.domain.Message;
import org.example.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

public class MessageService implements Service<Long, Message> {
    private final MessageRepository messageRepository;
    private final ObservableList<Message> messages;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.messages = FXCollections.observableArrayList();
    }

    @Override
    public void add(String... fields) {}

    public void sendMessage(String... fields) {
        Message message;
        String text = fields[0];
        LocalDateTime timestamp = LocalDateTime.now();
        Long senderId = Long.parseLong(fields[1]);
        Long receiverId = Long.parseLong(fields[2]);
        message = new Message(generateID(), text, timestamp, senderId, receiverId);
        if (fields.length == 4) {
            Long replyToId = Long.parseLong(fields[3]);
            message.setReplyTo(messageRepository.findById(replyToId));
        }
        messageRepository.add(message);
        messages.add(message);
    }

    public ObservableList<Message> getMessagesBetweenUsers(Long userId1, Long userId2) {
        List<Message> messageList = messageRepository.getMessagesBetweenUsers(userId1, userId2);
        messages.setAll(messageList);
        return messages;
    }

    @Override
    public void remove(String id) {}

    @Override
    public Iterable<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Long generateID() {
        long maxNumber = 0;
        for (Message message : findAll()) {
            if (message.getId() > maxNumber) {
                maxNumber = message.getId();
            }
        }
        return maxNumber + 1;
    }
}
