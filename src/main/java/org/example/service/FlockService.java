package org.example.service;

import org.example.domain.*;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.validationExceptions.userExceptions.IdValidationException;
import org.example.repository.FlockFileRepository;
import org.example.repository.Repository;
import org.example.validation.ValidatorContext;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Adds a new flock to the repository using the provided fields.
     * Validates the flock data and ensures no duplicate flock names exist.
     *
     * @param fields a variable number of strings representing the flock data.
     *               The first element is the flock name, and the second element is the flock type.
     * @throws IllegalArgumentException if a flock with the specified name already exists.
     */
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

    /**
     * Removes an entity with the specified identifier from the repository.
     * Validates the identifier before proceeding with the removal process.
     * If the identifier is invalid or the entity does not exist, a relevant exception is thrown.
     *
     * @param id the identifier of the entity to be removed
     * @throws IdValidationException if the provided identifier is not a valid number
     * @throws EntityNotFoundException if no entity with the specified identifier is found in the repository
     */
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

    /**
     * Generates a new unique identifier for a flock entity by finding the
     * maximum existing identifier among all flocks and incrementing it by one.
     *
     * @return a newly generated unique identifier as a {@code Long}
     */
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

    /**
     * Checks if a flock with the specified name exists in the repository.
     *
     * @param flockName the name of the flock to check for existence
     * @return {@code true} if a flock with the given name exists, {@code false} otherwise
     */
    public boolean exists(String flockName) {
        for (Flock existingFlock : repository.findAll()) {
            if (existingFlock.getFlockName().equals(flockName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a duck to a specific flock. Validates the identifiers, ensures the existence
     * and compatibility of the flock and duck, and prevents duplication before performing the addition.
     *
     * @param flockId the identifier of the flock to which the duck will be added
     * @param duckId the identifier of the duck to be added to the flock
     * @throws IdValidationException if the provided flockId or duckId is not a valid number
     * @throws EntityNotFoundException if the specified flock or duck does not exist,
     *                                  or*/
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
            throw new EntityNotFoundException("The duck with id " + duckId + " was not found");
        }
        User user = userRepository.findById(longDuckId);
        if (!(user instanceof Duck)) {
            throw new EntityNotFoundException("The duck with id " + duckId + " was not found");
        }
        Duck duck = (Duck) user;

        String flockType = flock.getFlockType();
        if ((flockType.equals("Flyer") && !(duck instanceof Flyer)) ||
            (flockType.equals("Swimmer") && !(duck instanceof Swimmer))) {
            throw new IllegalArgumentException("Duck " + duck.getUsername() + " is not compatible with flock type " + flockType);
        }
        if (flock.getDucks().contains(duck)) {
            throw new IllegalArgumentException(
                    "The Duck with id " + duckId + " is already taking part of this flock"
            );
        }

        flock.addDuck(duck);
        ((FlockFileRepository) repository).save();
    }

    /**
     * Removes a duck from a specified flock. Validates the provided identifiers,
     * ensures the existence of both the flock and the duck, checks type compatibility,
     * and verifies the duck’s presence in the flock before performing the removal.
     *
     * @param flockId the identifier of the flock from which the duck will be removed
     * @param duckId the identifier of the duck to be removed from the flock
     * @throws IdValidationException if the provided flockId or duckId is not a valid number
     * @throws EntityNotFoundException if the specified flock or duck does not exist
     * @throws IllegalArgumentException if the specified duck is not compatible with the flock
     *                                   or is not a part of the flock
     */
    public void removeDuckFromFlock(String flockId, String duckId) {
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

        if (!flock.getDucks().contains(duck)) {
            throw new IllegalArgumentException("The duck with id " + duckId + " is not a part of this flock");
        }

        flock.removeDuck(duck);
        ((FlockFileRepository) repository).save();
    }

    /**
     * Removes a duck, identified by its unique identifier, from all flocks in the repository.
     * Validates the duck ID before attempting removal. If the ID is invalid or no duck with the
     * specified identifier exists in any flock, an exception is thrown. Saves changes if any
     * removals are made.
     *
     * @param duckId the unique identifier of the duck to be removed from all flocks
     * @throws IdValidationException if the provided duckId is not a valid numeric value
     */
    public void removeDuckFromAllFlocks(String duckId) {
        long longDuckId;
        try {
            longDuckId = Long.parseLong(duckId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }

        boolean removed = false;
        for (Flock flock : repository.findAll()) {
            List<Duck> ducksToRemove = new ArrayList<>();
            for (Duck duck : flock.getDucks()) {
                if (duck.getId().equals(longDuckId)) {
                    ducksToRemove.add(duck);
                }
            }
            if (!ducksToRemove.isEmpty()) {
                flock.getDucks().removeAll(ducksToRemove);
                removed = true;
            }
        }

        if (removed) {
            ((FlockFileRepository) repository).save();
        }
    }
}
