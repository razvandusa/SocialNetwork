package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.example.domain.Duck;
import org.example.domain.Person;
import org.example.domain.User;
import org.example.domain.UserRow;
import org.example.service.UserService;

import java.time.LocalDate;
import java.util.List;

public class MainWindow {

    private Label comboBoxLabel;

    private UserService userService;

    private List<UserRow> originalRows;

    @FXML
    private ComboBox<String> userTypeComboBox;

    @FXML
    private TableView<UserRow> userTable;

    @FXML
    private TableColumn<UserRow, Long> userId;

    @FXML
    private TableColumn<UserRow, String> userType;

    @FXML
    private TableColumn<UserRow, String> userUsername;

    @FXML
    private TableColumn<UserRow, String> userEmail;

    @FXML
    private TableColumn<UserRow, String> userPassword;

    @FXML
    private TableColumn<UserRow, String> userSurname;

    @FXML
    private TableColumn<UserRow, String> userName;

    @FXML
    private TableColumn<UserRow, LocalDate> userBirthdate;

    @FXML
    private TableColumn<UserRow, String> userOccupation;

    @FXML
    private TableColumn<UserRow, String> userDuckType;

    @FXML
    private TableColumn<UserRow, Double> userSpeed;

    @FXML
    private TableColumn<UserRow, Double> userResistance;

    @FXML
    private void initialize() {
        configureTableColumns();
        configureComboBox();
        userTypeComboBox.setOnAction(event -> filterUsers());
    }

    private void configureTableColumns() {
        userId.setCellValueFactory(new PropertyValueFactory<>("id"));
        userType.setCellValueFactory(new PropertyValueFactory<>("userType"));
        userUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        userEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        userPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        userSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        userName.setCellValueFactory(new PropertyValueFactory<>("name"));
        userBirthdate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        userOccupation.setCellValueFactory(new PropertyValueFactory<>("occupation"));
        userDuckType.setCellValueFactory(new PropertyValueFactory<>("duckType"));
        userSpeed.setCellValueFactory(new PropertyValueFactory<>("speed"));
        userResistance.setCellValueFactory(new PropertyValueFactory<>("resistance"));
    }

    private void configureComboBox() {
        userTypeComboBox.getItems().addAll("SWIMMING", "FLYING", "FLYING_AND_SWIMMING");
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
        loadUsers();}

    private void loadUsers() {
        List<User> users = (List<User>) userService.findAll();
        originalRows = users.stream().map(u -> {
            UserRow r = new UserRow();
            r.setId(u.getId());
            r.setUserType(u.getUserType());
            r.setUsername(u.getUsername());
            r.setEmail(u.getEmail());
            r.setPassword(u.getPassword());

            if (u.getUserType().equals("Person")) {
                Person p = (Person) u;
                r.setSurname(p.getSurname());
                r.setName(p.getName());
                r.setBirthDate(p.getBirthdate());
                r.setOccupation(p.getOccupation());
            }

            if (u.getUserType().equals("Duck")) {
                Duck d = (Duck) u;
                r.setDuckType(d.getDuckType());
                r.setSpeed(d.getSpeed());
                r.setResistance(d.getResistance());
            }

            return r;
        }).toList();

        userTable.getItems().setAll(originalRows);
    }

    private void filterUsers() {
        String selectedType = userTypeComboBox.getValue();

        if (selectedType == null || selectedType.isEmpty()) {
            userTable.getItems().setAll(originalRows);
            return;
        }

        List<UserRow> filtered = originalRows.stream()
                .filter(r -> selectedType.equals(r.getDuckType()))
                .toList();

        userTable.getItems().setAll(filtered);
    }
}
