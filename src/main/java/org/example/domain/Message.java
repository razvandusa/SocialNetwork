package org.example.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long> {
    private String text;
    private LocalDateTime timestamp;
    private Long senderId;
    private Long receiverId;
    private Message replyTo;

    public Message(Long id, String text, LocalDateTime timestamp, Long senderId, Long receiverId) {
        super(id);
        this.text = text;
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public Long getId() { return super.getId(); }

    public void setId(Long id) { super.setId(id); }

    public String getText() { return text; }

    public void setText(String text) { this.text = text; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public Long getSenderId() { return senderId; }

    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }

    public Message getReplyTo() { return replyTo; }

    public void setReplyTo(Message replyTo) { this.replyTo = replyTo; }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + getId() +
                ", text='" + text + '\'' +
                ", timestamp=" + timestamp +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}
