package org.example.domain;

public class Lane extends Entity<Long> {

    Double distance;

    public Lane(Long id, Double distance) {
        super(id);
        this.distance = distance;
    }

    public void setId(Long id) { super.setId(id); }
    public Long getId() { return super.getId(); }

    public Double getDistance() { return distance; }
    public void setDistance(Double distance) { this.distance = distance; }

    @Override
    public String toString() {
        return "Lane{" +
                "id=" + getId() +
                ", distance=" + distance +
                '}';
    }
}
