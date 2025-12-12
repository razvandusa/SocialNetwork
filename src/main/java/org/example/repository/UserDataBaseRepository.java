package org.example.repository;

import org.example.domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDataBaseRepository implements Repository<Long, User>{
    private final String url;
    private final String username;
    private final String password;

    public UserDataBaseRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(User entity) {
        String sql = "INSERT INTO users (id, userType, username, email, password, surname, name, birthdate, occupation, duckType, speed, resistance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getUserType());
            ps.setString(3, entity.getUsername());
            ps.setString(4, entity.getEmail());
            ps.setString(5, entity.getPassword());
            if (entity.getUserType().equals("Person")) {
                Person person = (Person) entity;
                ps.setString(6, person.getSurname());
                ps.setString(7, person.getName());
                ps.setDate(8, java.sql.Date.valueOf(person.getBirthdate()));
                ps.setString(9, person.getOccupation());
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.DOUBLE);
                ps.setNull(12, Types.DOUBLE);
            } else if (entity.getUserType().equals("Duck")) {
                Duck duck = (Duck) entity;
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.DATE);
                ps.setNull(9, Types.VARCHAR);
                if (duck instanceof SwimmingDuck) {
                    ps.setString(10, "SWIMMING");
                } else if (duck instanceof FlyingDuck) {
                    ps.setString(10, "FLYING");
                } else if (duck instanceof FlyingSwimmingDuck) {
                    ps.setString(10, "FLYING_AND_SWIMMING");
                }
                ps.setDouble(11, duck.getSpeed());
                ps.setDouble(12, duck.getResistance());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(User entity) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long userId = rs.getLong("id");
                String userType = rs.getString("usertype");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                if (userType.equals("Person")) {
                    String surname = rs.getString("surname");
                    String name = rs.getString("name");
                    LocalDate birthdate = rs.getDate("birthdate").toLocalDate();
                    String occupation = rs.getString("occupation");
                    return new Person(userId, userType, username, email, password, surname, name, birthdate, occupation);
                } else if (userType.equals("Duck")) {
                    String duckType = rs.getString("duckType");
                    Double speed = rs.getDouble("speed");
                    Double resistance = rs.getDouble("resistance");
                    if (duckType.equals("SWIMMING")) {
                        return new SwimmingDuck(userId, userType, username, email, password, speed, resistance);
                    } else if (duckType.equals("FLYING")) {
                        return new FlyingDuck(userId, userType, username, email, password, speed, resistance);
                    } else if (duckType.equals("FLYING_AND_SWIMMING")) {
                        return new FlyingSwimmingDuck(userId, userType, username, email, password, speed, resistance);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users")) {
            ResultSet rs = ps.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                Long userId = rs.getLong("id");
                String userType = rs.getString("usertype");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");
                if (userType.equals("Person")) {
                    String surname = rs.getString("surname");
                    String name = rs.getString("name");
                    LocalDate birthdate = rs.getDate("birthdate").toLocalDate();
                    String occupation = rs.getString("occupation");
                    users.add(new Person(userId, userType, username, email, password, surname, name, birthdate, occupation));
                } else if (userType.equals("Duck")) {
                    String duckType = rs.getString("duckType");
                    Double speed = rs.getDouble("speed");
                    Double resistance = rs.getDouble("resistance");
                    if (duckType.equals("SWIMMING")) {
                        users.add(new SwimmingDuck(userId, userType, username, email, password, speed, resistance));
                    } else if (duckType.equals("FLYING")) {
                        users.add(new FlyingDuck(userId, userType, username, email, password, speed, resistance));
                    } else if (duckType.equals("FLYING_AND_SWIMMING")) {
                        users.add(new FlyingSwimmingDuck(userId, userType, username, email, password, speed, resistance));
                    }
                }
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
