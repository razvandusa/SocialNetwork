package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.domain.User;
import org.example.service.*;

import java.io.IOException;

public class LoginWindow {

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;
    private MessageService messageService;
    private EventService eventService;

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void initialize() {
        loginButton.setOnAction(e -> {
            try {
                handleLogin();
            } catch (IOException ex) {
                showInformationAlert("Error", "The page couldn't be loaded.", ex.getMessage());
            }
        });
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFriendshipService(FriendshipService friendshipService) { this.friendshipService = friendshipService; }

    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService) { this.friendshipRequestService = friendshipRequestService; }

    public void setMessageService(MessageService messageService) { this.messageService = messageService; }

    public void setEventService(EventService eventService) { this.eventService = eventService; }

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            if (username.isEmpty()) {
                showInformationAlert("Login Failed", "Username can't be empty!", "");
            } else {
                showInformationAlert("Login Failed", "Password can't be empty!", "");
            }
            return;
        }

        User user = userService.login(username, password);

        if (user != null) {
            if (user.getUsername().equals("admin")) {
                opneAdminWindow();
//                closeWindow((Stage) loginButton.getScene().getWindow());
            } else {
                openUserWindow(user);
//                closeWindow((Stage) loginButton.getScene().getWindow());
            }
        } else {
            showInformationAlert("Login Failed", "Invalid username or password.", "");
        }
    }

    private void showInformationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void opneAdminWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminWindow.fxml"));
        Scene scene = new Scene(loader.load(), 1360, 740);

        AdminWindow controller = loader.getController();
        controller.setUserService(userService);
        controller.setFriendshipService(friendshipService);
        controller.setEventService(eventService);
        scene.getStylesheets().add(getClass().getResource("/css/admin-window.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("Admin Panel");
        stage.setScene(scene);
        stage.show();
    }

    private void openUserWindow(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProfileWindow.fxml"));
        Scene scene = new Scene(loader.load(), 360, 640);

        ProfileWindow controller = loader.getController();
        controller.setUserService(userService);
        controller.setFriendshipService(friendshipService);
        controller.setMessageService(messageService);
        controller.setFriendshipRequestService(friendshipRequestService);
        controller.setEventService(eventService);
        controller.setUser(user);
        scene.getStylesheets().add(getClass().getResource("/css/profile-window.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("User Profile");
        stage.setScene(scene);
        stage.show();
    }

    private void closeWindow(Stage stage) { stage.close(); }
}
