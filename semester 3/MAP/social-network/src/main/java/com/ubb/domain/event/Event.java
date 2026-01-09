package com.ubb.domain.event;

import com.ubb.domain.Entity;
import com.ubb.domain.duck.Duck;
import com.ubb.domain.user.User;
import com.ubb.service.NotificationService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents an event that users can subscribe to. This class fallows the {@code Observer patterns}
 * <p>
 * Each {@code Event} has a unique identifier and maintains
 * a list of subscribed {@link User} instances.
 * When the event occurs or changes, it can notify all its subscribers
 * by calling their {@code update()} method.
 * </p>
 */
@Getter
public class Event extends Entity<Long> {
    /** The unique identifier of the event. */
    private final String name;

    /** The list of users subscribed to this event. */
    private final List<User> subscribers = new ArrayList<>();

    public Event(Long id, String name, List<User> subscribers) {
        super(id);
        this.name = name;
        this.subscribers.addAll(subscribers);
    }

    /**
     * Subscribes a user to this event.
     * The user will be notified when {@link} is called.
     *
     * @param user the user to subscribe
     */
    public void subscribe(User user) {
        subscribers.add(user);
    }

    /**
     * Unsubscribes a user from this event.
     *
     * @param user the user to remove from the subscriber list
     */
    public void unsubscribe(User user) {
        subscribers.remove(user);
    }

    /**
     * Notifies all subscribed users of an event update
     * by invoking their method.
     */
    public void notifySubscribers(NotificationService notificationService) {
        for  (User user : subscribers) {
            notificationService.addEventNotification(this, user);
        }
    }

    public void notifySubscribers(NotificationService notificationService, String message) {
        for  (User user : subscribers) {
            notificationService.addEventNotification(this, user, message);
        }
    }

    @Override
    public String toString() {
        String subscribersIds = subscribers.stream()
                .map(u -> String.valueOf(u.getId()))
                .collect(Collectors.joining("|"));

        return "E," + super.getId() + "," + name + "," + subscribersIds;
    }

    public static Event fromString(String line, Map<Long, User> allUsers) {
        // Expected format: E,<id>,<name>,<id1>|<id2>|<id3>
        String[] parts = line.split(",", 4);
        if (parts.length < 3) return null;

        long id = Long.parseLong(parts[1]);
        String name = parts[2];

        List<User> subscribers = new ArrayList<>();
        if (parts.length == 4 && !parts[3].isEmpty()) {
            for (String idStr : parts[3].split("\\|")) {
                long userId = Long.parseLong(idStr);
                User user = allUsers.get(userId);
                if (user != null) subscribers.add(user);
            }
        }

        return new Event(id, name, subscribers);
    }
}
