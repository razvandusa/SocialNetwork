package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.LoginWindow;
import org.example.domain.*;
import org.example.repository.*;
import org.example.service.*;
import org.example.validation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/SocialNetwork";
        String user = "postgres";
        String password = "1234";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the PostgresSQL server successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        UserRepository userRepository = new UserDataBaseRepository(url, user, password);
        FriendshipRepository friendshipRepository = new FriendshipDataBaseRepository(url, user, password);
        FriendshipRequestRepository friendshipRequestRepository = new FriendshipRequestDataBaseRepository(url, user, password);
        FlockRepository flockRepository = new FlockDataBaseRepository(url, user, password);
        EventRepository eventRepository = new EventDataBaseRepository(url, user, password);
        MessageRepository messageRepository = new MessageDataBaseRepository(url, user, password);

        ValidatorContext<User> userValidator = new ValidatorContext<>(new UserValidationStrategy());
        ValidatorContext<Friendship> friendshipValidator = new ValidatorContext<>(new FriendshipValidationStrategy());
        ValidatorContext<Flock> flockValidator = new ValidatorContext<>(new FlockValidationStrategy());
        ValidatorContext<Event> eventValidator = new ValidatorContext<>(new EventValidationStrategy());

        UserService userService = new UserService(userRepository, userValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository, friendshipValidator);
        FriendshipRequestService friendshipRequestService = new FriendshipRequestService(friendshipRequestRepository, friendshipRepository, userRepository);
        FlockService flockService = new FlockService(flockRepository, userRepository, flockValidator);
        EventService eventService = new EventService(eventRepository, userRepository, eventValidator);
        MessageService messageService = new MessageService(messageRepository);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
        Scene scene = new Scene(loader.load(), 420, 640);
        LoginWindow controller = loader.getController();
        controller.setUserService(userService);
        controller.setFriendshipService(friendshipService);
        controller.setFriendshipRequestService(friendshipRequestService);
        controller.setMessageService(messageService);
        controller.setEventService(eventService);

        stage.setTitle("LuckyLink - Login page");
        scene.getStylesheets().add(getClass().getResource("/css/login-window.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
