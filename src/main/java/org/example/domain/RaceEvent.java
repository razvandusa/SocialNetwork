package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class RaceEvent extends Event {

    private List<Swimmer> participants = new ArrayList<>(); // N ducks
    private List<Double> lanes = new ArrayList<>(); // M distances

    public RaceEvent(Long id, String eventName) {
        super(id, eventName);
    }

    public List<Swimmer> getParticipants() { return participants; }
    public List<Double> getLanes() { return lanes; }

    public void setParticipants(List<Swimmer> participants) { this.participants = participants; };
    public void setLanes(List<Double> lanes) { this.lanes = lanes; }

    public void addParticipant(Swimmer participant) {
        participants.add(participant);
    }

    public void addLane(Double lane) {
        lanes.add(lane);
    }
}
