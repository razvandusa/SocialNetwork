package org.example.domain;

import java.util.ArrayList;
import java.util.List;

public class RaceEvent extends Event {

    private final int maxParticipants;
    private final List<Swimmer> participants;

    RaceEvent(Long id, String eventName, int  maxParticipants) {
        super(id, eventName);
        this.maxParticipants = maxParticipants;
        this.participants = new ArrayList<>();
    }

    public void selectFromFlock(Flock flock) {
        for (Duck duck : flock.getDucks()) {
            if (duck instanceof Swimmer) {
                participants.add((Swimmer) duck);
                if (participants.size() == maxParticipants) {
                    break;
                }
            }
        }
    }

    @Override
    public void start() {
        notifySubscribers("Race Event " + getEventName() + " started with " + participants.size() + " participants!");
        for (Swimmer duck : participants) {
            duck.swim();
        }
    }
}
