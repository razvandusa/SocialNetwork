package org.example.domain;

public class DuckResult {
    private final Duck duck;
    private final double laneDistance;
    private final double time;

    public DuckResult(Duck duck, double laneDistance, double time) {
        this.duck = duck;
        this.laneDistance = laneDistance;
        this.time = time;
    }

    public Duck getDuck() { return duck; }
    public double getLaneDistance() { return laneDistance; }
    public double getTime() { return time; }
}
