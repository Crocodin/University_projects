package com.ubb.gui;

import com.ubb.domain.connection.Message;
import com.ubb.domain.user.User;
import com.ubb.service.FriendService;
import com.ubb.service.MessageService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class MessageTabController {
    @FXML
    TableView<User> userFriendTableView;

    @FXML
    private Label selectedMessageLabel;

    @FXML
    TextField messageTextField;

    @FXML
    ListView<Message> chatListView;

    @FXML
    Button sendMessageButton;

    private final FriendService friendService;
    private final MessageService messageService;
    private final User loggedUser;
    private User messageTo = null;
    private Message selectedMessage = null;
    private ObservableList<Message> messages = FXCollections.observableArrayList();

    public MessageTabController(FriendService friendService, MessageService messageService, User loggedUser) {
        this.friendService = friendService;
        this.messageService = messageService;
        this.loggedUser = loggedUser;
    }

    @FXML
    public void initialize() {
        userFriendTableView.setEditable(true);

        TableColumn<User, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getId())
        );

        TableColumn<User, String> colFriend = new TableColumn<>("Friend");
        colFriend.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getUsername())
        );

        userFriendTableView.getColumns().addAll(colId, colFriend);

        ObservableList<User> friendData = FXCollections.observableArrayList();
        userFriendTableView.setItems(friendData);

        friendData.setAll(friendService.getFriends(loggedUser));

        userFriendTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                messageTo = newValue;
                loadMessages();
            }
        });

        sendMessageButton.setOnAction(event -> {
            if (messageTo != null && !messageTextField.getText().isEmpty()) {
                try {
                    if (selectedMessage == null) {
                        messageService.sendMessage(loggedUser, messageTo, messageTextField.getText());
                    } else  {
                        messageService.sendMessage(loggedUser, messageTo, messageTextField.getText(), selectedMessage);
                    }
                    messageTextField.clear();
                    loadMessages();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        chatListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                selectedMessage = chatListView.getSelectionModel().getSelectedItem();
                selectedMessageLabel.setText(selectedMessage.getMessage());
            }
        });

        messageTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessageButton.fire();
            }
        });

        chatListView.setItems(
                messages
        );
    }

    private void loadMessages() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        messages.clear();
        messages.setAll(messageService.getMessagesBetweenUsers(loggedUser.getId(), messageTo.getId()));

        chatListView.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Message msg, boolean empty) {
                super.updateItem(msg, empty);

                if (empty || msg == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    StringBuilder displayText = new StringBuilder();

                    if (msg.getReplyTo() != null) {
                        String originalMsg = msg.getReplyTo().getMessage();
                        if (originalMsg.length() > 30) {
                            originalMsg = originalMsg.substring(0, 30) + "...";
                        }
                        displayText.append("➥").append(msg.getReplyTo().getFrom().getUsername())
                                .append(": ").append(originalMsg).append("\n");
                    }

                    displayText.append(String.format("[%s] %s: %s",
                            msg.getTimestamp().format(formatter),
                            msg.getFrom().getUsername(),
                            msg.getMessage()));

                    setText(displayText.toString());

                    getStyleClass().removeAll("message_bubble_sender", "message_bubble_receiver");

                    if (msg.getFrom().equals(loggedUser)) {
                        getStyleClass().add("message_bubble_sender");
                    } else {
                        getStyleClass().add("message_bubble_receiver");
                    }
                }
            }
        });
    }
}
