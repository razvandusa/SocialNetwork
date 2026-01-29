package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.domain.Event;
import org.example.domain.Person;
import org.example.domain.User;
import org.example.service.*;

import java.io.IOException;
import java.util.List;

public class ProfileWindow {

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label friends;

    @FXML
    public Label occupation;

    @FXML
    private Button eventButton;

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;
    private MessageService messageService;
    private EventService eventService;

    User currentUser;

    public void setUserService(UserService userService) { this.userService = userService; }

    public void setFriendshipService(FriendshipService friendshipService) { this.friendshipService = friendshipService; }

    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService) { this.friendshipRequestService = friendshipRequestService; }

    public void setMessageService(MessageService messageService) { this.messageService = messageService; }

    public void setEventService(EventService eventService) { this.eventService = eventService; }

    public void setUser(User user) {
        if (user instanceof Person) {
            fullNameLabel.setText(((Person) user).getName() + " " + ((Person) user).getSurname());
        }
        usernameLabel.setText("@" + user.getUsername());

        currentUser = user;
        int friendsCount = friendshipService.getNumberOfFriends(user.getId());
        friends.setText(String.valueOf(friendsCount));
        if (user instanceof Person) {
            occupation.setText(((Person) user).getOccupation());
        }

    }

    @FXML
    private void handleFriends() {
        openAddFriendWindow(currentUser);
        closeWindow((Stage) friends.getScene().getWindow());
    }

    @FXML
    private void handleMessages() throws IOException {
        openMessagesWindow(currentUser);
        closeWindow((Stage) friends.getScene().getWindow());
    }

    @FXML
    private void handleEvents() throws IOException {
        openEventWindow(currentUser);
        closeWindow((Stage) friends.getScene().getWindow());
    }

    private void openAddFriendWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddFriendWindow.fxml"));
            Scene scene = new Scene(loader.load(), 360, 830);
            scene.getStylesheets().add(getClass().getResource("/css/add-friend-window.css").toExternalForm());

            AddFriendWindow controller = loader.getController();
            controller.setServices(userService, friendshipService, friendshipRequestService ,messageService, eventService ,user);


            Stage stage = new Stage();
            stage.setTitle("Add Friend");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openMessagesWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MessagesWindow.fxml"));
            Scene scene = new Scene(loader.load(), 360, 730);
            scene.getStylesheets().add(getClass().getResource("/css/message-window.css").toExternalForm());

            MessagesWindow controller = loader.getController();
            controller.setUserService(userService);
            controller.setFriendshipRequestService(friendshipRequestService);
            controller.setFriendshipService(friendshipService);
            controller.setMessageService(messageService);
            controller.setEventService(eventService);
            controller.setCurrentUser(user);

            Stage stage = new Stage();
            stage.setTitle(user.getUsername());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openEventWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EventWindow.fxml"));
            Scene scene = new Scene(loader.load(), 360, 790);
            scene.getStylesheets().add(getClass().getResource("/css/event-window.css").toExternalForm());

            EventWindow controller = loader.getController();
            controller.setServices(userService, friendshipService, friendshipRequestService, messageService, eventService, currentUser);

            Stage stage = new Stage();
            stage.setTitle("Events");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow(Stage stage) { stage.close(); }
}
