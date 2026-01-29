package org.example.repository;

import org.example.domain.Event;
import org.example.domain.Lane;

import java.util.List;

/**
 * Repository interface for managing Event entities.
 */
public interface EventRepository extends Repository<Long, Event> {

    /**
     * Adds a spectator to the event with the given ID.
     *
     * @param eventId the ID of the event
     * @param userId  the ID of the user who will be added as a spectator
     */
    void addSpectatorToEvent(Long eventId, Long userId);

    /**
     * Removes a spectator from the event with the given ID.
     *
     * @param eventId the ID of the event
     * @param userId  the ID of the user who will be removed as a spectator
     */
    void removeSpectatorFromEvent(Long eventId, Long userId);

    /**
     * Adds a participant (duck) to the event with the given ID.
     *
     * @param eventId the ID of the event
     * @param duckId  the ID of the duck to be added as a participant
     */
    void addParticipantToEvent(Long eventId, Long duckId);

    /**
     * Removes a participant (duck) from the event with the given ID.
     *
     * @param eventId the ID of the event
     * @param duckId  the ID of the duck to be removed as a participant
     */
    void removeParticipantFromEvent(Long eventId, Long duckId);

    /**
     * Adds a lane to the event with the given ID.
     *
     * @param eventId   the ID of the event
     * @param laneValue the value or number of the lane to add
     */
    void addLaneToEvent(Long eventId, Double laneValue);

    /**
     * Removes a lane from the event with the given ID.
     *
     * @param eventId    the ID of the event
     * @param indexValue the index or identifier of the lane to remove
     */
    void removeLaneFromEvent(Long eventId, Long indexValue);

    /**
     * Retrieves all lanes associated with the event with the given ID.
     *
     * @param eventId the ID of the event
     * @return a list of lanes for the event
     */
    List<Lane> getLanes(Long eventId);

    /**
     * Updates the status of the event with the given ID.
     *
     * @param eventId the ID of the event
     * @param status  the new status of the event (true = active, false = inactive)
     */
    void updateStatusEvent(Long eventId, boolean status);
}