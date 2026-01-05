package org.example.domain;

/**
 * Represents a generic user in the social network.
 * Each user has a unique identifier, a username, an email, and a password.
 */
public abstract class User extends Entity<Long> implements Observer {
    private String userType;
    private String username;
    private String email;
    private String password;

    /**
     * Constructs a new User with the given parameters.
     *
     * @param id the unique identifier inherited from Entity
     * @param userType the type of the user
     * @param username the username of the user
     * @param email the email of the user
     * @param password the password of the user
     */
    public User(Long id, String userType, String username, String email, String password) {
        super(id);
        this.userType = userType;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Long getId() { return super.getId();}
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUserType() { return userType; }

    public void setId(Long id) { super.setId(id); }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setUserType(String userType) { this.userType = userType; }

    @Override
    public String toString() {
        return "id=" + getId() +
                ", userType='" + userType + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'';
    }

    @Override
    public void onNotification(String message) {
        System.out.println(
                "User[id=" + getId() +
                        ", username='" + username + "'] received notification: " + message
        );
    }

}
