package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class Event extends Entity<Long> implements Subject {

    private String eventName;
    private List<Observer> subscribers = new ArrayList<>();

    Event(Long id, String eventName) {
        super(id);
        this.eventName = eventName;
    }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    @Override
    public void subscribe(Observer observer) {
        this.subscribers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        this.subscribers.remove(observer);
    }

    public void notifySubscribers(String message) {
        for (Observer observer : subscribers) {
            observer.onNotification(message);
        }
    }

    public List<Observer> getSubscribers() { return subscribers; }
}
