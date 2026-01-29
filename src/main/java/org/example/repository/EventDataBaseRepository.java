package org.example.repository;

import org.example.domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventDataBaseRepository implements EventRepository {
    private final String url;
    private final String username;
    private final String password;

    public EventDataBaseRepository(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Event entity) {
        String sql = "INSERT INTO events(id, eventType, eventName) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, entity.getId());
            ps.setString(2, entity.getType());
            ps.setString(3, entity.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(Event entity) {
        String sql = "DELETE FROM events WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, entity.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Event findById(Long id) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sqlEvent = "SELECT * FROM events WHERE id = ?";
            Event event;
            String eventType;
            try (PreparedStatement ps = conn.prepareStatement(sqlEvent)) {
                ps.setLong(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    Long eventId = rs.getLong("id");
                    eventType = rs.getString("eventtype");
                    String eventName = rs.getString("eventname");
                    boolean status = rs.getBoolean("status");
                    if (eventType.equals("RaceEvent")) {
                        event = new RaceEvent(eventId, eventType, eventName);
                        event.setStatus(status);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }

            String sqlSubscribers = "SELECT s.* FROM Users s " +
                                    "JOIN Event_Subscriber es ON es.subscriberId = s.id " +
                                    "WHERE es.eventId = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlSubscribers)) {
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
                        event.addObserver(new Person(userId, userType, username, email, password, surname, name, birthdate, occupation));
                    } else if (userType.equals("Duck")) {
                        String duckType = rs.getString("duckType");
                        Double speed = rs.getDouble("speed");
                        Double resistance = rs.getDouble("resistance");
                        if (duckType.equals("SWIMMING")) {
                            event.addObserver(new SwimmingDuck(userId, userType, username, email, password, speed, resistance));
                        } else if (duckType.equals("FLYING")) {
                            event.addObserver(new FlyingDuck(userId, userType, username, email, password, speed, resistance));
                        } else if (duckType.equals("FLYING_AND_SWIMMING")) {
                            event.addObserver(new FlyingSwimmingDuck(userId, userType, username, email, password, speed, resistance));
                        }
                    }
                }
            }

            if (eventType.equals("RaceEvent")) {
                String sqlParticipants = "SELECT p.* FROM Users p " +
                                         "JOIN RaceEvent_Participant ep ON ep.participantId = p.id " +
                                         "WHERE ep.eventId = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlParticipants)) {
                    ps.setLong(1, id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Long userId = rs.getLong("id");
                        String userType = rs.getString("usertype");
                        String username = rs.getString("username");
                        String email = rs.getString("email");
                        String password = rs.getString("password");
                        if (userType.equals("Person")) {
                            throw new IllegalArgumentException("Participant must be a Duck, not a Person");
                        } else if (userType.equals("Duck")) {
                            String duckType = rs.getString("duckType");
                            Double speed = rs.getDouble("speed");
                            Double resistance = rs.getDouble("resistance");
                            if (duckType.equals("SWIMMING")) {
                                ((RaceEvent)event).addParticipant(new SwimmingDuck(userId, userType, username, email, password, speed, resistance));
                            } else if (duckType.equals("FLYING")) {
                                throw new IllegalArgumentException("Participant must be a SwimmingDuck, not a FlyingDuck");
                            } else if (duckType.equals("FLYING_AND_SWIMMING")) {
                                ((RaceEvent)event).addParticipant(new FlyingSwimmingDuck(userId, userType, username, email, password, speed, resistance));
                            }
                        }
                    }
                }

                String sqlLanes = "SELECT l.laneValue FROM Lanes l " +
                                  "JOIN Events e ON e.id = l.eventId " +
                                  "WHERE e.id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlLanes)) {
                    ps.setLong(1, id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        Double distance = rs.getDouble("laneValue");
                        ((RaceEvent)event).addLane(distance);
                    }
                }
            }

            return event;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Event> findAll() {
        String sql = "SELECT * FROM Events";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            List<Event> events = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long eventId = rs.getLong("id");
                Event event = findById(eventId);
                events.add(event);
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addSpectatorToEvent(Long eventId, Long userId) {
        String sql = "INSERT INTO Event_Subscriber(eventId, subscriberId) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeSpectatorFromEvent(Long eventId, Long userId) {
        String sql = "DELETE FROM Event_Subscriber WHERE eventId = ? AND subscriberId = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addParticipantToEvent(Long eventId, Long duckId) {
        String sql = "INSERT INTO RaceEvent_Participant(eventId, participantId) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            ps.setLong(2, duckId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeParticipantFromEvent(Long eventId, Long duckId) {
        String sql = "DELETE FROM Event_Subscriber WHERE eventId = ? AND participantId = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            ps.setLong(2, duckId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addLaneToEvent(Long eventId, Double laneValue) {
        String sql = "INSERT INTO Lanes(eventId, laneValue) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            ps.setDouble(2, laneValue);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeLaneFromEvent(Long eventId, Long indexValue) {
        String sql = "DELETE FROM Lanes WHERE eventId = ? AND id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, eventId);
            ps.setLong(2, indexValue);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Lane> getLanes(Long eventId) {
        List<Lane> lanes = new ArrayList<>();
        String sql = "SELECT * FROM Lanes WHERE eventId = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
             ps.setLong(1, eventId);
             ResultSet rs = ps.executeQuery();
             while (rs.next()) {
                 Long laneId = rs.getLong("id");
                 Double laneValue = rs.getDouble("laneValue");
                 Lane lane = new Lane(laneId, laneValue);
                 lanes.add(lane);
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lanes;
    }

    @Override
    public void updateStatusEvent(Long eventId, boolean status) {
        String sql = "UPDATE events SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setLong(2, eventId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
