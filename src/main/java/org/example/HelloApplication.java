package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.LoginWindow;
import org.example.domain.Event;
import org.example.domain.Flock;
import org.example.domain.Friendship;
import org.example.domain.User;
import org.example.repository.*;
import org.example.service.EventService;
import org.example.service.FlockService;
import org.example.service.FriendshipService;
import org.example.service.UserService;
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

        Repository<Long, User> userRepository = new UserDataBaseRepository(url, user, password);
        Repository<Long, Friendship> friendshipRepository = new FriendshipDataBaseRepository(url, user, password);
        Repository<Long, Flock> flockRepository = new FlockDataBaseRepository(url, user, password);
        Repository<Long, Event> eventRepository = new EventDataBaseRepository(url, user, password);

        ValidatorContext<User> userValidator = new ValidatorContext<>(new UserValidationStrategy());
        ValidatorContext<Friendship> friendshipValidator = new ValidatorContext<>(new FriendshipValidationStrategy());
        ValidatorContext<Flock> flockValidator = new ValidatorContext<>(new FlockValidationStrategy());
        ValidatorContext<Event> eventValidator = new ValidatorContext<>(new EventValidationStrategy());

        UserService userService = new UserService(userRepository, userValidator);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository, friendshipValidator);
        FlockService flockService = new FlockService(flockRepository, userRepository, flockValidator);
        EventService eventService = new EventService(eventRepository, userRepository, eventValidator);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginWindow.fxml"));
        Scene scene = new Scene(loader.load(), 420, 640);
        LoginWindow controller = loader.getController();
        controller.setUserService(userService);
        controller.setFriendshipService(friendshipService);

        stage.setTitle("LuckyLink - Login page");
        scene.getStylesheets().add(getClass().getResource("/css/login-window.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
