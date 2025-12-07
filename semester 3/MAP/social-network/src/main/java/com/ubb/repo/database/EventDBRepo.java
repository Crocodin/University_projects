package com.ubb.repo.database;

import com.ubb.domain.duck.Duck;
import com.ubb.domain.event.Event;
import com.ubb.domain.event.RaceEvent;
import com.ubb.domain.user.User;
import com.ubb.dto.ObjectFilterDTO;
import com.ubb.facade.UserFacade;
import com.ubb.service.DuckService;
import com.ubb.service.LaneService;
import com.ubb.utils.paging.Pageable;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventDBRepo extends DBRepo<Event> {

    private final UserFacade userFacade;
    private final DuckService duckService;
    private final LaneService laneService;

    public EventDBRepo(String url, String username, String password,
                       UserFacade userFacade,
                       DuckService duckService,
                       LaneService laneService) {
        super(url, username, password, "events");
        this.userFacade = userFacade;
        this.duckService = duckService;
        this.laneService = laneService;
    }

    private Event getEvent(ResultSet rs, Connection connection) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");

        // Load subscribers
        var subsStmt = connection.prepareStatement(
                "SELECT user_id FROM event_subscribers WHERE event_id = ?"
        );
        subsStmt.setLong(1, id);
        var subsRs = subsStmt.executeQuery();

        List<User> subscribers = new ArrayList<>();
        while (subsRs.next()) {
            long userId = subsRs.getLong("user_id");
            subscribers.add(userFacade.getUser(userId));
        }

        var raceStmt = connection.prepareStatement(
                "SELECT event_id FROM race_events WHERE event_id = ?"
        );
        raceStmt.setLong(1, id);
        var raceRs = raceStmt.executeQuery();

        if (raceRs.next()) {
            // RaceEvent
            return new RaceEvent(
                    id,
                    name,
                    subscribers,
                    duckService.getObjects(),
                    laneService.getObjects()
            );
        }

        // Regular Event
        return new Event(id, name, subscribers);
    }

    @Override
    public Optional<Event> findId(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var stmt = connection.prepareStatement("SELECT * FROM events WHERE id = ?");
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(getEvent(rs, connection));
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Event> getObjects() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var stmt = connection.prepareStatement("SELECT * FROM events");
            var rs = stmt.executeQuery();

            List<Event> list = new ArrayList<>();
            while (rs.next()) {
                list.add(getEvent(rs, connection));
            }
            return list;
        }
    }

    // ---------- add (Event and RaceEvent) ----------
    @Override
    public Optional<Event> add(Event obj) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            var stmt = connection.prepareStatement(
                    "INSERT INTO events(name) VALUES (?) RETURNING id"
            );
            stmt.setString(1, obj.getName());
            var rs = stmt.executeQuery();

            if (!rs.next()) return Optional.empty();
            long id = rs.getLong("id");
            obj.setId(id);

            // Insert subscribers
            var subsStmt = connection.prepareStatement(
                    "INSERT INTO event_subscribers(event_id, user_id) VALUES (?, ?)"
            );
            for (User u : obj.getSubscribers()) {
                subsStmt.setLong(1, id);
                subsStmt.setLong(2, u.getId());
                subsStmt.executeUpdate();
            }

            // Insert race-event specific data
            if (obj instanceof RaceEvent) {
                var raceStmt = connection.prepareStatement(
                        "INSERT INTO race_events(event_id) VALUES (?)"
                );
                raceStmt.setLong(1, id);
                raceStmt.executeUpdate();
            }

            obj.setId(id);
            return Optional.of(obj);
        }
    }

    public void addSubscriber(long eventId, long userId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var stmt = connection.prepareStatement(
                    "INSERT INTO event_subscribers(event_id, user_id) VALUES (?, ?) ON CONFLICT DO NOTHING"
            );
            stmt.setLong(1, eventId);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        }
    }

    public void removeSubscriber(long eventId, long userId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var stmt = connection.prepareStatement(
                    "DELETE FROM event_subscribers WHERE event_id = ? AND user_id = ?"
            );
            stmt.setLong(1, eventId);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        }
    }

    @Override
    protected List<Event> findAllOnPage(Connection connection, Pageable pageable, ObjectFilterDTO filter) throws SQLException {
        List<Event> eventOnPage = new ArrayList<>();
        // Using StringBuilder rather than "+" operator for concatenating Strings is more performant
        // since Strings are immutable, so every operation applied on a String will create a new String
        String sql = "select * from " + tableName;

        Pair<String, List<Object>> sqlFilter;
        if (filter != null) {
            sqlFilter = filter.toSql();
        }
        else sqlFilter = new Pair<>("", List.of());

        if (!sqlFilter.getKey().isEmpty()) {
            sql += " where " + sqlFilter.getKey();
        }
        sql += " limit ? offset ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 0;
            for (Object param : sqlFilter.getValue()) {
                statement.setObject(++paramIndex, param);
            }
            statement.setInt(++paramIndex, pageable.getPageSize());
            statement.setInt(++paramIndex, pageable.getPageSize() * pageable.getPageNumber());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Event event = getEvent(resultSet, connection);
                    eventOnPage.add(event);
                }
            }
        }
        return eventOnPage;
    }
}

