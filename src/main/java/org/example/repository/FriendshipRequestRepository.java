package org.example.repository;

import org.example.domain.FriendshipRequest;
import org.example.domain.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing FriendshipRequest entities.
 */
public interface FriendshipRequestRepository extends Repository<Long, FriendshipRequest> {

    /**
     * Retrieves all users who have sent a friend request to the given user.
     *
     * @param userId the ID of the user receiving friend requests
     * @return a list of User objects who sent friend requests
     */
    List<User> getFriendRequests(Long userId);

    /**
     * Sends a friend request from one user to another.
     *
     * @param userId   the ID of the user sending the request
     * @param friendId the ID of the user receiving the request
     */
    void sendFriendRequest(Long userId, Long friendId);

    /**
     * Saves a FriendshipRequest entity.
     *
     * @param entity the FriendshipRequest to save
     * @return an Optional containing the saved entity if successful
     */
    Optional<FriendshipRequest> save(FriendshipRequest entity);

    /**
     * Retrieves the list of users for whom the given user has sent friend requests.
     *
     * @param userId the ID of the user who sent friend requests
     * @return a list of User objects who received friend requests from this user
     */
    List<User> getFriendRequestsUser(Long userId);

    /**
     * Sets parameters of a PreparedStatement for updating a FriendshipRequest entity.
     *
     * @param statement the PreparedStatement to set parameters on
     * @param entity    the FriendshipRequest entity
     * @throws SQLException if an SQL error occurs
     */
    void setPreparedStatementParametersForUpdate(PreparedStatement statement, FriendshipRequest entity) throws SQLException;

    /**
     * Updates a FriendshipRequest entity in the database.
     *
     * @param entity the FriendshipRequest to update
     */
    void updateFR(FriendshipRequest entity);

    /**
     * Finds a user by their ID.
     *
     * @param userId the ID of the user
     * @return an Optional containing the User if found
     */
    Optional<User> findUserById(Long userId);

    /**
     * Maps a ResultSet row to a FriendshipRequest entity.
     *
     * @param resultSet the ResultSet to map
     * @return a FriendshipRequest entity
     * @throws SQLException if an SQL error occurs
     */
    FriendshipRequest mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    /**
     * Finds a friendship request between a sender and a recipient.
     *
     * @param senderId    the ID of the user who sent the request
     * @param recipientId the ID of the user who received the request
     * @return an Optional containing the FriendshipRequest if found
     */
    Optional<FriendshipRequest> findRequestBySenderAndRecipient(Long senderId, Long recipientId);
}