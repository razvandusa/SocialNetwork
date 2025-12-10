package org.example.repository;

import org.example.domain.*;

import java.util.ArrayList;
import java.util.List;

public class EventFileRepository extends AbstractFileRepository<Long, Event> {

    Repository<Long, User> userRepository;

    /**
     * Constructs a new AbstractFileRepository with the specified file name.
     *
     * @param fileName the file name where entities will be loaded from and saved to
     */
    EventFileRepository(String fileName, Repository<Long, User> userRepository) {
        super(fileName);
        this.userRepository = userRepository;
    }

    @Override
    public Event extractEntity(List<String> data) {
        Long id = Long.parseLong(data.get(0));
        String raceType = data.get(1);
        String eventName = data.get(2);

        switch (raceType) {
            case "RaceEvent":
                String[] subscribersIDs = new String[0];
                String[] participantsIDs = new String[0];
                String[] lanesDistances = new String[0];
                RaceEvent raceEvent = new RaceEvent(id, eventName);

                if (data.size() > 3 && !data.get(3).isEmpty()) {
                    subscribersIDs = data.get(3).split(",");
                }

                for (String userID : subscribersIDs) {
                    User user = userRepository.findById(Long.parseLong(userID));
                    raceEvent.subscribe(user);
                }

                if (data.size() > 4 && !data.get(4).isEmpty()) {
                    participantsIDs = data.get(4).split(",");
                }

                for (String participantID : participantsIDs) {
                    User user = userRepository.findById(Long.parseLong(participantID));
                    if (user instanceof Duck && user instanceof Swimmer) {
                        raceEvent.addParticipant((Swimmer) user);
                    }
                }

                if (data.size() > 5 && !data.get(5).isEmpty()) {
                    lanesDistances = data.get(5).split(",");
                }

                for (String laneDistance : lanesDistances) {
                    Double distance = Double.parseDouble(laneDistance);
                    raceEvent.addLane(distance);
                }
                return raceEvent;
            default:
                throw new IllegalArgumentException("Invalid event type: " + raceType);
        }
    }

    @Override
    protected String createEntityAsString(Event entity) {
        StringBuilder sb = new StringBuilder();
        if (entity instanceof RaceEvent) {
            sb.append(entity.getId()).append(";");
            sb.append(entity.getClass().getSimpleName()).append(";");
            sb.append(entity.getEventName()).append(";");

            List<Observer> subs = entity.getSubscribers();
            for (int i = 0; i < subs.size(); i++) {
                sb.append(((User) subs.get(i)).getId());
                if (i < subs.size() - 1) sb.append(",");
            }
            sb.append(";");

            List<Swimmer> participants = ((RaceEvent) entity).getParticipants();
            for (int i = 0; i < participants.size(); i++) {
                sb.append(((User) participants.get(i)).getId());
                if (i < participants.size() - 1) sb.append(",");
            }
            sb.append(";");

            List<Double> lanes = ((RaceEvent) entity).getLanes();
            for (int i = 0; i < lanes.size(); i++) {
                sb.append(lanes.get(i));
                if (i < lanes.size() - 1) sb.append(",");
            }

            return sb.toString();

        } else {
            throw new IllegalArgumentException("Invalid event type: " + entity.getClass().getSimpleName());
        }
    }
}
