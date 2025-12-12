package org.example.repository;

import org.example.domain.Flock;
import org.example.domain.FlyingDuck;
import org.example.domain.FlyingSwimmingDuck;
import org.example.domain.SwimmingDuck;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlockDataBaseRepository implements Repository<Long, Flock>{
    private final String url;
    private final String username;
    private final String password;

    public FlockDataBaseRepository(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Flock entity) {
        String sql = "INSERT INTO Flocks(id, flockname, flocktype) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getFlockName());
            ps.setString(3, entity.getFlockType());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Flock entity) {
        String sql = "DELETE FROM Flocks WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Flock findById(Long id) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sqlFlock = "SELECT * FROM Flocks WHERE id = ?";
            Flock flock;
            try (PreparedStatement ps = conn.prepareStatement(sqlFlock)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Long flockId = rs.getLong("id");
                    String flockName = rs.getString("flockname");
                    String flockType = rs.getString("flocktype");
                    flock = new Flock(flockId, flockName, flockType);
                } else {
                    return null;
                }
            }

            String sqlDucks = "SELECT d.* FROM Users d " +
                              "JOIN Flock_Duck fd ON fd.duckId = d.id " +
                              "WHERE fd.flockId = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlDucks)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Long duckId = rs.getLong("id");
                    String userType = rs.getString("usertype");
                    String username = rs.getString("username");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String duckType = rs.getString("duckType");
                    Double speed = rs.getDouble("speed");
                    Double resistance = rs.getDouble("resistance");
                    if (duckType.equals("SWIMMING")) {
                        SwimmingDuck duck = new SwimmingDuck(duckId, userType, username, email, password, speed, resistance);
                        flock.addDuck(duck);
                    } else if (duckType.equals("FLYING")) {
                        FlyingDuck duck = new FlyingDuck(duckId, userType, username, email, password, speed, resistance);
                        flock.addDuck(duck);
                    } else if (duckType.equals("FLYING_AND_SWIMMING")) {
                        FlyingSwimmingDuck duck = new FlyingSwimmingDuck(duckId, userType, username, email, password, speed, resistance);
                        flock.addDuck(duck);
                    }
                }
                return flock;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Flock> findAll() {
        String sql = "SELECT * FROM Flocks";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            List<Flock> flocks = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long flockId = rs.getLong("id");
                Flock flock = findById(flockId);
                flocks.add(flock);
            }
            return flocks;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addDuckToFlock(Long flockId, Long duckId){
        String sql = "INSERT INTO Flock_Duck(flockId, duckId) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, flockId);
            ps.setLong(2, duckId);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }

    public void removeDuckFromFlock(Long flockId, Long duckId){
        String sql = "DELETE FROM Flock_Duck WHERE flockId = ? AND duckId = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, flockId);
            ps.setLong(2, duckId);
            ps.executeUpdate();
        } catch (SQLException e) {}
    }
}
