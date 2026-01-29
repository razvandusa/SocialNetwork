package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.domain.*;
import org.example.service.EventService;

import java.util.List;

public class AdminEventWindow {

    private EventService eventService;

    @FXML
    private TableView<Event> eventsTable;

    @FXML
    private TableColumn<Event, Long>  eventId;

    @FXML
    private TableColumn<Event, String>  eventName;

    @FXML
    private TableColumn<Event, String>  eventType;

    @FXML
    private TableColumn<Event, Long>  endStatus;

    @FXML
    private void initialize() {
        configureTableColumns();
    }

    private void configureTableColumns() {
        eventId.setCellValueFactory(new PropertyValueFactory<>("id"));
        eventName.setCellValueFactory(new PropertyValueFactory<>("name"));
        eventType.setCellValueFactory(new PropertyValueFactory<>("type"));
        endStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
        loadEvents();
    }

    private void loadEvents() {
        List<Event> events = (List<Event>) eventService.findAll();
        eventsTable.getItems().setAll(events);
    }

    public void startEvent() {
        String eventId = eventsTable.getSelectionModel().getSelectedItem().getId().toString();
        try {
            eventService.start(eventId);
            loadEvents();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Event Invalid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void resetEvent() {
        String eventId = eventsTable.getSelectionModel().getSelectedItem().getId().toString();
        try {
            eventService.reset(eventId);
            loadEvents();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Event Invalid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
