package com.ubb.repo.database;

import com.ubb.domain.connection.Message;
import com.ubb.domain.user.User;
import com.ubb.facade.UserFacade;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageDBRepo implements DBRepoInt<Long, Message> {
    protected final String url;
    protected final String username;
    protected final String password;
    protected final UserFacade userFacade;

    public MessageDBRepo(String url, String username, String password,  UserFacade userFacade) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userFacade = userFacade;
    }

    @Override
    public Optional<Message> add(Message obj) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            var stmt = connection.prepareStatement(
                    "INSERT INTO message(user_from, user_to, message, timestamp) " +
                            "VALUES (?, ?, ?, ?) RETURNING id"
            );

            stmt.setLong(1, obj.getFrom().getId());
            stmt.setLong(2, obj.getTo().getId());
            stmt.setString(3, obj.getMessage());
            stmt.setTimestamp(4, Timestamp.valueOf(obj.getTimestamp()));

            var rs = stmt.executeQuery();
            if (!rs.next()) return Optional.empty();

            long id = rs.getLong("id");
            obj.setId(id);

            if (obj.getReplyTo() != null) {
                var replyStmt = connection.prepareStatement(
                        "INSERT INTO reply_message(id, reply_to) VALUES (?, ?)"
                );

                replyStmt.setLong(1, id);
                replyStmt.setLong(2, obj.getReplyTo().getId());
                replyStmt.executeUpdate();
            }

            return Optional.of(obj);
        }
    }

    @Override
    public Optional<Message> remove(Message obj) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var stmt = connection.prepareStatement("DELETE FROM events WHERE id = ?");
            stmt.setLong(1, obj.getId());
            return stmt.executeUpdate() > 0 ? Optional.of(obj) : Optional.empty();
        }
    }

    private Message getMessage(ResultSet rs, Connection connection) throws SQLException {
        long id = rs.getLong("id");
        long fromId = rs.getLong("user_from");
        long toId = rs.getLong("user_to");
        String text = rs.getString("message");

        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();

        User from = userFacade.getUser(fromId);
        User to = userFacade.getUser(toId);

        // Check if it is a reply
        var replyStmt = connection.prepareStatement(
                "SELECT reply_to FROM reply_message WHERE id = ?"
        );
        replyStmt.setLong(1, id);
        var replyRs = replyStmt.executeQuery();

        Message replyTo = null;
        if (replyRs.next()) {
            long replyToId = replyRs.getLong("reply_to");
            replyTo = findId(replyToId).orElse(null);
        }

        return new Message(id, from, to, text, timestamp, replyTo);
    }

    public List<Message> getObjects() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            var stmt = connection.prepareStatement("SELECT * FROM message ORDER BY timestamp");
            var rs = stmt.executeQuery();

            List<Message> list = new ArrayList<>();
            while (rs.next()) {
                list.add(getMessage(rs, connection));
            }
            return list;
        }
    }

    @Override
    public Optional<Message> findId(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var stmt = connection.prepareStatement("SELECT * FROM message WHERE id = ?");
            stmt.setLong(1, id);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(getMessage(rs, connection));
            }
            return Optional.empty();
        }
    }

    public List<Message> getMessagesBetweenUsers(Long userId1, Long userId2) {
        String sql = "SELECT * FROM message WHERE (user_from = ? AND user_to = ?) OR (user_from = ? AND user_to = ?) ORDER BY timestamp";
        List<Message> messages = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, userId1);
            stmt.setLong(2, userId2);
            stmt.setLong(3, userId2);
            stmt.setLong(4, userId1);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Message message = getMessage(rs, connection);
                messages.add(message);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving messages: " + e.getMessage(), e);
        }
        return messages;
    }
}
