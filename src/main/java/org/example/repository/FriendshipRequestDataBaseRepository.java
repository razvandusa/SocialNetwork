package org.example.repository;

import org.example.domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipRequestDataBaseRepository implements Repository<Long, FriendshipRequest> {
    private final String url;
    private final String username;
    private final String password;

    public FriendshipRequestDataBaseRepository(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public List<User> getFriendRequests(Long userId) {
        String sql = "SELECT u.* " +
                "FROM users u " +
                "JOIN friendship_requests fr ON u.id = fr.idsender OR u.id = fr.idrecipient " +
                "WHERE (fr.idrecipient = ? OR fr.idsender = ?) AND fr.status = 'PENDING'";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, userId);
            ResultSet rs = ps.executeQuery();

            List<User> friendRequests = new ArrayList<>();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String userType = rs.getString("usertype");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                if (userType.equals("Person")) {
                    String surname = rs.getString("surname");
                    String name = rs.getString("name");
                    LocalDate birthdate = rs.getDate("birthdate").toLocalDate();
                    String occupation = rs.getString("occupation");
                    friendRequests.add(new Person(id, userType, username, email, password, surname, name, birthdate, occupation));
                } else if (userType.equals("Duck")) {
                    String duckType = rs.getString("duckType");
                    Double speed = rs.getDouble("speed");
                    Double resistance = rs.getDouble("resistance");
                    if (duckType.equals("SWIMMING")) {
                        friendRequests.add(new SwimmingDuck(id, userType, username, email, password, speed, resistance));
                    } else if (duckType.equals("FLYING")) {
                        friendRequests.add(new FlyingDuck(id, userType, username, email, password, speed, resistance));
                    } else if (duckType.equals("FLYING_AND_SWIMMING")) {
                        friendRequests.add(new FlyingSwimmingDuck(id, userType, username, email, password, speed, resistance));
                    }
                }
            }
            return friendRequests;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding friend requests for user with ID " + userId + ": " + e.getMessage(), e);
        }
    }

    public void sendFriendRequest(Long userId, Long friendId) {
        String sql = "INSERT INTO friendship_requests (idsender, idrecipient, status) VALUES (?, ?, 'PENDING')";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, friendId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error sending friend request from user with ID " + userId + " to user with ID " + friendId + ": " + e.getMessage(), e);
        }
    }

    public Optional<FriendshipRequest> save(FriendshipRequest entity) {
        String sql = "INSERT INTO friendship_requests (idsender, idrecipient, status) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, entity.getSender().getId());
            ps.setLong(2, entity.getRecipient().getId());
            ps.setString(3, entity.getStatus().name());

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving friendship request to DB: " + e.getMessage(), e);
        }
        return Optional.of(entity);
    }

    @Override
    public void add(FriendshipRequest entity) {

    }

    @Override
    public void remove(FriendshipRequest entity) {

    }

    @Override
    public FriendshipRequest findById(Long aLong) {
        return null;
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        return null;
    }
}
