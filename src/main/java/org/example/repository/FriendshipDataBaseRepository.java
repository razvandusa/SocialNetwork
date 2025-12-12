package org.example.repository;

import org.example.domain.Friendship;
import org.example.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDataBaseRepository implements Repository<Long, Friendship>{
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
}
