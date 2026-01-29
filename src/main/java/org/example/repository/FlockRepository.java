package org.example.repository;

import org.example.domain.Flock;

/**
 * Repository interface for managing Flock entities.
 */
public interface FlockRepository extends Repository<Long, Flock> {

    /**
     * Adds a duck to the flock with the given ID.
     *
     * @param flockId the ID of the flock
     * @param duckId  the ID of the duck to be added
     */
    void addDuckToFlock(Long flockId, Long duckId);

    /**
     * Removes a duck from the flock with the given ID.
     *
     * @param flockId the ID of the flock
     * @param duckId  the ID of the duck to be removed
     */
    void removeDuckFromFlock(Long flockId, Long duckId);
}