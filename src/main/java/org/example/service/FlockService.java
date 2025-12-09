package org.example.service;

import org.example.domain.*;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.validationExceptions.userExceptions.IdValidationException;
import org.example.repository.Repository;
import org.example.validation.ValidatorContext;

public class FlockService extends AbstractService<Long, Flock>{
    private final Repository<Long, User> userRepository;
    private final ValidatorContext<Flock> validatorFlock;

    /**
     * Creates an FlockService with the given Repository
     *
     * @param userRepository the repository used by the service
     */
    public FlockService(Repository<Long, Flock> flockRepository, Repository<Long, User> userRepository, ValidatorContext<Flock> validatorFlock) {
        super(flockRepository);
        this.userRepository = userRepository;
        this.validatorFlock = validatorFlock;
    }

    @Override
    public void add(String... fields) {
        String flockName = fields[0];
        String flockType = fields[1];

        Flock flock = new Flock(generateID(), flockName, flockType);
        validatorFlock.validate(flock);

        if (exists(flockName)) {
            throw new IllegalArgumentException("A flock with this name already exists!");
        }

        repository.add(flock);
    }

    @Override
    public void remove(String id) {
        long longId;
        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        if (repository.findById(longId) == null) {
            throw new EntityNotFoundException("The flock with id " + id + " was not found");
        }
        repository.remove(repository.findById(longId));
    }

    @Override
    public Long generateID() {
        long maxNumber = 0;
        for (Flock flock : super.findAll()) {
            if (flock.getId() > maxNumber) {
                maxNumber = flock.getId();
            }
        }
        return maxNumber + 1;
    }

    public boolean exists(String flockName) {
        for (Flock existingFlock : repository.findAll()) {
            if (existingFlock.getFlockName().equals(flockName)) {
                return true;
            }
        }
        return false;
    }

    public void addDuckToFlock(String flockId, String duckId) {
        long longFlockId;
        try {
            longFlockId = Long.parseLong(flockId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        if (repository.findById(longFlockId) == null) {
            throw new EntityNotFoundException("The flock with id " + flockId + " was not found");
        }
        Flock flock = repository.findById(longFlockId);

        long longDuckId;
        try {
            longDuckId = Long.parseLong(duckId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        if (userRepository.findById(longDuckId) == null) {
            throw new EntityNotFoundException("The duck with id " + flockId + " was not found");
        }
        User user = userRepository.findById(longDuckId);
        if (!(user instanceof Duck)) {
            throw new EntityNotFoundException("The duck with id " + flockId + " was not found");
        }
        Duck duck = (Duck) user;

        String flockType = flock.getFlockType();
        if ((flockType.equals("Flyer") && !(duck instanceof Flyer)) ||
            (flockType.equals("Swimmer") && !(duck instanceof Swimmer))) {
            throw new IllegalArgumentException("Duck " + duck.getUsername() + " is not compatible with flock type " + flockType);
        }

        flock.addDuck(duck);
    }
}
