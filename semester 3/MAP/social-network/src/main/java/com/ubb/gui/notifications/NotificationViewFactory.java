package com.ubb.gui.notifications;

import com.ubb.domain.connection.Friendship;
import com.ubb.domain.notifications.EventNotification;
import com.ubb.domain.notifications.FriendRequest;
import com.ubb.domain.notifications.Notification;
import com.ubb.domain.notifications.RequestStatus;
import com.ubb.service.FriendService;
import com.ubb.service.NotificationService;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.sql.SQLException;

public class NotificationViewFactory {

    public static HBox createNotificationView(Notification notification, FriendService friendService, NotificationService notificationService) {
        if (notification instanceof FriendRequest fr) {
            return createFriendRequestView(fr,  friendService, notificationService);
        } else if (notification instanceof EventNotification ev) {
            return createRaceView(ev);
        }
        return new HBox();
    }

    private static HBox createFriendRequestView(FriendRequest fr, FriendService friendService, NotificationService notificationService) {
        Label text;

        Button accept = new Button("Accept");
        Button reject = new Button("Reject");

        if (!fr.getStatus().equals(RequestStatus.PENDING)) {
            accept.setDisable(true);
            reject.setDisable(true);
            if  (fr.getStatus().equals(RequestStatus.ACCEPTED)) {
                text = new Label("You have accepted " + fr.getFrom().getUsername() + " friend request");
            }
            else {
                text = new Label("You have rejected " + fr.getFrom().getUsername() + " friend request");
            }
        }
        else {
            text = new Label(fr.getFrom().getUsername() + " sent you a friend request");
        }

        accept.setOnAction(e -> {
            fr.accept();
            accept.setDisable(true);
            reject.setDisable(true);
            text.setText("You have accepted " + fr.getFrom().getUsername() + " friend request");
            try {
                friendService.addObject(new Friendship((long) 0, fr.getFrom(), fr.getTo()));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                notificationService.acceptFriendRequest(fr);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            notificationService.notifyObservers();
        });

        reject.setOnAction(e -> {
            fr.reject();
            accept.setDisable(true);
            reject.setDisable(true);
            text.setText("You have rejected " + fr.getFrom().getUsername() + " friend request");
            try {
                notificationService.rejectFriendRequest(fr);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        HBox buttons = new HBox(10, accept, reject);
        HBox container = new HBox(20, text, buttons);

        container.setPadding(new Insets(10));
        container.setStyle("""
            -fx-background-color: #f4f4f4;
            -fx-border-color: #ccc;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
        """);

        return container;
    }

    private static HBox createRaceView(EventNotification ev) {
        Label text = new Label(ev.getStatus());

        HBox container = new HBox(text);
        container.setPadding(new Insets(10));
        container.setStyle("""
            -fx-background-color: #e8f0ff;
            -fx-border-color: #aac4ff;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
        """);

        return container;
    }

}