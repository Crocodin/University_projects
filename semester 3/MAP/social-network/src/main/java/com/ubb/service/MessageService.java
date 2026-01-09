package com.ubb.service;

import com.ubb.config.Config;
import com.ubb.domain.connection.Message;
import com.ubb.domain.user.User;
import com.ubb.facade.UserFacade;
import com.ubb.repo.database.MessageDBRepo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MessageService {
    private final MessageDBRepo messageDBRepo;
    private final ObservableList<Message> messages;

    public MessageService(UserFacade userFacade) {
        this.messageDBRepo = new MessageDBRepo(
                Config.getProperties().getProperty("DB_URL"),
                Config.getProperties().getProperty("DB_USER"),
                Config.getProperties().getProperty("DB_PASSWORD"),
                userFacade
        );
        this.messages = FXCollections.observableArrayList();
    }

    public void sendMessage(User sender, User receiver, String message) throws SQLException {
        var msg = new Message((long) 0, sender, List.of(receiver), message, LocalDateTime.now());
        messageDBRepo.add(msg);
    }

    public void sendMessage(User sender, User receiver, String message, Message replyTo) throws SQLException {
        var msg = new Message((long) 0, sender, List.of(receiver), message, LocalDateTime.now(),  replyTo);
        messageDBRepo.add(msg);
    }

    public ObservableList<Message> getMessagesBetweenUsers(Long userId1, Long userId2) {
        List<Message> messageList = messageDBRepo.getMessagesBetweenUsers(userId1, userId2);
        messages.setAll(messageList);
        return messages;
    }

    public long getNumberOfMessages(long userId) {
        try {
            return messageDBRepo.getNumberOfMessages(userId);
        } catch (SQLException e) {
            return 0;
        }
    }
}
