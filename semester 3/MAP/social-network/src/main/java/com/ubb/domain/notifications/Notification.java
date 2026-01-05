package com.ubb.domain.notifications;

import com.ubb.domain.Entity;
import lombok.Getter;

public abstract class Notification extends Entity<Long> {
    @Getter
    private final NotificationType type;

    public Notification(long id, NotificationType type) {
        super(id);
        this.type = type;
    }
}
