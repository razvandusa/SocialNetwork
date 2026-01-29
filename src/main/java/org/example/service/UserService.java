package org.example.service;

import org.example.domain.*;
import org.example.exceptions.EntityNotFoundException;
import org.example.exceptions.validationExceptions.duckExceptions.DuckTypeValidationException;
import org.example.exceptions.validationExceptions.duckExceptions.ResistanceValidationException;
import org.example.exceptions.validationExceptions.duckExceptions.SpeedValidationException;
import org.example.exceptions.validationExceptions.personExceptions.BirthdateValidationException;
import org.example.exceptions.validationExceptions.userExceptions.IdValidationException;
import org.example.repository.UserRepository;
import org.example.validation.ValidatorContext;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

/**
 * Service layer for managing {@link User}, {@link Duck}, and {@link Person} entities.
 */
public class UserService implements Service<Long, User> {

    UserRepository userRepository;
    ValidatorContext<User> validatorUser;

    /**
     * Constructs a UserService with the given repository and validator.
     *
     * @param userRepository repository used to store users
     * @param validatorUser validator used to validate user entities
     */
    public UserService(UserRepository userRepository, ValidatorContext<User> validatorUser) {
        this.userRepository = userRepository;
        this.validatorUser = validatorUser;
    }

    /**
     * Adds a new user of type Duck or Person with the given fields.
     *
     * @param fields a variable number of strings representing the entity data
     * @throws DuckTypeValidationException if the duck type is invalid
     * @throws SpeedValidationException if the speed is not a valid number
     * @throws ResistanceValidationException if the resistance is not a valid number
     * @throws BirthdateValidationException if the birthdate format is invalid
     */
    public void add(String ... fields) {
        String userType = fields[0];
        String username = fields[1];
        String email = fields[2];
        String password = fields[3];
        switch (userType) {
            case "Duck":
                String duckType = fields[4];
                String speed = fields[5];
                String resistance = fields[6];
                final Set<String> VALID_DUCK_TYPES = Set.of("FLYING", "SWIMMING", "FLYING_AND_SWIMMING");
                if (!VALID_DUCK_TYPES.contains(duckType)) {
                    throw new DuckTypeValidationException(
                            "Invalid duck type: " + duckType + ". Valid types are: " + VALID_DUCK_TYPES
                    );
                }
                double doubleSpeed;
                try {
                    doubleSpeed = Double.parseDouble(speed);
                } catch (NumberFormatException e) {
                    throw new SpeedValidationException("Speed must be a valid number");
                }
                double doubleResistance;
                try {
                    doubleResistance = Double.parseDouble(resistance);
                } catch (NumberFormatException e) {
                    throw new ResistanceValidationException("Resistance must be a valid number");
                }
                Duck duck = switch (duckType) {
                    case "SWIMMING" ->
                            new SwimmingDuck(generateID(), userType, username, email, password, doubleSpeed, doubleResistance);
                    case "FLYING" ->
                            new FlyingDuck(generateID(), userType, username, email, password, doubleSpeed, doubleResistance);
                    case "FLYING_AND_SWIMMING" ->
                            new FlyingSwimmingDuck(generateID(), userType, username, email, password, doubleSpeed, doubleResistance);
                    default -> throw new DuckTypeValidationException("Invalid duck type: " + duckType);
                };
                validatorUser.validate(duck);
                duck.setPassword(hashPassword(password));
                userRepository.add(duck);
                break;
            case "Person":
                LocalDate localDateBirthdate;
                String surname = fields[4];
                String name = fields[5];
                String birthdate = fields[6];
                String occupation = fields[7];
                try {
                    localDateBirthdate = LocalDate.parse(birthdate);
                }  catch (DateTimeParseException e) {
                    throw new BirthdateValidationException("Birthdate format must be yyyy-MM-dd");
                }
                Person person = new Person(generateID(), userType, username, email, password, surname, name, localDateBirthdate, occupation);
                validatorUser.validate(person);
                person.setPassword(hashPassword(password));
                userRepository.add(person);
                break;
        }
    }

    /**
     * Removes a user by id
     *
     * @param id the identifier of the entity to be removed
     */
    @Override
    public void remove(String id) {
        long longId;
        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        User user = userRepository.findById(longId);
        if (user == null) {
            throw new EntityNotFoundException("The user with id " + id + " was not found");
        }
        userRepository.remove(user);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Generates a unique id for a new user.
     *
     * @return a new unique user id
     */
    @Override
    public Long generateID() {
        long maxNumber = 0;
        for (User user : findAll()) {
            if (user.getId() > maxNumber) {
                maxNumber = user.getId();
            }
        }
        return maxNumber + 1;
    }

    /**
     * Finds a user by id
     * @param id the id of the user to find
     * @return the User entity with the given id, or null if not found
     */
    public User findById(String id) {
        long longId;
        try {
            longId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new IdValidationException("The id must be a number");
        }
        return userRepository.findById(longId);
    }

//    public List<User> findDuckByType(String duckType) {
//        List<User> filtered = new ArrayList<>();
//        List<User> allUsers = (List<User>) userRepository.findAll();
//
//        switch (duckType) {
//            case "SWIMMING":
//                for (User user : allUsers) {
//                    if (user instanceof Swimmer) {
//                        filtered.add(user);
//                    }
//                }
//                break;
//            case "FLYING":
//                for (User user : allUsers) {
//                    if (user instanceof Flyer) {
//                        filtered.add(user);
//                    }
//                }
//                break;
//            case "FLYING_AND_SWIMMING":
//                for (User user : allUsers) {
//                    if (user instanceof Flyer && user instanceof Swimmer) {
//                        filtered.add(user);
//                    }
//                }
//                break;
//            default:
//                throw new IllegalArgumentException("Invalid duck type: " + duckType);
//        }
//
//        return filtered;
//    }

    public List<User> getUsersPage(Long pageNumber, Long pageSize) {
        return userRepository.getUsersPage(pageNumber, pageSize);
    }

    public Long getUsersCount() {
        return userRepository.getUsersCount();
    }

    public List<User> getDucksPageByType(String type, Long page, Long pageSize) {
        return userRepository.findDucksPageByType(type, page, pageSize);
    }

    public long getDucksCountByType(String type) {
        return userRepository.getDucksCountByType(type);
    }

    public User login(String username, String password) {
        for (User user : userRepository.findAll()) {

            if (user == null) {
                continue;
            }

            if (user.getUsername().equals(username)
                    && user.getPassword().equals(hashPassword(password))) {
                return user;
            }
        }
        return null;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
