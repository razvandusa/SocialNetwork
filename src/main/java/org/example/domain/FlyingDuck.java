package org.example.domain;

public class FlyingDuck extends Duck implements Flyer {

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
    public FlyingDuck(Long id, String userType, String username, String email, String password, Double speed, Double resistance) {
        super(id, userType, username, email, password, speed, resistance);
    }

    @Override
    public void fly() {
        System.out.println("Duck " + super.getUsername() + " is flying");
    }

    @Override
    public String toString() {
        return "FlyingDuck{" +
                super.toString() +
                ", speed=" + super.getSpeed() +
                ", resistance=" + super.getResistance() +
                '}';
    }
}
