package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class Flock extends Entity<Long> {
    private String flockName;
    List<Duck> ducks;
    private final String flockType;

    public Flock(Long id, String FlockName, String flockType) {
        super(id);
        this.flockName = FlockName;
        this.flockType = flockType;
        ducks = new ArrayList<>();
    }

    public String getFlockName() { return flockName; }
    public String getFlockType() { return flockType; }
    public List<Duck> getDucks() { return ducks; }

    public void setFlockName(String flockName) { this.flockName = flockName; }
    public void setDucks(List<Duck> ducks) { this.ducks = ducks; }

    public Double getAveragePerformance() {
        if (ducks.isEmpty()) return 0.0;

        Double totalSpeed = 0.0;
        Double totalResistance = 0.0;

        for (Duck duck : ducks) {
            totalResistance += duck.getResistance();
            totalSpeed += duck.getSpeed();
        }

        return (totalSpeed + totalResistance) / (2.0 * ducks.size());
    }

    public void addDuck(Duck duck) {
        ducks.add(duck);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Flock{");
        sb.append("id=").append(super.getId());
        sb.append(", flockName='").append(flockName).append('\'');
        sb.append(", flockType='").append(flockType).append('\'');
        sb.append(", ducks=[");
        for (int i = 0; i < ducks.size(); i++) {
            sb.append(ducks.get(i).getUsername());
            if (i < ducks.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("]}");
        return sb.toString();
    }
}
