package org.example.exceptions.validationExceptions.eventExceptions;

public class EventAlreadyExists extends RuntimeException {
    public EventAlreadyExists(String message) {
        super(message);
    }
}
