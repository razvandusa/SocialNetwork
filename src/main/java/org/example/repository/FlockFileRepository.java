package org.example.repository;

import org.example.domain.*;

import java.util.List;

public class FlockFileRepository extends AbstractFileRepository<Long, Flock> {

    Repository<Long, User> userRepository;

    /**
     * Constructs a new AbstractFileRepository with the specified file name.
     *
     * @param fileName the file name where entities will be loaded from and saved to
     */
    public FlockFileRepository(String fileName, Repository<Long, User> userRepository) {
        super(fileName);
        this.userRepository = userRepository;
    }

    @Override
    public Flock extractEntity(List<String> data) {
        Long id = Long.parseLong(data.get(0));
        String flockName = data.get(1);
        String flockType = data.get(2);
        String[] duckIDs = new String[0];
        Flock flock = new Flock(id, flockName, flockType);

        if (data.size() > 3 && !data.get(3).isEmpty()) {
            duckIDs = data.get(3).split(",");
        }

        for (String duckID : duckIDs) {
            Duck duck = (Duck) userRepository.findById(Long.parseLong(duckID));
            flock.addDuck(duck);
        }

        return flock;
    }

    @Override
    protected String createEntityAsString(Flock entity) {
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder(entity.getId() + ";" + entity.getFlockName() + ";" + entity.getFlockType() + ";");
        List<Duck> ducks = entity.getDucks();
        for (int i = 0; i < ducks.size(); i++) {
            stringBuilder.append(ducks.get(i).getId());
            if (i < ducks.size() - 1) {
                stringBuilder.append(",");
            }
        }

        return stringBuilder.toString();
    }
}
