package org.example.repository;

import org.example.domain.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDataBaseRepository implements MessageRepository {
    private final String url;
    private final String username;
    private final String password;

    public MessageDataBaseRepository(String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public void add(Message message) {
        String sql = "INSERT INTO messages (id, text, timestamp, sender_id, recipient_id, reply_to) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, message.getId());
            ps.setString(2, message.getText());
            ps.setTimestamp(3, Timestamp.valueOf(message.getTimestamp()));
            ps.setLong(4, message.getSenderId());
            ps.setLong(5, message.getReceiverId());
            if (message.getReplyTo() != null) {
                ps.setLong(6, message.getReplyTo().getId());
            } else {
                ps.setNull(6, Types.BIGINT);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message findById(Long id) {
        String sql = "SELECT * FROM messages WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Long idMessage = rs.getLong("id");
                String text = rs.getString("text");
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                Long senderId = rs.getLong("sender_id");
                Long recipientId = rs.getLong("recipient_id");
                Message message = new Message(idMessage, text, timestamp, senderId, recipientId);
                Long replyToId = rs.getLong("reply_to");
                if (!rs.wasNull()) {
                    message.setReplyTo(findById(replyToId));
                }
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Message> getMessagesBetweenUsers(Long userId1, Long userId2) {
        String sql = "SELECT * FROM messages WHERE (sender_id = ? AND recipient_id = ?) OR (sender_id = ? AND recipient_id = ?) ORDER BY timestamp";
        List<Message> messages = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId1);
            ps.setLong(2, userId2);
            ps.setLong(3, userId2);
            ps.setLong(4, userId1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Long idMessage = rs.getLong("id");
                String text = rs.getString("text");
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                Long senderId = rs.getLong("sender_id");
                Long recipientId = rs.getLong("recipient_id");
                Message message = new Message(idMessage, text, timestamp, senderId, recipientId);
                Long replyToId = rs.getLong("reply_to");
                if (!rs.wasNull()) {
                    message.setReplyTo(findById(replyToId));
                }
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public void remove(Message entity) {}

    @Override
    public Iterable<Message> findAll() {
        try (Connection conn = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM messages")) {
            ResultSet rs = ps.executeQuery();
            List<Message> messages = new ArrayList<>();
            while (rs.next()) {
                Long messageId = rs.getLong("id");
                Message message = findById(messageId);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
