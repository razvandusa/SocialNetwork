package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.domain.User;
import org.example.service.FriendshipRequestService;
import org.example.service.FriendshipService;
import org.example.service.MessageService;
import org.example.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AddFriendWindow {

    @FXML
    private TableView<User> usersTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private Button notifiationButton;

    @FXML
    private Button prevPageButton;

    @FXML
    private Button nextPageButton;

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;
    private MessageService messageService;
    private User currentUser;
    private ObservableList<User> availableUsers;
    private int currentPage = 1;
    private final int pageSize = 10;

    public void setServices(UserService userService, FriendshipService friendshipService, FriendshipRequestService friendshipRequestService, MessageService messageService, User currentUser) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipRequestService = friendshipRequestService;
        this.messageService = messageService;
        this.currentUser = currentUser;
        loadUsers();
        updateNotificationIcon();
    }

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    private void loadUsers() {
        List<User> allUsers = StreamSupport.stream(userService.findAll().spliterator(), false)
                .collect(Collectors.toList());

        List<User> friends = friendshipService.findFriends(currentUser.getId(), 1, Integer.MAX_VALUE);

        List<User> friendRequests = friendshipRequestService.getFriendRequests(currentUser.getId());

        List<User> filteredUsers = allUsers.stream()
                .filter(user -> !friends.contains(user) && !friendRequests.contains(user) && !user.equals(currentUser))
                .collect(Collectors.toList());

        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, filteredUsers.size());
        List<User> paginatedUsers = filteredUsers.subList(fromIndex, toIndex);

        availableUsers = FXCollections.observableArrayList(paginatedUsers);
        usersTable.setItems(availableUsers);

        prevPageButton.setDisable(currentPage == 1);
        nextPageButton.setDisable(toIndex >= filteredUsers.size());
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            loadUsers();
        }
    }

    @FXML
    private void handleNextPage() {
        currentPage++;
        loadUsers();
    }

    @FXML
    private void handleAddFriend() {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            friendshipRequestService.sendFriendRequest(currentUser.getId(), selectedUser.getId());
            availableUsers.remove(selectedUser);
        }
    }

    @FXML
    private void handleFriendRequests() {
        openFriendRequestWindow(currentUser);
        closeWindow((Stage) usersTable.getScene().getWindow());
    }

    private void openFriendRequestWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FriendRequestWindow.fxml"));
            Scene scene = new Scene(loader.load(), 360, 750);

            FriendRequestWindow controller = loader.getController();
            controller.setServices(userService, friendshipService, friendshipRequestService, messageService, user);
            scene.getStylesheets().add(getClass().getResource("/css/friend-request-window.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Friend Requests");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProfile() throws IOException {
        openUserWindow(currentUser);
        closeWindow((Stage) usersTable.getScene().getWindow());
    }

    private void closeWindow(Stage stage) { stage.close(); }

    private void openUserWindow(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ProfileWindow.fxml"));
        Scene scene = new Scene(loader.load(), 360, 640);

        ProfileWindow controller = loader.getController();
        controller.setUserService(userService);
        controller.setFriendshipService(friendshipService);
        controller.setFriendshipRequestService(friendshipRequestService);
        controller.setMessageService(messageService);
        controller.setUser(user);
        scene.getStylesheets().add(getClass().getResource("/css/profile-window.css").toExternalForm());

        Stage stage = new Stage();
        stage.setTitle("User Profile");
        stage.setScene(scene);
        stage.show();
    }

    public void updateNotificationIcon() {

        boolean hasNotifications = friendshipRequestService.hasPendingRequests(currentUser.getId());

        ImageView iv = (ImageView) notifiationButton.getGraphic();

        if (hasNotifications) {
            iv.setImage(new Image(getClass().getResourceAsStream("/images/notificationDotIcon.png")));
        } else {
            iv.setImage(new Image(getClass().getResourceAsStream("/images/notificationIcon.png")));
        }
    }
}