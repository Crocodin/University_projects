package com.ubb.domain.notifications;

import com.ubb.domain.event.Event;
import com.ubb.domain.user.User;
import lombok.Getter;

@Getter
public class EventNotification extends Notification {
    private final Event from;
    private  final User to;
    private final String status;

    public EventNotification(long id, Event from, User to, String status) {
        super(id, NotificationType.EVENT);
        this.from = from;
        this.to = to;
        this.status = status;
    }
}
