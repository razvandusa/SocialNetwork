package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Event extends Entity<Long> implements Subject {

    private String type;
    private String name;
    private boolean status = false;
    private List<Observer> subscribers = new ArrayList<>();

    public Event(Long id, String type, String name) {
        super(id);
        this.type = type;
        this.name = name;
    }

    // Getters / Setters
    public String getType() { return type; }
    public String getName() { return name; }
    public boolean getStatus() { return status; }
    public void setType(String type) { this.type = type; }
    public void setName(String name) { this.name = name; }
    public void setStatus(boolean status) { this.status = status; }

    // Observer pattern
    @Override
    public void addObserver(Observer observer) {
        if (!subscribers.contains(observer)) {
            this.subscribers.add(observer);
        }
    }

    public void removeObserver(Observer observer) {
        this.subscribers.remove(observer);
    }

    public void startEvent() {
        notifyObservers("Event \"" + name + "\" has started!");
    }

    public void notifyObservers(String message) {
        for (Observer observer : subscribers) {
            observer.update(message);
        }
    }

    public List<Observer> getSubscribers() {
        return subscribers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event{");
        sb.append("id=").append(getId());
        sb.append(", eventType='").append(type).append('\'');
        sb.append(", eventName='").append(name).append('\'');
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