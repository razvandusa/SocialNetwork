package org.example.repository;

import org.example.domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendshipRequestDataBaseRepository implements FriendshipRequestRepository {
    private final String url;
    private final String username;
    private final String password;

    public FriendshipRequestDataBaseRepository(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
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

    @Override
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

    @Override
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
    public List<User> getFriendRequestsUser(Long userId) {
        String sql = "SELECT u.* " +
                "FROM users u " +
                "JOIN friendship_requests fr ON u.id = fr.idsender " +
                "WHERE fr.idrecipient = ? AND fr.status = 'PENDING'";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
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

    @Override
    public void setPreparedStatementParametersForUpdate(PreparedStatement statement, FriendshipRequest entity) throws SQLException {
        statement.setLong(1, entity.getSender().getId());
        statement.setLong(2, entity.getRecipient().getId());
        statement.setString(3, entity.getStatus().name());
        statement.setLong(4, entity.getId());
    }

    @Override
    public void updateFR(FriendshipRequest entity) {
        String sql = "UPDATE friendship_requests SET idsender = ?, idrecipient = ?, status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setPreparedStatementParametersForUpdate(ps, entity);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating friendship request: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
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
                    return Optional.of(new Person(id, userType, username, email, password, surname, name, birthdate, occupation));
                } else if (userType.equals("Duck")) {
                    String duckType = rs.getString("duckType");
                    Double speed = rs.getDouble("speed");
                    Double resistance = rs.getDouble("resistance");
                    if (duckType.equals("SWIMMING")) {
                        return Optional.of(new SwimmingDuck(id, userType, username, email, password, speed, resistance));
                    } else if (duckType.equals("FLYING")) {
                        return Optional.of(new FlyingDuck(id, userType, username, email, password, speed, resistance));
                    } else if (duckType.equals("FLYING_AND_SWIMMING")) {
                        return Optional.of(new FlyingSwimmingDuck(id, userType, username, email, password, speed, resistance));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by ID: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public FriendshipRequest mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Long idRequest = resultSet.getLong("id");
        Long idSender = resultSet.getLong("idsender");
        Long idRecipient = resultSet.getLong("idrecipient");
        String statusStr = resultSet.getString("status");

        User sender = findUserById(idSender).orElseThrow(() -> new SQLException("Sender with ID " + idSender + " not found."));
        User recipient = findUserById(idRecipient).orElseThrow(() -> new SQLException("Recipient with ID " + idRecipient + " not found."));

        Status status = Status.valueOf(statusStr.toUpperCase());

        FriendshipRequest request = new FriendshipRequest(idRequest ,sender, recipient, status);

        return request;
    }

    @Override
    public Optional<FriendshipRequest> findRequestBySenderAndRecipient(Long senderId, Long recipientId) {
        String sql = "SELECT * FROM friendship_requests WHERE idsender = ? AND idrecipient = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, senderId);
            ps.setLong(2, recipientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding friendship request: " + e.getMessage(), e);
        }
        return Optional.empty();
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
        List<FriendshipRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM friendship_requests";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                FriendshipRequest request = mapResultSetToEntity(rs);
                requests.add(request);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all friendship requests: " + e.getMessage(), e);
        }
        return requests;
    }
}
