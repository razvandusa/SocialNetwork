package org.example.domain;

public interface Subject {
    void subscribe(Observer o);

    void unsubscribe(Observer o);

    void notifySubscribers(String message);
}
