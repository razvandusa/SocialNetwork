package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.domain.Person;
import org.example.domain.User;
import org.example.service.FriendshipRequestService;
import org.example.service.FriendshipService;
import org.example.service.MessageService;
import org.example.service.UserService;

import java.io.IOException;

public class ProfileWindow {

    User currentUser;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label friends;

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;
    private MessageService messageService;

    public void setUserService(UserService userService) { this.userService = userService; }

    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService) { this.friendshipRequestService = friendshipRequestService; }

    public void setFriendshipService(FriendshipService friendshipService) { this.friendshipService = friendshipService; }

    public void setMessageService(MessageService messageService) { this.messageService = messageService; }

    public void setUser(User user) {
        if (user instanceof Person) {
            fullNameLabel.setText(((Person) user).getName() + " " + ((Person) user).getSurname());
        }
        usernameLabel.setText("@" + user.getUsername());

        currentUser = user;
        int friendsCount = friendshipService.getNumberOfFriends(user.getId());
        friends.setText(String.valueOf(friendsCount));

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
            controller.setCurrentUser(user);

            Stage stage = new Stage();
            stage.setTitle(user.getUsername());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openAddFriendWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddFriendWindow.fxml"));
            Scene scene = new Scene(loader.load(), 360, 830);
            scene.getStylesheets().add(getClass().getResource("/css/add-friend-window.css").toExternalForm());

            AddFriendWindow controller = loader.getController();
            controller.setServices(userService, friendshipService, friendshipRequestService ,messageService, user);


            Stage stage = new Stage();
            stage.setTitle("Add Friend");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow(Stage stage) { stage.close(); }
}
