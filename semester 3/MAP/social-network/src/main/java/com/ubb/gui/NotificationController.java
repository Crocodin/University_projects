package com.ubb.gui;

import com.ubb.domain.notifications.Notification;
import com.ubb.domain.user.User;
import com.ubb.gui.notifications.NotificationViewFactory;
import com.ubb.observer.Observer;
import com.ubb.service.FriendService;
import com.ubb.service.NotificationService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class NotificationController implements Observer {

    @FXML
    private VBox notificationContainer;

    @FXML
    private Label notificationLabel;

    private NotificationService notificationService;
    private User loggedUser;
    private FriendService friendService;
    public void setService(NotificationService service,  User loggedUser, FriendService friendService) throws SQLException {
        this.notificationService = service;
        this.friendService = friendService;
        this.loggedUser = loggedUser;

        service.loadForUser(loggedUser.getId());
        service.getNotifications().addListener(
                (javafx.collections.ListChangeListener<Notification>) c -> refresh()
        );

        refresh();
    }

    private void refresh() {
        notificationContainer.getChildren().clear();

        if (notificationService.getNotifications().isEmpty()) {
            notificationLabel.setText("No notifications");
            return;
        }

        notificationLabel.setText("Notifications");

        for (Notification n : notificationService.getNotifications()) {
            HBox view = NotificationViewFactory.createNotificationView(n, friendService, notificationService);
            notificationContainer.getChildren().add(view);
        }
    }

    @Override
    public void update() {
        //refresh();
    }
}
