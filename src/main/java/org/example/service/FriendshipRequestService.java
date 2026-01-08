package org.example.service;

import org.example.domain.*;
import org.example.repository.FriendshipDataBaseRepository;
import org.example.repository.FriendshipRequestDataBaseRepository;
import org.example.repository.Repository;
import org.example.validation.ValidatorContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipRequestService extends AbstractService<Long, FriendshipRequest> implements Subject {
    private final Repository<Long, Friendship> friendshipRepository;
    private final Repository<Long, User> userRepository;
    private final List<Observer> observers = new ArrayList<>();

    public FriendshipRequestService(Repository<Long, FriendshipRequest> friendshipRequestRepository, Repository<Long, Friendship> friendshipRepository, Repository<Long, User> userRepository) {
        super(friendshipRequestRepository);
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    public List<User> getFriendRequests(Long userId) {
        return ((FriendshipRequestDataBaseRepository)repository).getFriendRequests(userId);
    }

    public void sendFriendRequest(Long id1, Long id2) {
        ((FriendshipRequestDataBaseRepository)repository).sendFriendRequest(id1, id2);
        notifySubscribers("Friend request sent from user " + id1 + " to user " + id2);
    }

    public List<User> getFriendRequestsUser(Long userId) {
        return ((FriendshipRequestDataBaseRepository)repository).getFriendRequestsUser(userId);
    }

    public void acceptFriendRequest(Long recipientId, Long senderId) {
        Optional<FriendshipRequest> requestOptional = ((FriendshipRequestDataBaseRepository)repository).findRequestBySenderAndRecipient(senderId, recipientId);
        if (requestOptional.isPresent()) {
            FriendshipRequest request = requestOptional.get();
            request.setStatus(Status.ACCEPTED);
            ((FriendshipRequestDataBaseRepository)repository).updateFR(request);
            LocalDateTime date = LocalDateTime.now();
            User user1 = userRepository.findById(senderId);
            if (user1 == null) {
                throw new RuntimeException("Sender not found");
            }
            User user2 = userRepository.findById(recipientId);
            if (user2 == null) {
                throw new RuntimeException("Recipient not found");
            }
            friendshipRepository.add(new Friendship(generateIDFriendship(), user1.getId(), user2.getId()));
            notifySubscribers("Friend request accepted from user " + senderId);
        } else {
            throw new RuntimeException("Friendship request not found.");
        }
    }

    public void denyFriendRequest(Long recipientId, Long senderId) {
        Optional<FriendshipRequest> requestOptional = ((FriendshipRequestDataBaseRepository)repository).findRequestBySenderAndRecipient(senderId, recipientId);
        if (requestOptional.isPresent()) {
            FriendshipRequest request = requestOptional.get();
            request.setStatus(Status.REJECTED);
            ((FriendshipRequestDataBaseRepository)repository).updateFR(request);
            notifySubscribers("Friend request dennied from user " + senderId);
        } else {
            throw new RuntimeException("Friendship request not found.");
        }
    }

    @Override
    public void add(String... fields) {

    }

    @Override
    public void remove(String id) {

    }

    @Override
    public Long generateID() {
        long maxNumber = 0;
        for (FriendshipRequest friendshipRequest : super.findAll()) {
            if (friendshipRequest.getId() > maxNumber) {
                maxNumber = friendshipRequest.getId();
            }
        }
        return maxNumber + 1;
    }

    public Long generateIDFriendship() {
        long maxNumber = 0;
        for (Friendship friendship : friendshipRepository.findAll()) {
            if (friendship.getId() > maxNumber) {
                maxNumber = friendship.getId();
            }
        }
        return maxNumber + 1;
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
            observer.onNotification(message);
        }
    }

    public boolean hasPendingRequests(Long userId) {
        List<FriendshipRequest> requests = (List<FriendshipRequest>) repository.findAll();
        return requests.stream()
                .anyMatch(request -> request.getRecipient().getId().equals(userId) && request.getStatus() == Status.PENDING);
    }
}
