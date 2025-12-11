package org.example.exceptions.validationExceptions.eventExceptions;

public class NotEnoughLanesException extends RuntimeException {
    public NotEnoughLanesException(String message) {
        super(message);
    }
}
