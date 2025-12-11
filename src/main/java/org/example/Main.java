package org.example;

import org.example.domain.*;
import org.example.repository.*;
import org.example.service.EventService;
import org.example.service.FlockService;
import org.example.service.FriendshipService;
import org.example.service.UserService;
import org.example.ui.Console;
import org.example.validation.*;

public class Main {
    static void main() {
        Repository<Long, User> userRepository = new UserFileRepository("/Users/razvandusa/IdeaProjects/SocialNetwork/src/main/resources/users.txt");
        Repository<Long, Friendship> friendshipRepository = new FriendshipFileRepository("/Users/razvandusa/IdeaProjects/SocialNetwork/src/main/resources/friendships.txt");
        Repository<Long, Flock> flockRepository = new FlockFileRepository("/Users/razvandusa/IdeaProjects/SocialNetwork/src/main/resources/flocks.txt", userRepository);
        Repository<Long, Event> eventRepository = new EventFileRepository("/Users/razvandusa/IdeaProjects/SocialNetwork/src/main/resources/events.txt", userRepository);

        ValidatorContext<User> userValidator = new ValidatorContext<>(new UserValidationStrategy());
        ValidatorContext<Friendship> friendshipValidator = new ValidatorContext<>(new FriendshipValidationStrategy());
        ValidatorContext<Flock> flockValidator = new ValidatorContext<>(new FlockValidationStrategy());
        ValidatorContext<Event> eventValidator = new ValidatorContext<>(new EventValidationStrategy());

        UserService userService = new UserService(userRepository, userValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository, friendshipValidator);
        FlockService flockService = new FlockService(flockRepository, userRepository, flockValidator);
        EventService eventService = new EventService(eventRepository, userRepository, eventValidator);

        Console console = Console.getInstance(userService, friendshipService, flockService, eventService);

        console.run();
    }
}