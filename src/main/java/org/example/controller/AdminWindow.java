package org.example.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.domain.*;
import org.example.service.EventService;
import org.example.service.FriendshipService;
import org.example.service.UserService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class AdminWindow implements Observer {

    private UserService userService;

    private FriendshipService friendshipService;

    private EventService eventService;

    private AdminUserWindow userManagerController;

    private AdminFriendshipWindow friendshipManagerController;

    private AdminEventWindow eventManagerController;

    private Long currentPage = 1L;

    private Long pageSize = 5L;

    private Long totalPages;

    private boolean isFiltered = false;

    private String filterType = null;

    private List<UserRow> originalRows;

    public Label labelPage;

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
    private Label comboBoxLabel;

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
        List<User> allUsers = userService.getUsersPage(1L, userService.getUsersCount());
        originalRows = allUsers.stream()
                .filter(u -> u != null)
                .map(this::mapToUserRow)
                .toList();
        initializePagination();
        loadPage(1L);}

    public void setFriendshipService(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;}

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    private void loadUsers() {
        isFiltered = false;
        filterType = null;
        initializePagination();
        loadPage(1L);
    }

    private void filterUsers() {
        String selectedType = userTypeComboBox.getValue();

        if (selectedType == null || selectedType.isEmpty()) {
            userTable.getItems().setAll(originalRows);
            return;
        }

        isFiltered = true;
        filterType = selectedType;
        initializePagination();
        loadPage(1L);
    }

    public void openUserManagerWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminUserWindow.fxml"));
        Scene scene = new Scene(loader.load(), 660, 640);
        this.userManagerController = loader.getController();
        userManagerController.setUserService(userService);
        userManagerController.addObserver(this);
        if (friendshipManagerController != null) {
            userManagerController.addObserver(friendshipManagerController);
        }
        stage.setTitle("Manage Users");
        scene.getStylesheets().add(getClass().getResource("/css/user-manager-window.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void openFriendshipManagerWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminFriendshipWindow.fxml"));
        Scene scene = new Scene(loader.load(), 860, 640);
        this.friendshipManagerController = loader.getController();
        friendshipManagerController.setFriendshipService(friendshipService);
        if (userManagerController != null) {
            userManagerController.addObserver(friendshipManagerController);
        }
        stage.setTitle("Manage Friendships");
        scene.getStylesheets().add(getClass().getResource("/css/friendship-manager-window.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public void openEventManagerWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminEventWindow.fxml"));
        Scene scene = new Scene(loader.load(), 430, 640);
        this.eventManagerController = loader.getController();
        eventManagerController.setEventService(eventService);
        stage.setTitle("Manage Events");
        scene.getStylesheets().add(getClass().getResource("/css/event-manager-window.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void update(String message) {
        System.out.println(message);
        loadPage(currentPage);
    }

    public void initializePagination() {
        if (isFiltered && filterType != null) {
            long count = userService.getDucksCountByType(filterType);
            this.totalPages = (long) Math.ceil(count / (double) pageSize);
        } else {
            this.totalPages = (long) Math.ceil(userService.getUsersCount() / (double) pageSize);
        }
    }

    public void loadPage(Long pageNumber) {
        if (pageNumber < 1 || pageNumber > totalPages) {
            return;
        }
        currentPage = pageNumber;
        labelPage.setText(String.valueOf(currentPage));
        List<User> users;
        if (isFiltered && filterType != null) {
            users = userService.getDucksPageByType(filterType, pageNumber, pageSize);
        } else {
            users = userService.getUsersPage(pageNumber, pageSize);
        }
        userTable.getItems().setAll(users.stream().map(u -> {
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
        }).toList());
    }

    public void nextPage() {
        if (currentPage + 1 > totalPages) {
            return;
        }
        loadPage(currentPage + 1);
    }

    public void previousPage() {
        if (currentPage - 1 < 1) {
            return;
        }
        loadPage(currentPage - 1);
    }

    private UserRow mapToUserRow(User u) {
        if (u == null) return null;

        UserRow r = new UserRow();
        r.setId(u.getId());
        r.setUserType(u.getUserType());
        r.setUsername(u.getUsername());
        r.setEmail(u.getEmail());
        r.setPassword(u.getPassword());

        if (u instanceof Person p) {
            r.setSurname(p.getSurname());
            r.setName(p.getName());
            r.setBirthDate(p.getBirthdate());
            r.setOccupation(p.getOccupation());
        }

        if (u instanceof Duck d) {
            r.setDuckType(d.getDuckType());
            r.setSpeed(d.getSpeed());
            r.setResistance(d.getResistance());
        }

        return r;
    }
}
