package org.example.service;

import org.example.domain.*;
import org.example.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipRequestService implements Service<Long, FriendshipRequest>, Subject {
    private final FriendshipRequestRepository friendshipRequestRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final List<Observer> observers = new ArrayList<>();

    public FriendshipRequestService(FriendshipRequestRepository friendshipRequestRepository, FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRequestRepository = friendshipRequestRepository;
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    public List<User> getFriendRequests(Long userId) {
        return friendshipRequestRepository.getFriendRequests(userId);
    }

    public void sendFriendRequest(Long id1, Long id2) {
        friendshipRequestRepository.sendFriendRequest(id1, id2);
        notifyObservers("Friend request sent from user " + id1 + " to user " + id2);
    }

    public List<User> getFriendRequestsUser(Long userId) {
        return friendshipRequestRepository.getFriendRequestsUser(userId);
    }

    public void acceptFriendRequest(Long recipientId, Long senderId) {
        Optional<FriendshipRequest> requestOptional = friendshipRequestRepository.findRequestBySenderAndRecipient(senderId, recipientId);
        if (requestOptional.isPresent()) {
            FriendshipRequest request = requestOptional.get();
            request.setStatus(Status.ACCEPTED);
            friendshipRequestRepository.updateFR(request);
            User user1 = userRepository.findById(senderId);
            if (user1 == null) {
                throw new RuntimeException("Sender not found");
            }
            User user2 = userRepository.findById(recipientId);
            if (user2 == null) {
                throw new RuntimeException("Recipient not found");
            }
            friendshipRepository.add(new Friendship(generateIDFriendship(), user1.getId(), user2.getId()));
            notifyObservers("Friend request accepted from user " + senderId);
        } else {
            throw new RuntimeException("Friendship request not found.");
        }
    }

    public void denyFriendRequest(Long recipientId, Long senderId) {
        Optional<FriendshipRequest> requestOptional = friendshipRequestRepository.findRequestBySenderAndRecipient(senderId, recipientId);
        if (requestOptional.isPresent()) {
            FriendshipRequest request = requestOptional.get();
            request.setStatus(Status.REJECTED);
            friendshipRequestRepository.updateFR(request);
            notifyObservers("Friend request denied from user " + senderId);
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
    public Iterable<FriendshipRequest> findAll() {
        return friendshipRequestRepository.findAll();
    }

    @Override
    public Long generateID() {
        long maxNumber = 0;
        for (FriendshipRequest friendshipRequest : findAll()) {
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
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public boolean hasPendingRequests(Long userId) {
        List<FriendshipRequest> requests = (List<FriendshipRequest>) friendshipRequestRepository.findAll();
        return requests.stream()
                .anyMatch(request -> request.getRecipient().getId().equals(userId) && request.getStatus() == Status.PENDING);
    }
}
