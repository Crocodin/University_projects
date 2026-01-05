package com.ubb.service;

import com.ubb.domain.event.Event;
import com.ubb.domain.notifications.Notification;
import com.ubb.domain.notifications.FriendRequest;
import com.ubb.domain.notifications.EventNotification;
import com.ubb.domain.user.User;
import com.ubb.observer.Observable;
import com.ubb.observer.Observer;
import com.ubb.service.notifications.EventNotificationService;
import com.ubb.service.notifications.FriendRequestService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NotificationService implements Observable {

    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        this.observers.forEach(Observer::update);
    }

    @Getter
    private final FriendRequestService friendRequestService;
    private final EventNotificationService eventNotificationService;
    private final User loggedUser;

    @Getter
    private final ObservableList<Notification> notifications =
            FXCollections.observableArrayList();

    public NotificationService(
            FriendRequestService frs,
            EventNotificationService ens,
            User loggedUser
    ) {
        this.friendRequestService = frs;
        this.eventNotificationService = ens;
        this.loggedUser = loggedUser;
    }

    public void loadForUser(Long userId) throws SQLException {
        notifications.clear();

        List<Notification> all = new ArrayList<>();
        all.addAll(friendRequestService.getRequestsForUser(userId));
        all.addAll(eventNotificationService.getForUser(userId));

        notifications.setAll(all);
    }

    public void acceptFriendRequest(FriendRequest request) throws SQLException {
        friendRequestService.accept(request);
    }

    public void rejectFriendRequest(FriendRequest request) throws SQLException {
        friendRequestService.reject(request);
    }

    public void addEventNotification(Event event, User user) {
        EventNotification ev =
                new EventNotification(0L, event, user,
                        "Event with id " + event.getId() + " notified you at " +
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

        try {
            eventNotificationService.repository.add(ev);
            if (user.equals(this.loggedUser)) notifications.add(ev);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add event notification");
        }
    }

    public void addFriendRequest(FriendRequest friendRequest) throws SQLException {
        friendRequestService.sendRequest(friendRequest);
        if (friendRequest.getTo().equals(this.loggedUser)) notifications.add(friendRequest);
    }
}
