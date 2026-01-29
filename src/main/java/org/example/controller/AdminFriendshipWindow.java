package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.domain.*;
import org.example.service.FriendshipService;

import java.util.ArrayList;
import java.util.List;

public class AdminFriendshipWindow implements Observer {

    private FriendshipService friendshipService;

    private List<Observer> observers = new ArrayList<>();

    @FXML
    private TableView<Friendship> friendshipTable;

    @FXML
    private TableColumn<Friendship, Long>  friendshipId;

    @FXML
    private TableColumn<Friendship, Long>  firstFriendId;

    @FXML
    private TableColumn<Friendship, Long>  secondFriendId;

    @FXML
    private TextField fieldFriendshipId;

    @FXML
    private TextField fieldFirstFriendId;

    @FXML
    private TextField fieldSecondFriendId;

    @FXML
    private void initialize() {
        configureTableColumns();
    }

    private void configureTableColumns() {
        friendshipId.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstFriendId.setCellValueFactory(new PropertyValueFactory<>("firstFriendId"));
        secondFriendId.setCellValueFactory(new PropertyValueFactory<>("secondFriendId"));
    }

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
        loadFriendships();
    }

    private void loadFriendships() {
        List<Friendship> friendships = (List<Friendship>) friendshipService.findAll();
        friendshipTable.getItems().setAll(friendships);
    }

    @Override
    public void update(String message) {
        System.out.println(message);
        loadFriendships();
    }

    public void addFriendship() {
        String idFirstFriend = fieldFirstFriendId.getText();
        String idSecondFriend = fieldSecondFriendId.getText();
        try {
            friendshipService.add(idFirstFriend, idSecondFriend);
            loadFriendships();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Friendship Invalid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void removeFriendship() {
        String userId = fieldFriendshipId.getText();
        try {
            friendshipService.remove(userId);
            loadFriendships();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("User Invalid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void showCommunities() {
        int numberOfCommunities = friendshipService.getNumberOfCommunities();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Communities");
        alert.setHeaderText("All Communities");
        alert.setContentText(String.valueOf(numberOfCommunities));
        alert.showAndWait();
    }

    public void showMostSociableCommunity() {
        List<Long> community = friendshipService.getMostSociableCommunity();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Most Sociable Community");
        alert.setHeaderText("The users in the most sociable community are:");
        alert.setContentText(community.toString());
        alert.showAndWait();
    }
}
