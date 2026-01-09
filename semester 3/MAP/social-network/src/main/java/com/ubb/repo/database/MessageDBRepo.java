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
import java.util.OptionalLong;

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
                    "INSERT INTO message(user_from, message, timestamp) " +
                            "VALUES (?, ?, ?) RETURNING id"
            );

            stmt.setLong(1, obj.getFrom().getId());
            stmt.setString(2, obj.getMessage());
            stmt.setTimestamp(3, Timestamp.valueOf(obj.getTimestamp()));

            var rs = stmt.executeQuery();
            if (!rs.next()) return Optional.empty();

            long id = rs.getLong("id");
            obj.setId(id);

            if (obj.getTo() != null) {
                var toStmt = connection.prepareStatement(
                        "INSERT INTO message_user(message_id, user_from, user_to) VALUES (?, ?, ?)"
                );

                for (User recipient : obj.getTo()) {
                    toStmt.setLong(1, id);
                    toStmt.setLong(2, obj.getFrom().getId());
                    toStmt.setLong(3, recipient.getId());
                    toStmt.addBatch();
                }

                toStmt.executeBatch();
            }

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
        String text = rs.getString("message");

        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();

        User from = userFacade.getUser(fromId);

        // -- getting the users 'to' list
        List<User> recipients = new ArrayList<>();
        PreparedStatement toStmt = connection.prepareStatement(
                "SELECT user_to FROM message_user WHERE message_id = ?"
        );
        toStmt.setLong(1, id);
        ResultSet toRs = toStmt.executeQuery();

        while (toRs.next()) {
            long userId = toRs.getLong("user_to");
            recipients.add(userFacade.getUser(userId));
        }

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

        return new Message(id, from, recipients, text, timestamp, replyTo);
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
        String sql = """
            SELECT m.* FROM message m \
            JOIN message_user mu ON m.id = mu.message_id \
            WHERE (m.user_from = ? AND mu.user_to = ?) \
               OR (m.user_from = ? AND mu.user_to = ?) \
            ORDER BY m.timestamp""";

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

    public long getNumberOfMessages(long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM message WHERE user_from = ?";

        try (Connection con = DriverManager.getConnection(url, username, password);
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getLong(1);
        }
    }

}
