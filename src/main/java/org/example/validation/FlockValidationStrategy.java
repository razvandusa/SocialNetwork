package org.example.validation;

import org.example.domain.Flock;
import org.example.exceptions.NameException;
import org.example.exceptions.validationExceptions.flockExceptions.FlockTypeValidationException;

import java.util.Set;

public class FlockValidationStrategy implements ValidationStrategy<Flock> {

    @Override
    public void validate(Flock entity) {
        validateFlockName(entity);
        validateFlockType(entity);
    }

    private void validateFlockName(Flock entity) {
        String name = entity.getFlockName();
        if (name == null || name.isEmpty()) {
            throw new NameException("Name of the flock cannot be empty!");
        }
    }

    private void validateFlockType(Flock entity) {
        final Set<String> VALID_FLOCK_TYPES = Set.of("Flyer", "Swimmer");
        String flockType = entity.getFlockType();
        if (!VALID_FLOCK_TYPES.contains(flockType)) {
            throw new FlockTypeValidationException(flockType + " is not a valid flock type! Valid types are: " + VALID_FLOCK_TYPES);
        }
    }

}
