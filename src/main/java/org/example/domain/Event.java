package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Event extends Entity<Long> implements Subject {

    private String eventType;
    private String eventName;
    private List<Observer> subscribers = new ArrayList<>();

    public Event(Long id, String eventType, String eventName) {
        super(id);
        this.eventType = eventType;
        this.eventName = eventName;
    }

    public String getEventType() { return eventType; }
    public String getEventName() { return eventName; }
    public void setEventType(String eventType) { this.eventType = eventType; }
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event{");
        sb.append("id=").append(getId());
        sb.append(", eventType='").append(eventType).append('\'');
        sb.append(", eventName='").append(eventName).append('\'');
        sb.append(", subscribers=[");

        for (int i = 0; i < subscribers.size(); i++) {
            sb.append(((User) subscribers.get(i)).getId());
            if (i < subscribers.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("]}");
        return sb.toString();
    }
}
