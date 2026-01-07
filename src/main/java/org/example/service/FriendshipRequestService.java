package org.example.service;

import org.example.domain.*;
import org.example.repository.FriendshipRequestDataBaseRepository;
import org.example.repository.Repository;
import org.example.validation.ValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class FriendshipRequestService extends AbstractService<Long, FriendshipRequest> implements Subject {
    private final Repository<Long, FriendshipRequest> friendshipRepository;
    private final List<Observer> observers = new ArrayList<>();

    public FriendshipRequestService(Repository<Long, FriendshipRequest> friendshipRepository) {
        super(friendshipRepository);
        this.friendshipRepository = friendshipRepository;
    }

    public List<User> getFriendRequests(Long userId) {
        return ((FriendshipRequestDataBaseRepository)repository).getFriendRequests(userId);
    }

    public void sendFriendRequest(Long id1, Long id2) {
        ((FriendshipRequestDataBaseRepository)repository).sendFriendRequest(id1, id2);
        notifySubscribers("Friend request sent from user " + id1 + " to user " + id2);
    }

    @Override
    public void add(String... fields) {

    }

    @Override
    public void remove(String id) {

    }

    @Override
    public Long generateID() {
        return 0L;
    }

    @Override
    public void subscribe(Observer o) {

    }

    @Override
    public void unsubscribe(Observer o) {

    }

    @Override
    public void notifySubscribers(String message) {
        for (Observer observer : observers) {
            observer.onNotification("Friend request received");
        }
    }
}
