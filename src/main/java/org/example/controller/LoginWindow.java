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
import org.example.service.FriendshipService;
import org.example.service.UserService;

import java.io.IOException;

public class LoginWindow {

    private UserService userService;
    private FriendshipService friendshipService;
    Stage stage = new Stage();

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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("The page couldn't be loaded.");
                alert.showAndWait();
            }
        });
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            if (username.isEmpty()) {
                alert.setContentText("Username can't be empty!");
            } else {
                alert.setContentText("Password can't be empty!");
            }
            alert.showAndWait();
            return;
        }

        User user = userService.login(username, password);

        if (user != null) {
            if (user.getUsername().equals("admin")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminWindow.fxml"));
                Scene scene = new Scene(loader.load(), 1360, 740);
                AdminWindow controller = loader.getController();
                controller.setUserService(userService);
                controller.setFriendshipService(friendshipService);
                stage.setTitle("Admin Panel");
                scene.getStylesheets().add(getClass().getResource("/css/admin-window.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }
}
