package org.example.exceptions;

import org.example.exceptions.validationExceptions.ValidationException;

public class NameException extends ValidationException {
    public NameException(String message) {
        super(message);
    }
}
