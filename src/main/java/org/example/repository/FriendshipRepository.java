package org.example.repository;

import org.example.domain.Friendship;
import org.example.domain.User;

import java.util.List;

/**
 * Repository interface for managing Friendship entities.
 */
public interface FriendshipRepository extends Repository<Long, Friendship> {

    /**
     * Counts the number of friends a user has.
     *
     * @param userId the ID of the user
     * @return the total number of friends
     */
    int countFriendsOfUser(Long userId);

    /**
     * Retrieves a paginated list of friends for a given user.
     *
     * @param userId   the ID of the user
     * @param page     the page number (starting from 0)
     * @param pageSize the number of friends per page
     * @return a list of User objects representing the user's friends
     */
    List<User> findFriends(Long userId, int page, int pageSize);
}