package com.ubb.service.notifications;

import com.ubb.config.Config;
import com.ubb.domain.notifications.EventNotification;
import com.ubb.facade.UserFacade;
import com.ubb.repo.database.notifications.EventNotificationDBRepo;
import com.ubb.service.EventService;

import java.sql.SQLException;
import java.util.List;

public class EventNotificationService {
    public final EventNotificationDBRepo repository;

    public EventNotificationService(UserFacade uf, EventService es) {
        this.repository = new EventNotificationDBRepo(
                Config.getProperties().getProperty("DB_URL"),
                Config.getProperties().getProperty("DB_USER"),
                Config.getProperties().getProperty("DB_PASSWORD"),
                uf, es
        );
    }

    public List<EventNotification> getForUser(Long userId) throws SQLException {
        return repository.findForUser(userId);
    }

    public void remove(EventNotification notification) throws SQLException {
        repository.remove(notification);
    }
}
