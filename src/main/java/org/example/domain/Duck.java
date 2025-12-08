package org.example.domain;

/**
 * Represents a Duck user in the social network.
 * Each duck has a type, speed, and resistance.
 */
public abstract class Duck extends User {
    private Double speed;
    private Double resistance;

    /**
     * Constructs a new Duck with the given parameters.
     *
     * @param id the unique identifier inherited from User
     * @param username the username of the Duck
     * @param email the email of the Duck
     * @param password the password of the Duck
     * @param speed the speed of the duck
     * @param resistance the resistance of the duck
     */
    public Duck(Long id, String userType, String username, String email, String password,Double speed, Double resistance) {
        super(id, userType, username, email, password);
        this.speed = speed;
        this.resistance = resistance;
    }

    public Double getSpeed() { return speed; }
    public Double getResistance() { return resistance; }

    public void setSpeed(Double speed) { this.speed = speed; }
    public void setResistance(Double resistance) { this.resistance = resistance; }
}
