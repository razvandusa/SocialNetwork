package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Flock<T extends Duck> {
    private Long id;
    private String FlockName;
    List<T> members;

    public Flock(Long id, String FlockName) {
        this.id = id;
        this.FlockName = FlockName;
        members = new ArrayList<>();
    }

    Long getId() { return id; }
    String getFlockName() { return FlockName; }
    List<T> getMembers() { return members; }

    void setId(Long id) { this.id = id; }
    void setFlockName(String FlockName) { this.FlockName = FlockName; }
    void setMembers(List<T> members) { this.members = members; }

    public Double getAveragePerformance() {
        Double totalSpeed = 0.0;
        Double totalResistance = 0.0;

        for (T duck : members) {
            totalResistance += duck.getResistance();
            totalSpeed += duck.getSpeed();
        }

        return (totalSpeed + totalResistance) / (2.0 * members.size());
    }
}
