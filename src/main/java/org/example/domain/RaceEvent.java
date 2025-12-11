package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class RaceEvent extends Event {

    private List<Swimmer> participants = new ArrayList<>(); // N ducks
    private List<Double> lanes = new ArrayList<>(); // M distances

    public RaceEvent(Long id, String eventType, String eventName) {
        super(id, eventType, eventName);
    }

    public List<Swimmer> getParticipants() { return participants; }
    public List<Double> getLanes() { return lanes; }

    public void setParticipants(List<Swimmer> participants) { this.participants = participants; };
    public void setLanes(List<Double> lanes) { this.lanes = lanes; }

    public void addParticipant(Swimmer participant) {
        participants.add(participant);
    }

    public void removeParticipant(Swimmer participant) {
        participants.remove(participant);
    }

    public void addLane(Double lane) {
        lanes.add(lane);
    }

    public void removeLane(Double lane) {
        lanes.remove(lane);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(", participants=[");

        for (int i = 0; i < participants.size(); i++) {
            sb.append(((User) participants.get(i)).getId());
            if (i < participants.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("], lanes=").append(lanes);
        sb.append('}');
        return sb.toString();
    }
}
