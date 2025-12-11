package org.example.validation;

import org.example.domain.Event;
import org.example.exceptions.validationExceptions.flockExceptions.FlockTypeValidationException;

import java.util.Set;

public class EventValidationStrategy implements ValidationStrategy<Event> {
    @Override
    public void validate(Event event) {
        validateEventType(event);
    }

    private void validateEventType(Event entity) {
        final Set<String> VALID_FLOCK_TYPES = Set.of("RaceEvent");
        String flockType = entity.getEventType();
        if (!VALID_FLOCK_TYPES.contains(flockType)) {
            throw new FlockTypeValidationException(flockType + " is not a valid event type! Valid types are: " + VALID_FLOCK_TYPES);
        }
    }
}
