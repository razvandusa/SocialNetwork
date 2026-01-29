package org.example.repository;

import org.example.domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDataBaseRepository implements FriendshipRepository{
    private final String url;
    private final String username;
    private final String password;

    public FriendshipDataBaseRepository(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Friendship entity) {
        String sql = "INSERT INTO friendships(id, firstFriendId, secondFriendId) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, entity.getId());
            ps.setLong(2, entity.getFirstFriendId());
            ps.setLong(3, entity.getSecondFriendId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Friendship entity) {
        String sql = "DELETE FROM friendships WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Friendship findById(Long id) {
        String sql = "SELECT * FROM friendships WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Long friendshipId = rs.getLong("id");
                Long firstFriendId = rs.getLong("firstFriendId");
                Long secondFriendId = rs.getLong("secondFriendId");
                return new Friendship(friendshipId, firstFriendId, secondFriendId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        String sql = "SELECT * FROM friendships";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            List<Friendship> friendships = new ArrayList<>();

            while (rs.next()) {
                Long friendshipId = rs.getLong("id");
                Friendship friendship = findById(friendshipId);
                friendships.add(friendship);
            }

            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int countFriendsOfUser(Long userId) {
        String sql = """
        SELECT COUNT(*) AS cnt
        FROM friendships
        WHERE firstFriendId = ? OR secondFriendId = ?
        """;

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, userId);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<User> findFriends(Long userId, int page, int pageSize) {
        String sql = "SELECT u.* " +
                "FROM users u " +
                "JOIN friendships f ON (u.id = f.firstFriendId OR u.id = f.secondFriendId) " +
                "WHERE (f.firstFriendId = ? OR f.secondFriendId = ?) AND u.id != ? " +
                "LIMIT ? OFFSET ?";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            ps.setLong(2, userId);
            ps.setLong(3, userId);
            ps.setInt(4, pageSize);
            ps.setInt(5, (page - 1) * pageSize);

            ResultSet rs = ps.executeQuery();
            List<User> friends = new ArrayList<>();

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
                    friends.add(new Person(id, userType, username, email, password, surname, name, birthdate, occupation));
                } else if (userType.equals("Duck")) {
                    String duckType = rs.getString("duckType");
                    Double speed = rs.getDouble("speed");
                    Double resistance = rs.getDouble("resistance");
                    if (duckType.equals("SWIMMING")) {
                        friends.add(new SwimmingDuck(id, userType, username, email, password, speed, resistance));
                    } else if (duckType.equals("FLYING")) {
                        friends.add(new FlyingDuck(id, userType, username, email, password, speed, resistance));
                    } else if (duckType.equals("FLYING_AND_SWIMMING")) {
                        friends.add(new FlyingSwimmingDuck(id, userType, username, email, password, speed, resistance));
                    }
                }
            }
            return friends;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>(); // always return an empty list if something fails
    }
}
