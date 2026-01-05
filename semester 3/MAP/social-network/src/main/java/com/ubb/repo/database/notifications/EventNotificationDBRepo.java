package com.ubb.repo.database.notifications;

import com.ubb.domain.notifications.EventNotification;
import com.ubb.domain.event.Event;
import com.ubb.domain.user.User;
import com.ubb.facade.UserFacade;
import com.ubb.service.EventService;
import com.ubb.repo.database.DBRepoInt;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EventNotificationDBRepo implements DBRepoInt<Long, EventNotification> {

    private final String url;
    private final String username;
    private final String password;

    private final UserFacade userFacade;
    private final EventService eventService;

    @Override
    public Optional<EventNotification> add(EventNotification obj) throws SQLException {
        try (Connection con = DriverManager.getConnection(url, username, password)) {

            // base notification
            var notifStmt = con.prepareStatement("INSERT INTO notification DEFAULT VALUES RETURNING id");
            var rs = notifStmt.executeQuery();
            rs.next();
            long id = rs.getLong("id");

            // event notification
            var evStmt = con.prepareStatement("INSERT INTO event_notification(id, event_id, user_id, status) VALUES (?, ?, ?, ?)");

            evStmt.setLong(1, id);
            evStmt.setLong(2, obj.getFrom().getId());
            evStmt.setLong(3, obj.getTo().getId());
            evStmt.setString(4, obj.getStatus().toString());

            evStmt.executeUpdate();

            obj.setId(id);
            return Optional.of(obj);
        }
    }

    @Override
    public Optional<EventNotification> remove(EventNotification obj) throws SQLException {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            var stmt = con.prepareStatement(
                    "DELETE FROM notification WHERE id = ?"
            );
            stmt.setLong(1, obj.getId());

            return stmt.executeUpdate() > 0 ? Optional.of(obj) : Optional.empty();
        }
    }

    @Override
    public List<EventNotification> getObjects() throws SQLException {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            var stmt = con.prepareStatement("SELECT * FROM event_notification");
            var rs = stmt.executeQuery();

            List<EventNotification> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    @Override
    public Optional<EventNotification> findId(Long id) throws SQLException {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            var stmt = con.prepareStatement(
                    "SELECT * FROM event_notification WHERE id = ?"
            );
            stmt.setLong(1, id);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(map(rs));
            }
            return Optional.empty();
        }
    }

    private EventNotification map(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");

        Event event = eventService.findId(rs.getLong("event_id"));
        User user = userFacade.getUser(rs.getLong("user_id"));
        String status = rs.getString("status");

        return new EventNotification(id, event, user, status);
    }

    public List<EventNotification> findForUser(Long userId) throws SQLException {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            var stmt = con.prepareStatement("SELECT * FROM event_notification WHERE user_id = ?");
            stmt.setLong(1, userId);

            var rs = stmt.executeQuery();
            List<EventNotification> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }
}
