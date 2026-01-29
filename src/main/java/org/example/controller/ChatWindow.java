package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.domain.Message;
import org.example.domain.User;
import org.example.service.MessageService;

import java.time.format.DateTimeFormatter;

public class ChatWindow {
    @FXML
    private ListView<Message> messagesListView;

    @FXML
    private TextField messageTextField;

    @FXML
    private Button sendMessageButton;

    @FXML
    private Button replyButton;

    @FXML
    private Label selectedMessageLabel;

    private MessageService messageService;

    private User currentUser;
    private User chatUser;
    private Message selectedMessage;

    public void setServices(MessageService messageService, User currentUser, User chatUser) {
        this.messageService = messageService;
        this.currentUser = currentUser;
        this.chatUser = chatUser;
        loadMessages();
    }

    private void loadMessages() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        messagesListView.setItems(messageService.getMessagesBetweenUsers(currentUser.getId(), chatUser.getId()));
        messagesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);
                if (empty || message == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox();
                    String senderName = message.getSenderId().equals(currentUser.getId()) ? currentUser.getUsername() : chatUser.getUsername();
                    Label nameLabel = new Label(senderName + ":");
                    nameLabel.setStyle("-fx-font-weight: bold;");
                    vbox.getChildren().add(nameLabel);

                    Label timestampLabel = new Label(message.getTimestamp().format(formatter));
                    timestampLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #000000;");
                    vbox.getChildren().add(timestampLabel);

                    if (message.getReplyTo() != null) {
                        Label replyLabel = new Label("Reply to: " + message.getReplyTo().getText());
                        replyLabel.setStyle("-fx-font-style: italic; -fx-text-fill: #000000;");
                        vbox.getChildren().add(replyLabel);
                    }

                    Label contentLabel = new Label(message.getText());
                    contentLabel.setWrapText(true);
                    vbox.getChildren().add(contentLabel);

                    if (message.getSenderId().equals(currentUser.getId())) {
                        vbox.setStyle(
                            "-fx-background-color: #2bdb21;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-border-radius: 5px;" +
                            "-fx-padding: 5;"
                        );
                    } else {
                        vbox.setStyle(
                            "-fx-background-color: #FFFFFF;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-border-radius: 5px;" +
                            "-fx-padding: 5;"
                        );
                    }

                    setGraphic(vbox);
                }
            }
        });
        messagesListView.scrollTo(messagesListView.getItems().size() - 1);
    }

    @FXML
    private void sendMessageButtonClicked() {
        String text = messageTextField.getText();
        if (!text.isEmpty()) {
            if (selectedMessage != null) {
                messageService.sendMessage(text, currentUser.getId().toString(), chatUser.getId().toString(), selectedMessage.getId().toString());
            } else {
                messageService.sendMessage(text, currentUser.getId().toString(), chatUser.getId().toString());
            }
            messageTextField.clear();
            selectedMessage = null;
            selectedMessageLabel.setText("");
            loadMessages();
            messagesListView.scrollTo(messagesListView.getItems().size() - 1);
        }
    }

    @FXML
    private void replyButtonClicked() {
        selectedMessage = messagesListView.getSelectionModel().getSelectedItem();
        if (selectedMessage != null) {
            selectedMessageLabel.setText("Replying to: " + selectedMessage.getText());
        }
    }
}
