package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.domain.Observer;
import org.example.domain.User;
import org.example.service.FriendshipRequestService;
import org.example.service.FriendshipService;
import org.example.service.MessageService;
import org.example.service.UserService;

import java.io.IOException;
import java.util.List;

public class MessagesWindow implements Observer {
    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;
    private MessageService messageService;
    private User currentUser;
    private ObservableList<User> friendsList = FXCollections.observableArrayList();
    private int currentPage = 1;
    private final int pageSize = 5;

    @Override
    public void onNotification(String message) {
        loadFriends();
    }

    @FXML
    private TableView<User> friendsTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private Button prevPageButton;

    @FXML
    private Button nextPageButton;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setFriendshipService(FriendshipService friendshipService) { this.friendshipService = friendshipService; }

    public void setFriendshipRequestService(FriendshipRequestService friendshipRequestService) { this.friendshipRequestService = friendshipRequestService; }

    public void setMessageService(MessageService messageService) { this.messageService = messageService; }

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    }

    private void
    loadFriends() {
        List<User> friends = friendshipService.findFriends(currentUser.getId(), currentPage, pageSize);
        friendsList.setAll(friends);
        friendsTable.setItems(friendsList);

        prevPageButton.setDisable(currentPage == 1);
        nextPageButton.setDisable(friends.size() < pageSize);
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            loadFriends();
        }
    }

    @FXML
    private void handleNextPage() {
        currentPage++;
        loadFriends();
    }

    @FXML
    private void handleChat() {
        User selectedUser = friendsTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ChatWindow.fxml"));
                Scene scene = new Scene(loader.load(), 360, 660);
                scene.getStylesheets().add(getClass().getResource("/css/chat-window.css").toExternalForm());

                ChatWindow chatController = loader.getController();
                chatController.setServices(messageService, currentUser, selectedUser);

                Stage stage = new Stage();
                stage.setTitle("Chat with " + selectedUser.getUsername());
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to Load Chat Page", "An error occurred while trying to load the Chat page: " + e.getMessage());
            }
        } else {
            showAlert("No Selection", "No User Selected", "Please select a user in the table.");
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleProfile() throws IOException {
        openUserWindow(currentUser);
        closeWindow((Stage) friendsTable.getScene().getWindow());
    }

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

    private void closeWindow(Stage stage) { stage.close(); }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        loadFriends();
    }
}
