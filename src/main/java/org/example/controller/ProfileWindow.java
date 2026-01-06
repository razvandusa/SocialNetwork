package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.domain.Person;
import org.example.domain.User;
import org.example.service.FriendshipService;

import java.io.IOException;

public class ProfileWindow {

    User currentUser;

    @FXML
    private Label fullNameLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label friends;

    private FriendshipService friendshipService;

    public void setFriendshipService(FriendshipService friendshipService) { this.friendshipService = friendshipService; }

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
    private void handleFriends() {}

    @FXML
    private void handleMessages() throws IOException {
        openMessagesWindow(currentUser);
        closeWindow((Stage) friends.getScene().getWindow());
    }

    private void openMessagesWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MessagesWindow.fxml"));
            Scene scene = new Scene(loader.load(), 360, 730);

            MessagesWindow controller = loader.getController();
            controller.setFriendshipService(friendshipService);
            controller.setCurrentUser(user);

            Stage stage = new Stage();
            stage.setTitle(user.getUsername());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow(Stage stage) { stage.close(); }
}
