package org.example.domain;

public class FlyingSwimmingDuck extends Duck implements Flyer, Swimmer {

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
    public FlyingSwimmingDuck(Long id, String userType, String username, String email, String password, Double speed, Double resistance) {
        super(id, userType, username, email, password, "FLYING_AND_SWIMMING", speed, resistance);
    }

    @Override
    public void fly() {
        System.out.println("Duck " + super.getUsername() + " is flying");
    }

    @Override
    public void swim() {
        System.out.println("Duck " + super.getUsername() + " is swimming");
    }

    @Override
    public String toString() {
        return "FlyingSwimmingDuck{" +
                super.toString() +
                ", speed=" + super.getSpeed() +
                ", resistance=" + super.getResistance() +
                '}';
    }
}
