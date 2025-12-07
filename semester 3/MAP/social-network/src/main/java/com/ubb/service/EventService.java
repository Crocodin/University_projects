package com.ubb.service;

import com.ubb.config.Config;
import com.ubb.domain.event.Event;
import com.ubb.domain.event.RaceEvent;
import com.ubb.domain.user.User;
import com.ubb.exception.EntityException;
import com.ubb.exception.userException.UserExistsException;
import com.ubb.facade.UserFacade;
import com.ubb.repo.database.EventDBRepo;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class EventService extends DBService<Event> {
    LaneService laneService;

    public EventService(UserFacade uf, DuckService ds, LaneService ls) {
        // this is important, the Lane file must be loaded before the Event Repo
        super(
                new EventDBRepo(
                        Config.getProperties().getProperty("DB_URL"),
                        Config.getProperties().getProperty("DB_USER"),
                        Config.getProperties().getProperty("DB_PASSWORD"),
                        uf,
                        ds,
                        ls
                ),
                line -> {
                    Map<Long, User> users = uf.getUsers().stream().collect(Collectors.toMap(User::getId, user -> user));
                    Event newEvent;
                    if (line.split(",")[0].equals("RE")) {
                        newEvent = RaceEvent.fromString(line, users, ds.getObjects(), ls.getObjects());
                    } else {
                        newEvent = Event.fromString(line, users);
                    }
                    return newEvent;
                }
        );
        this.laneService = ls;
    }

    public void addSubscriber(Long eventId, User u) throws SQLException, EntityException {
        Event e = findId(eventId);

        if (e.getSubscribers().contains(u))
            throw new EntityException("User already subscribed!");
        ((EventDBRepo) dbRepo).addSubscriber(eventId, u.getId());

        e.subscribe(u);
    }

    public void removeSubscriber(Long eventId, User u) throws SQLException, EntityException {
        Event e = findId(eventId);

        if (!e.getSubscribers().contains(u))
            throw new EntityException("User is not subscribed to this event!");
        ((EventDBRepo) dbRepo).removeSubscriber(eventId, u.getId());

        e.unsubscribe(u);
    }
}
