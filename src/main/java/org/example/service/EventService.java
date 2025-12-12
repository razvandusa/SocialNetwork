package org.example.service;

import org.example.domain.*;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.validationExceptions.eventExceptions.EventAlreadyExists;
import org.example.exceptions.validationExceptions.eventExceptions.NotEnoughLanesException;
import org.example.exceptions.validationExceptions.eventExceptions.NotEnoughParticipantsException;
import org.example.exceptions.validationExceptions.userExceptions.IdValidationException;
import org.example.repository.EventDataBaseRepository;
import org.example.repository.EventFileRepository;
import org.example.repository.Repository;
import org.example.validation.ValidatorContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventService extends AbstractService<Long, Event> {
    private final Repository<Long, User> userRepository;
    private final ValidatorContext<Event> validatorEvent;
    /**
     * Creates an AbstractService with the given Repository
     *
     * @param eventRepository the repository used by the service
     */
    public EventService(Repository<Long, Event> eventRepository, Repository<Long, User> userRepository, ValidatorContext<Event> validatorEvent) {
        super(eventRepository);
        this.userRepository = userRepository;
        this.validatorEvent = validatorEvent;
    }

    /**
     * Adds a new event based on the provided details and validates its data.
     * If the event already exists, it throws an EventAlreadyExists exception.
     *
     * @param fields a variable number of strings representing the event data:
     *               the first parameter is the event type (e.g., "RaceEvent"),
     *               and the second parameter is the event name.
     * @throws EventAlreadyExists  if an event with the given name already exists.
     */
    @Override
    public void add(String... fields) {
        String eventType = fields[0];
        String eventName = fields[1];

        Event event;
        if (eventType.equals("RaceEvent")) {
            event = new RaceEvent(generateID(), eventType, eventName);
        } else {
            event = new Event(generateID(), eventType, eventName);
        }

        validatorEvent.validate(event);

        if (exists(eventName)) {
            throw new EventAlreadyExists("An event with this name already exists!");
        }

        repository.add(event);
    }

    /**
     * Removes the event with the specified identifier. If the identifier is not valid
     * or if the event is not found, appropriate exceptions are thrown.
     *
     * @param id the identifier of the event to be removed
     * @throws IdValidationException if the specified id cannot be parsed as long
     * @throws EntityNotFoundException if no event with the specified id exists
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
            throw new EntityNotFoundException("The event with id " + id + " was not found");
        }
        repository.remove(repository.findById(longId));
    }

    /**
     * Generates a new unique identifier for an Event.
     * The method iterates through all existing events and identifies the highest existing ID,
     * incrementing it by 1 to generate a new, unique identifier.
     *
     * @return the newly generated unique identifier as Long
     */
    @Override
    public Long generateID() {
        long maxNumber = 0;
        for (Event event : super.findAll()) {
            if (event.getId() > maxNumber) {
                maxNumber = event.getId();
            }
        }
        return maxNumber + 1;
    }

    /**
     * Checks if an event with the specified name exists in the repository.
     *
     * @param flockName the name of the event to search for
     * @return {@code true} if an event with the given name exists, {@code false} otherwise
     */
    public boolean exists(String flockName) {
        for (Event existingEvent : repository.findAll()) {
            if (existingEvent.getEventName().equals(flockName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a spectator to the specified event. This method validates the event ID
     * and user ID, ensuring they are numeric and exist in their respective repositories.
     * If the user is already a spectator of the event, an exception is thrown.
     *
     * @param eventId the identifier of the event to which the user will be added as a spectator
     * @param userId the identifier of the user to be added as a spectator to the event
     * @throws IdValidationException if the eventId or userId cannot be parsed as long
     * @throws EntityNotFoundException if no event or user with the specified id exists
     * @throws IllegalArgumentException if the user is already a spectator of the event
     */
    public void addSpectatorToEvent(String eventId, String userId) {
        long longEventId;
        try {
            longEventId = Long.parseLong(eventId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        if (repository.findById(longEventId) == null) {
            throw new EntityNotFoundException("The event with id " + eventId + " was not found");
        }
        Event event = repository.findById(longEventId);

        long longUserId;
        try {
            longUserId = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        if (userRepository.findById(longUserId) == null) {
            throw new EntityNotFoundException("The user with id " + eventId + " was not found");
        }
        User user = userRepository.findById(longUserId);
        if (event.getSubscribers().contains(user)) {
            throw new IllegalArgumentException(
                    "The user with id " + userId + " is already a spectator of this event"
            );
        }

        ((EventDataBaseRepository) repository).addSpectatorToEvent(longEventId, longUserId);
    }

    /**
     * Removes a spectator from a specified event. This method validates the event ID
     * and user ID, ensuring they are numeric and exist in their respective repositories.
     * If the user is not a spectator of the event, an exception is thrown.
     *
     * @param eventId the identifier of the event from which the spectator will be removed
     * @param userId the identifier of the user to be removed as a spectator from the event
     * @throws IdValidationException if the eventId or userId cannot be parsed as long
     * @throws EntityNotFoundException if no event or user with the specified id exists
     * @throws IllegalArgumentException if the user is not a spectator of the event
     */
    public void removeSpectatorFromEvent(String eventId, String userId) {
        long longEventId;
        try {
            longEventId = Long.parseLong(eventId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        if (repository.findById(longEventId) == null) {
            throw new EntityNotFoundException("The event with id " + eventId + " was not found");
        }
        Event event = repository.findById(longEventId);

        long longUserId;
        try {
            longUserId = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        if (userRepository.findById(longUserId) == null) {
            throw new EntityNotFoundException("The user with id " + eventId + " was not found");
        }
        User user = userRepository.findById(longUserId);

        if (!event.getSubscribers().contains(user)) {
            throw new IllegalArgumentException(
                    "The user with id " + userId + " is not a spectator of this event"
            );
        }

        ((EventDataBaseRepository) repository).removeSpectatorFromEvent(longEventId, longUserId);
    }

    /**
     * Adds a participant to a specific RaceEvent. This method validates the event ID and user ID,
     * ensuring they are numeric and exist in their respective repositories. It checks that the event
     * is a RaceEvent, the user is a Duck, and the Duck is a Swimmer. It also ensures the Duck is not
     * already a participant in the RaceEvent or a spectator in the event.
     *
     * @param eventId the identifier of the event to which the participant will be added
     * @param userId the identifier of the user to be added as a participant
     * @throws IdValidationException if the eventId or userId cannot be parsed as long
     * @throws EntityNotFoundException if no event or user with the specified id exists, or if the user is not a Duck
     * @throws IllegalArgumentException if the event is not a RaceEvent, if the Duck is not a Swimmer,
     *                                  if the user is already a spectator, or if the user is already a participant
     */
    public void addParticipantsToEvent(String eventId, String userId) {
        long longEventId;
        try {
            longEventId = Long.parseLong(eventId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        if (repository.findById(longEventId) == null) {
            throw new EntityNotFoundException("The event with id " + eventId + " was not found");
        }
        Event event = repository.findById(longEventId);

        long longUserId;
        try {
            longUserId = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        if (userRepository.findById(longUserId) == null) {
            throw new EntityNotFoundException("The user with id " + userId + " was not found");
        }
        User user = userRepository.findById(longUserId);

        if (!(event instanceof RaceEvent)) {
            throw new IllegalArgumentException("Only RaceEvents can have participants");
        }
        RaceEvent raceEvent = (RaceEvent) event;

        if (!(user instanceof Duck)) {
            throw new EntityNotFoundException("The user with id " + userId + " is not a duck");
        }
        Duck duck = (Duck) user;

        if (event.getSubscribers().contains(user)) {
            throw new IllegalArgumentException(
                    "The user with id " + userId + " is already a spectator of this event"
            );
        }

        if (!(duck instanceof Swimmer)) {
            throw new IllegalArgumentException(
                    "Duck " + duck.getUsername() + " is not compatible with RaceEvent"
            );
        }

        if (raceEvent.getParticipants().contains((Swimmer) user)) {
            throw new IllegalArgumentException(
                    "The user with id " + userId + " is already a participant in this RaceEvent"
            );
        }

        ((EventDataBaseRepository) repository).addParticipantToEvent(longEventId, longUserId);
    }

    /**
     * Removes a participant from a specific RaceEvent. This method ensures that the event ID
     * and duck ID are valid and checks that the event is a RaceEvent, the user is a Duck,
     * and the Duck is a Swimmer. It verifies that the Duck is a participant of the RaceEvent
     * before removal.
     *
     * @param eventId the identifier of the event from which the participant will be removed
     * @param duckId the identifier of the duck to be removed as a participant from the event
     * @throws IdValidationException if the eventId or duckId cannot be parsed as long
     * @throws EntityNotFoundException if no event or user with the specified ID exists, or if the user is not a Duck
     * @throws IllegalArgumentException if the event is not a RaceEvent, if the Duck is not a Swimmer,
     *                                  or if the Duck is not a participant of the RaceEvent
     */
    public void removeParticipantsFromEvent(String eventId, String duckId) {
        long longEventId;
        try {
            longEventId = Long.parseLong(eventId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        if (repository.findById(longEventId) == null) {
            throw new EntityNotFoundException("The event with id " + eventId + " was not found");
        }
        Event event = repository.findById(longEventId);

        long longDuckId;
        try {
            longDuckId = Long.parseLong(duckId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        if (userRepository.findById(longDuckId) == null) {
            throw new EntityNotFoundException("The user with id " + duckId + " was not found");
        }
        User user = userRepository.findById(longDuckId);

        if (!(event instanceof RaceEvent)) {
            throw new IllegalArgumentException("Only RaceEvents can have participants");
        }

        if (!(user instanceof Duck)) {
            throw new EntityNotFoundException("The user with id " + duckId + " is not a duck");
        }
        Duck duck = (Duck) user;

        if (!(duck instanceof Swimmer)) {
            throw new IllegalArgumentException(
                    "Duck " + duck.getUsername() + " is not compatible with RaceEvent"
            );
        }

        RaceEvent raceEvent = (RaceEvent) event;

        if (!raceEvent.getParticipants().contains((Swimmer) duck)) {
            throw new IllegalArgumentException(
                    "The duck with id " + duckId + " is not a participant in this event"
            );
        }

        ((EventDataBaseRepository) repository).removeParticipantFromEvent(longEventId, longDuckId);
    }

    /**
     * Adds a lane to a specified RaceEvent. This method validates the event ID
     * and the lane value, ensuring they are numeric and valid. It ensures the event
     * exists, is of type RaceEvent, and that the new lane distance is greater than
     * the distance of the last existing lane. If validations fail, corresponding
     * exceptions are thrown.
     *
     * @param eventId the identifier of the RaceEvent to which the lane will be added
     * @param laneValue the value of the lane to be added, in numeric format
     * @throws IdValidationException if the eventId cannot be parsed as long
     * @throws EntityNotFoundException if no event with the specified ID exists
     * @throws IllegalArgumentException if the event is not a RaceEvent, or
     *                                  if the laneValue is invalid or less than
     *                                  or equal to the last existing lane distance
     */
    public void addLaneToRaceEvent(String eventId, String laneValue) {
        long longEventId;
        try {
            longEventId = Long.parseLong(eventId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }

        Event event = repository.findById(longEventId);
        if (event == null) {
            throw new EntityNotFoundException("The event with id " + eventId + " was not found");
        }

        if (!(event instanceof RaceEvent)) {
            throw new IllegalArgumentException("The event is not a RaceEvent");
        }

        double doubleLaneValue;
        try {
            doubleLaneValue = Double.parseDouble(laneValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Lane must be a number");
        }

        RaceEvent raceEvent = (RaceEvent) event;

        List<Double> existingLanes = raceEvent.getLanes();
        if (!existingLanes.isEmpty() && doubleLaneValue <= existingLanes.getLast()) {
            throw new IllegalArgumentException("Lane distance must be greater than the previous lane distance");
        }

        ((EventDataBaseRepository) repository).addLaneToEvent(longEventId, doubleLaneValue);
    }

    /**
     * Removes a lane from a specific race event identified by its event ID and lane index.
     *
     * @param eventId the unique identifier of the race event. Must be a numeric string.
     * @param indexValue the 1-based index of the lane to be removed from the race event. Must be a numeric string.
     * @throws IdValidationException if the provided event ID is not a valid number.
     * @throws EntityNotFoundException if no event is found with the provided ID.
     * @throws IllegalArgumentException if the event is not a RaceEvent,
     *                                  if the index value is not a valid number,
     *                                  or if there are no lanes to remove.
     * @throws IndexOutOfBoundsException if the specified lane index is out of the valid range.
     */
    public void removeLaneFromRaceEvent(String eventId, String indexValue) {
        long longEventId;
        try {
            longEventId = Long.parseLong(eventId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }

        Event event = repository.findById(longEventId);
        if (event == null) {
            throw new EntityNotFoundException("The event with id " + eventId + " was not found");
        }

        if (!(event instanceof RaceEvent)) {
            throw new IllegalArgumentException("The event is not a RaceEvent");
        }

        Long intIndexValue;
        try {
            intIndexValue = Long.parseLong(indexValue);
            intIndexValue--;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Index must be a number");
        }

        RaceEvent raceEvent = (RaceEvent) event;

        List<Double> lanes = raceEvent.getLanes();
        if (lanes.isEmpty()) {
            throw new IllegalArgumentException("There are no lanes to remove");
        }
        if (intIndexValue < 0 || intIndexValue > lanes.size() - 1) {
            throw new IndexOutOfBoundsException("Invalid lane index: " + intIndexValue);
        }

        ((EventDataBaseRepository) repository).removeLaneFromEvent(longEventId, intIndexValue);
    }

    /**
     * Retrieves the lane data for a given race event based on the event ID.
     *
     * @param eventId the unique identifier of the event, provided as a String
     * @return a list of lane numbers represented as Double values
     * @throws IdValidationException if the provided event ID is not a valid number
     * @throws EntityNotFoundException if no event is found with the provided ID
     * @throws IllegalArgumentException if the event associated with the ID is not a RaceEvent
     */
    public List<Lane> getLanes(String eventId) {
        long longEventId;
        try {
            longEventId = Long.parseLong(eventId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }

        Event event = repository.findById(longEventId);
        if (event == null) {
            throw new EntityNotFoundException("The event with id " + eventId + " was not found");
        }

        if (!(event instanceof RaceEvent)) {
            throw new IllegalArgumentException("The event is not a RaceEvent");
        }

        List<Lane> lanes = ((EventDataBaseRepository) repository).getLanes(longEventId);
        return lanes;
    }

    /**
     * Starts the specified event by its ID. If the event is a race event, it verifies
     * that the number of lanes is enough for the participants and that there are
     * lanes available. If the conditions are met, it notifies subscribers and determines
     * the best ducks for the given race lanes.
     *
     * @param eventId the identifier of the event to start. It must be a string representing a valid number.
     * @return a list of DuckResult objects representing the results of the best participating ducks in the race event.
     * @throws IdValidationException if the provided eventId is not a valid numeric value.
     * @throws NotEnoughParticipantsException if the race event does not have enough participants compared to the number of lanes.
     * @throws NotEnoughLanesException if the race event does not have enough lanes to start.
     * @throws EntityNotFoundException if the event with the specified ID is not found.
     */
    public List<DuckResult> start(String eventId) {
        long longEventId;
        try {
            longEventId = Long.parseLong(eventId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        for (Event event : super.findAll()) {
            if (event.getId() == longEventId) {
                if (event.getEventType().equals("RaceEvent")) {
                    if (((RaceEvent) event).getLanes().size() > ((RaceEvent) event).getParticipants().size()) {
                        throw new NotEnoughParticipantsException("The event can't start because the number of lanes is greater than the number of participants");
                    }
                    if (((RaceEvent) event).getLanes().isEmpty()) {
                        throw new NotEnoughLanesException("The event can't start because the number of lanes is empty");
                    }
                    event.notifySubscribers("Race Event " + event.getEventName() + " has started!");
                    List<Duck> duckList = new ArrayList<>();
                    for (Swimmer s : ((RaceEvent)event).getParticipants()) {
                        duckList.add((Duck) s);
                    }
                    return bestDuckSelection(duckList, ((RaceEvent)event).getLanes());
                }
            }
        }

        throw new EntityNotFoundException("The event with id " + eventId + " was not found");
    }

    /**
     * Selects the best ducks from the given list based on their resistance and speed to compete
     * in races across the specified lanes and calculates the results for each selected duck.
     *
     * @param ducks in the list of ducks available for selection, each duck having attributes such
     *              as speed and resistance
     * @param lanes the list of race lane distances, where each lane represents a race the duck
     *              must compete in
     * @return a list of {@code DuckResult} objects containing the selected duck, the lane
     *         distance assigned, and the calculated time taken for the race
     */
    private List<DuckResult> bestDuckSelection(List<Duck> ducks, List<Double> lanes) {
        int M = lanes.size();
        List<Duck> bestSelection = new ArrayList<>();
        double[] bestTime = {Double.MAX_VALUE}; // Using an array to allow updates within recursive calls.
        List<Duck> currentSelection = new ArrayList<>();
        boolean[] used = new boolean[ducks.size()];

        ducks.sort((d1, d2) -> {
            if (!Objects.equals(d1.getResistance(), d2.getResistance()))
                return Double.compare(d1.getResistance(), d2.getResistance());
            return Double.compare(d2.getSpeed(), d1.getSpeed());
        });

        backtrack(0, M, ducks, lanes, currentSelection, used, bestSelection, bestTime, -1);

        List<DuckResult> results = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            Duck d = bestSelection.get(i);
            double time = 2 * lanes.get(i) / d.getSpeed();
            results.add(new DuckResult(d, lanes.get(i), time));
        }
        return results;
    }

    /**
     * A recursive method that performs backtracking to find the best range of ducks
     * across lanes with the goal of minimizing the maximum time required to cross all lanes.
     *
     * @param laneIndex         The current lane being processed.
     * @param M                 The total number of lanes.
     * @param ducks             The list of available ducks to choose from.
     * @param lanes             The list of distances for each lane.
     * @param currentSelection  The current selection of ducks matching lanes being processed.
     * @param used              An array indicating whether a duck has already been selected.
     * @param bestSelection     The list that holds the optimal selection of ducks minimizing the maximum time.
     * @param bestTime          An array holding the best (minimum) maximum time found so far.
     * @param lastResistance    The resistance value of the last duck added to the current selection.
     */
    private void backtrack(int laneIndex, int M, List<Duck> ducks, List<Double> lanes,
                           List<Duck> currentSelection, boolean[] used,
                           List<Duck> bestSelection, double[] bestTime, double lastResistance) {

        if (laneIndex == M) {
            double maxTime = 0;
            for (int i = 0; i < M; i++) {
                Duck d = currentSelection.get(i);
                double time = 2 * lanes.get(i) / d.getSpeed();
                maxTime = Math.max(maxTime, time);
            }
            if (maxTime < bestTime[0]) {
                bestTime[0] = maxTime;
                bestSelection.clear();
                bestSelection.addAll(currentSelection);
            }
            return;
        }

        for (int i = 0; i < ducks.size(); i++) {
            if (!used[i] && ducks.get(i).getResistance() >= lastResistance) {
                used[i] = true;
                currentSelection.add(ducks.get(i));
                backtrack(laneIndex + 1, M, ducks, lanes, currentSelection, used, bestSelection, bestTime, ducks.get(i).getResistance());
                currentSelection.removeLast();
                used[i] = false;
            }
        }
    }

    /**
     * Finds and returns the Event object associated with the given ID.
     * Converts the provided string ID to a long value and retrieves the corresponding Event.
     * If the conversion fails, an IdValidationException is thrown.
     *
     * @param id the string representation of the Event's ID
     * @return the Event associated with the given ID
     * @throws IdValidationException if the given ID is not a valid numeric string
     */
    public Event findById(String id) {
        long longId;
        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        return repository.findById(longId);
    }

    /**
     * Removes a user from all events in the repository. The user is removed as a participant
     * from any RaceEvent they are part of, or as a subscriber from general events. If the user
     * is successfully removed from at least one event, the repository is saved.
     *
     * @param userId The ID of the user to be removed from all events. The ID must be a numeric
     *               string; otherwise, an IdValidationException is thrown.
     * @throws IdValidationException if the provided userId is not a valid numeric string.
     */
    public void removeUserFromAllEvents(String userId) {
        long longUserId;
        try {
            longUserId = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }

        boolean removed = false;
        for (Event event : repository.findAll()) {
            if (event instanceof RaceEvent) {
                RaceEvent raceEvent = (RaceEvent) event;
                List<Swimmer> participantsToRemove = new ArrayList<>();
                for (Swimmer swimmer : raceEvent.getParticipants()) {
                    if (((Duck)swimmer).getId() == longUserId) {
                        participantsToRemove.add(swimmer);
                    }
                }
                if (!participantsToRemove.isEmpty()) {
                    raceEvent.getParticipants().removeAll(participantsToRemove);
                    removed = true;
                }
            }

            List<Observer> subscribersToRemove = new ArrayList<>();
            for (Observer user : event.getSubscribers()) {
                if (((User) user).getId() == longUserId) {
                    subscribersToRemove.add(user);
                }
            }
            if (!subscribersToRemove.isEmpty()) {
                event.getSubscribers().removeAll(subscribersToRemove);
                removed = true;
            }
        }

        if (removed) {
            ((EventFileRepository) repository).save();
        }
    }
}
