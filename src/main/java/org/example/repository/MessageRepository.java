package org.example.repository;

import org.example.domain.Message;

import java.util.List;

/**
 * Repository interface for managing Message entities.
 */
public interface MessageRepository extends Repository<Long, Message> {

    /**
     * Retrieves all messages exchanged between two users.
     *
     * @param userId1 the ID of the first user
     * @param userId2 the ID of the second user
     * @return a list of Message objects exchanged between the two users
     */
    List<Message> getMessagesBetweenUsers(Long userId1, Long userId2);

}