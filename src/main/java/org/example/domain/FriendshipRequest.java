package org.example.domain;

public class FriendshipRequest extends Entity<Long> {
    private User sender;
    private User recipient;
    private Status status;

    public FriendshipRequest(Long id, User sender, User recipient, Status status) {
        super(id);
        this.sender = sender;
        this.recipient = recipient;
        this.status = status;
    }

    public User getSender() { return sender; }

    public void setSender(User sender) { this.sender = sender; }

    public Status getStatus() { return status; }

    public void setStatus(Status status) { this.status = status; }

    public User getRecipient() { return recipient; }

    public void setRecipient(User recipient) { this.recipient = recipient; }

    @Override
    public String toString() {
        return "FriendshipRequest{" +
                "sender=" + sender +
                ", recipient=" + recipient +
                ", status=" + status +
                '}';
    }
}
