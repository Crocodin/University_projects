package com.ubb.repo.database;

import com.ubb.domain.notifications.FriendRequest;
import com.ubb.domain.notifications.RequestStatus;
import com.ubb.domain.user.User;
import com.ubb.facade.UserFacade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendRequestDBRepo implements DBRepoInt<Long, FriendRequest> {

    private final String url;
    private final String username;
    private final String password;
    private final UserFacade userFacade;

    public FriendRequestDBRepo(String url, String username, String password, UserFacade userFacade) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.userFacade = userFacade;
    }

    @Override
    public Optional<FriendRequest> add(FriendRequest obj) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            var stmt = connection.prepareStatement(
                    "INSERT INTO friend_request(user_from, user_to, status) VALUES (?, ?, ?) RETURNING id"
            );

            stmt.setLong(1, obj.getFrom().getId());
            stmt.setLong(2, obj.getTo().getId());
            stmt.setString(3, obj.getStatus().name());

            var rs = stmt.executeQuery();
            if (!rs.next()) return Optional.empty();

            obj.setId(rs.getLong("id"));
            return Optional.of(obj);
        }
    }

    @Override
    public Optional<FriendRequest> remove(FriendRequest obj) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var stmt = connection.prepareStatement(
                    "DELETE FROM friend_request WHERE id = ?"
            );
            stmt.setLong(1, obj.getId());
            return stmt.executeUpdate() > 0 ? Optional.of(obj) : Optional.empty();
        }
    }

    public void updateStatus(Long requestId, RequestStatus status) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var stmt = connection.prepareStatement(
                    "UPDATE friend_request SET status = ? WHERE id = ?"
            );
            stmt.setString(1, status.name());
            stmt.setLong(2, requestId);
            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<FriendRequest> findId(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var stmt = connection.prepareStatement(
                    "SELECT * FROM friend_request WHERE id = ?"
            );
            stmt.setLong(1, id);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(getRequest(rs));
            }
            return Optional.empty();
        }
    }

    @Override
    public List<FriendRequest> getObjects() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var stmt = connection.prepareStatement("SELECT * FROM friend_request");
            var rs = stmt.executeQuery();

            List<FriendRequest> list = new ArrayList<>();
            while (rs.next()) {
                list.add(getRequest(rs));
            }
            return list;
        }
    }

    public List<FriendRequest> getRequestsForUser(Long userId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            var stmt = connection.prepareStatement("""
                SELECT * FROM friend_request
                WHERE user_from = ? OR user_to = ?
                ORDER BY id DESC
            """);

            stmt.setLong(1, userId);
            stmt.setLong(2, userId);

            var rs = stmt.executeQuery();
            List<FriendRequest> list = new ArrayList<>();

            while (rs.next()) {
                list.add(getRequest(rs));
            }
            return list;
        }
    }

    private FriendRequest getRequest(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        long fromId = rs.getLong("user_from_id");
        long toId = rs.getLong("user_to_id");
        RequestStatus status = RequestStatus.valueOf(rs.getString("status"));

        User from = userFacade.getUser(fromId);
        User to = userFacade.getUser(toId);

        return new FriendRequest(id, from, to, status);
    }
}
