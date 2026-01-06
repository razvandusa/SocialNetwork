package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.domain.Observer;
import org.example.domain.Subject;
import org.example.service.UserService;

import java.util.ArrayList;
import java.util.List;

public class AdminUserWindow implements Subject {

    private List<Observer> observers = new ArrayList<>();

    private UserService userService;

    @FXML
    private TextField fieldUserId;

    @FXML
    private TextField fieldUserType;

    @FXML
    private TextField fieldUserUsername;

    @FXML
    private TextField fieldUserEmail;

    @FXML
    private TextField fieldUserPassword;

    @FXML
    private TextField fieldPersonSurname;

    @FXML
    private TextField fieldPersonName;

    @FXML
    private TextField fieldPersonBirthdate;

    @FXML
    private TextField fieldPersonOccupation;

    @FXML
    private TextField fieldDuckType;

    @FXML
    private TextField fieldDuckSpeed;

    @FXML
    private TextField fieldDuckResistance;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void addUser() {
        String userType = fieldUserType.getText();
        String username = fieldUserUsername.getText();
        String email = fieldUserEmail.getText();
        String password = fieldUserPassword.getText();
        String surname = fieldPersonSurname.getText();
        String name = fieldPersonName.getText();
        String birthdate = fieldPersonBirthdate.getText();
        String occupation = fieldPersonOccupation.getText();
        String duckType = fieldDuckType.getText();
        String speed = fieldDuckSpeed.getText();
        String resistance = fieldDuckResistance.getText();
        try {
            if (userType.equals("Person")) {
                userService.add(userType, username, email, password, surname, name, birthdate, occupation);
                notifySubscribers("User added successfully!");
            } else if (userType.equals("Duck")) {
                userService.add(userType, username, email, password, duckType, speed, resistance);
                notifySubscribers("User added successfully!");
            } else {
                throw new IllegalArgumentException("Invalid user type +" + userType);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("User Invalid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void removeUser() {
        String userId = fieldUserId.getText();
        try {
            userService.remove(userId);
            notifySubscribers("User removed successfully!");
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("User Invalid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void subscribe(Observer o) {
        observers.add(o);
    }

    @Override
    public void unsubscribe(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifySubscribers(String message) {
        for (Observer observer : observers) {
            observer.onNotification(message);
        }
    }
}
