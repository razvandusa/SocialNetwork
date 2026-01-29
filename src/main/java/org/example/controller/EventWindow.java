package org.example.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.domain.Event;
import org.example.domain.Observer;
import org.example.domain.User;
import org.example.service.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EventWindow implements Observer {

    boolean hasNotifications;

    @FXML
    private TableView<Event> eventsTable;

    @FXML
    private TableColumn<Event, String> eventNameColumn;

    @FXML
    private TableColumn<Event, String> eventTypeColumn;

    @FXML
    private Button notificationButton;

    @FXML
    private Button prevPageButton;

    @FXML
    private Button nextPageButton;

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;
    private MessageService messageService;
    private EventService eventService;

    private User currentUser;
    private ObservableList<Event> events;
    private int currentPage = 1;
    private final int pageSize = 10;

    @FXML
    public void initialize() {
        eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        eventTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    public void setServices(UserService userService, FriendshipService friendshipService, FriendshipRequestService friendshipRequestService, MessageService messageService, EventService eventService, User currentUser) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipRequestService = friendshipRequestService;
        this.messageService = messageService;
        this.eventService = eventService;
        this.currentUser = currentUser;
        eventService.addObserver(this);
        loadEvents();
        updateNotificationIcon();
    }

    private void loadEvents() {
        List<Event> allEvents = StreamSupport.stream(eventService.findAll().spliterator(), false)
                .collect(Collectors.toList());

        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, allEvents.size());
        List<Event> paginatedEvents = allEvents.subList(fromIndex, toIndex);

        events = FXCollections.observableArrayList(paginatedEvents);
        eventsTable.setItems(events);
        eventsTable.setRowFactory(_ -> new TableRow<>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);
                if (event == null || empty) {
                    setStyle("");
                } else if (eventService.findEvents(currentUser).contains(event)) {
                    setStyle("-fx-background-color: #c8f7c5;");
                } else {
                    setStyle("");
                }
            }
        });

        prevPageButton.setDisable(currentPage == 1);
        nextPageButton.setDisable(toIndex >= allEvents.size());
    }

    @FXML
    private void handlePrevPage() {
        if (currentPage > 1) {
            currentPage--;
            loadEvents();
        }
    }

    @FXML
    private void handleNextPage() {
        currentPage++;
        loadEvents();
    }

    @FXML
    private void handleSubscribeEvent() {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                eventService.addSpectatorToEvent(selectedEvent.getId().toString(), currentUser.getId().toString());
                selectedEvent.addObserver(this);
                hasNotifications = false;
                eventsTable.refresh();
            } catch (Exception e) {
                showInformationAlert("Subscribe Failed", "User is already subscribed to this event!", "");
            }
        }
    }

    @FXML
    private void handleUnsubscribeEvent() {
        Event selectedEvent = eventsTable.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                eventService.removeSpectatorFromEvent(selectedEvent.getId().toString(), currentUser.getId().toString());
                selectedEvent.removeObserver(this);
                hasNotifications = false;
                eventsTable.refresh();
            } catch (Exception e) {
                showInformationAlert("Unsubscribe Failed", "User is not subscribed to this event!", "");
            }
        }
    }

    @FXML
    private void handleProfile() throws IOException {
        openUserWindow(currentUser);
        closeWindow((Stage) eventsTable.getScene().getWindow());
    }

    @FXML
    private void handleNotifications() {
        List<Event> subscribedEvents = eventService.findEvents(currentUser);
        List<Event> endedEvents = subscribedEvents.stream()
                .filter(Event::getStatus)
                .toList();

        if (endedEvents.isEmpty()) {
            return;
        } else {
            ExecutorService pool = Executors.newFixedThreadPool(4);
            List<CompletableFuture<String>> futures = endedEvents.stream()
                    .map(e -> CompletableFuture.supplyAsync(() -> {
                        return "Event: " + e.getName() + "\n" +
                                "Type: " + e.getType() + "\n" +
                                eventService.getResults(e.getId().toString()) +
                                "-----------------\n";
                    }, pool))
                    .toList();


            StringBuilder content = new StringBuilder();
            for (CompletableFuture<String> f : futures) {
                try {
                    content.append(f.get());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            pool.shutdown();
            try {
                if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                }
            } catch (InterruptedException ex) {
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
            hasNotifications = false;
            updateNotificationIcon();
            showInformationAlert("Notifications", "You have notifications for these events:", content.toString());
        }
    }

    private void showInformationAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
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

    private void closeWindow(Stage stage) {
        friendshipRequestService.removeObserver(this);
        stage.close();
    }

    public void updateNotificationIcon() {
        ImageView iv = (ImageView) notificationButton.getGraphic();
        if (hasNotifications) {
            iv.setImage(new Image(getClass().getResourceAsStream("/images/notificationDotIcon.png")));
        } else {
            iv.setImage(new Image(getClass().getResourceAsStream("/images/notificationIcon.png")));
        }
    }

    @Override
    public void update(String message) {
        hasNotifications = true;
        updateNotificationIcon();
    }

    public User getCurrentUser() {
        return currentUser;
    }
}