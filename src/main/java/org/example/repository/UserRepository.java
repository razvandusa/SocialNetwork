package org.example.repository;

import org.example.domain.User;

import java.util.List;

/**
 * Repository interface for managing User entities.
 */
public interface UserRepository extends Repository<Long, User> {

    /**
     * Retrieves a paginated list of users.
     *
     * @param pageNumber the page number (starting from 0)
     * @param pageSize   the number of users per page
     * @return a list of User objects for the specified page
     */
    List<User> getUsersPage(Long pageNumber, Long pageSize);

    /**
     * Counts the total number of users in the repository.
     *
     * @return the total number of users
     */
    Long getUsersCount();

    /**
     * Retrieves a paginated list of ducks filtered by type.
     *
     * @param type       the type of ducks to filter
     * @param pageNumber the page number (starting from 0)
     * @param pageSize   the number of ducks per page
     * @return a list of User objects representing ducks of the given type
     */
    List<User> findDucksPageByType(String type, Long pageNumber, Long pageSize);

    /**
     * Counts the total number of ducks of a specific type.
     *
     * @param type the type of ducks to count
     * @return the total number of ducks of the given type
     */
    Long getDucksCountByType(String type);
}