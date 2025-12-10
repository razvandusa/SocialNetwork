package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class Event extends Entity<Long> {

    private String eventName;
    private List<User> subscribers;

    Event(Long id, String eventName) {
        super(id);
        this.eventName = eventName;
        this.subscribers = new ArrayList<>();
    }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public void subscribe(User user) {
        this.subscribers.add(user);
    }

    public void unsubscribe(User user) {
        this.subscribers.remove(user);
    }

    protected void notifySubscribers(String message) {
        for (User user : subscribers) {
            user.onNotification(message);
        }
    }

    public List<User> getSubscribers() { return subscribers; }

    public abstract void start();
}
