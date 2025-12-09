package org.example;

import org.example.domain.Duck;
import org.example.domain.Flock;
import org.example.domain.Friendship;
import org.example.domain.User;
import org.example.repository.FlockFileRepository;
import org.example.repository.FriendshipFileRepository;
import org.example.repository.Repository;
import org.example.repository.UserFileRepository;
import org.example.service.FlockService;
import org.example.service.FriendshipService;
import org.example.service.UserService;
import org.example.ui.Console;
import org.example.validation.FlockValidationStrategy;
import org.example.validation.FriendshipValidationStrategy;
import org.example.validation.UserValidationStrategy;
import org.example.validation.ValidatorContext;

public class Main {
    static void main() {
        Repository<Long, User> userRepository = new UserFileRepository("/Users/razvandusa/IdeaProjects/SocialNetwork/src/main/java/org/example/ui/users.txt");
        Repository<Long, Friendship> friendshipRepository = new FriendshipFileRepository("/Users/razvandusa/IdeaProjects/SocialNetwork/src/main/java/org/example/ui/friendships.txt");
        Repository<Long, Flock> flockRepository = new FlockFileRepository("/Users/razvandusa/IdeaProjects/SocialNetwork/src/main/java/org/example/ui/flock.txt");

        ValidatorContext<User> userValidator = new ValidatorContext<>(new UserValidationStrategy());
        ValidatorContext<Friendship> friendshipValidator = new ValidatorContext<>(new FriendshipValidationStrategy());
        ValidatorContext<Flock> flockValidator = new ValidatorContext<>(new FlockValidationStrategy());

        UserService userService = new UserService(userRepository, userValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository, friendshipValidator);
        FlockService flockService = new FlockService(flockRepository, userRepository, flockValidator);

        Console console = new Console(userService, friendshipService, flockService);

        console.run();
    }
}