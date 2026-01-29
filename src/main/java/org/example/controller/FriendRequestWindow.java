package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.domain.User;
import org.example.service.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class FriendRequestWindow {
    @FXML
    private TableView<User> friendRequestsTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> firstNameColumn;

    @FXML
    private TableColumn<User, String> lastNameColumn;

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;
    private MessageService messageService;
    private EventService eventService;

    private User currentUser;
    private ObservableList<User> friendRequests;

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    public void setServices(UserService userService, FriendshipService friendshipService, FriendshipRequestService friendshipRequestService, MessageService messageService, EventService eventService, User currentUser) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipRequestService = friendshipRequestService;
        this.messageService = messageService;
        this.eventService = eventService;
        this.currentUser = currentUser;
        loadFriendRequests();
    }

    private void loadFriendRequests() {
        List<User> requests = friendshipRequestService.getFriendRequestsUser(currentUser.getId());
        requests = requests.stream()
                .filter(user -> !user.equals(currentUser))
                .collect(Collectors.toList());
        friendRequests = FXCollections.observableArrayList(requests);
        friendRequestsTable.setItems(friendRequests);
    }

    @FXML
    private void handleAccept() {
        User selectedUser = friendRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            friendshipRequestService.acceptFriendRequest(currentUser.getId(), selectedUser.getId());
            friendRequests.remove(selectedUser);
        }
    }

    @FXML
    private void handleDeny() {
        User selectedUser = friendRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            friendshipRequestService.denyFriendRequest(currentUser.getId(), selectedUser.getId());
            friendRequests.remove(selectedUser);
        }
    }

    @FXML
    private void handleProfile() throws IOException {
        openUserWindow(currentUser);
        closeWindow((Stage) friendRequestsTable.getScene().getWindow());
    }

    private void openUserWindow(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProfileWindow.fxml"));
        Scene scene = new Scene(loader.load(), 360, 640);

        ProfileWindow controller = loader.getController();
        controller.setUserService(userService);
        controller.setFriendshipService(friendshipService);
        controller.setFriendshipRequestService(friendshipRequestService);
        controller.setMessageService(messageService);
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
