package com.ubb.repo.database.notifications;

import com.ubb.domain.notifications.FriendRequest;
import com.ubb.domain.notifications.RequestStatus;
import com.ubb.domain.user.User;
import com.ubb.facade.UserFacade;
import com.ubb.repo.database.DBRepoInt;
import lombok.RequiredArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class FriendRequestDBRepo implements DBRepoInt<Long, FriendRequest> {

    private final String url;
    private final String username;
    private final String password;
    private final UserFacade userFacade;

    @Override
    public Optional<FriendRequest> add(FriendRequest obj) throws SQLException {
        try (Connection con = DriverManager.getConnection(url, username, password)) {

            // create base notification
            var notifStmt = con.prepareStatement(
                    "INSERT INTO notification DEFAULT VALUES RETURNING id"
            );
            var rs = notifStmt.executeQuery();
            rs.next();
            long id = rs.getLong("id");

            // create friend request
            var frStmt = con.prepareStatement("INSERT INTO friend_request(id, user_to_id, user_from_id, status) VALUES (?, ?, ?, ?)");

            frStmt.setLong(1, id);
            frStmt.setLong(2, obj.getTo().getId());
            frStmt.setLong(3, obj.getFrom().getId());
            frStmt.setString(4, obj.getStatus().name());

            frStmt.executeUpdate();

            obj.setId(id);
            return Optional.of(obj);
        }
    }

    @Override
    public Optional<FriendRequest> remove(FriendRequest obj) throws SQLException {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            var stmt = con.prepareStatement(
                    "DELETE FROM notification WHERE id = ?"
            );
            stmt.setLong(1, obj.getId());

            return stmt.executeUpdate() > 0 ? Optional.of(obj) : Optional.empty();
        }
    }

    @Override
    public List<FriendRequest> getObjects() throws SQLException {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            var stmt = con.prepareStatement("SELECT * FROM friend_request");
            var rs = stmt.executeQuery();

            List<FriendRequest> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    @Override
    public Optional<FriendRequest> findId(Long id) throws SQLException {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            var stmt = con.prepareStatement(
                    "SELECT * FROM friend_request WHERE id = ?"
            );
            stmt.setLong(1, id);

            var rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(map(rs));
            }
            return Optional.empty();
        }
    }

    private FriendRequest map(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        User from = userFacade.getUser(rs.getLong("user_from_id"));
        User to = userFacade.getUser(rs.getLong("user_to_id"));
        RequestStatus status = RequestStatus.valueOf(rs.getString("status"));

        return new FriendRequest(id, from, to, status);
    }

    public List<FriendRequest> findForUser(Long userId) throws SQLException {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            var stmt = con.prepareStatement("SELECT * FROM friend_request WHERE user_to_id = ?");
            stmt.setLong(1, userId);

            var rs = stmt.executeQuery();
            List<FriendRequest> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    public void updateStatus(Long id, RequestStatus status) throws SQLException {
        try (Connection con = DriverManager.getConnection(url, username, password)) {
            var stmt = con.prepareStatement(
                    "UPDATE friend_request SET status = ? WHERE id = ?"
            );
            stmt.setString(1, status.name());
            stmt.setLong(2, id);
            stmt.executeUpdate();
        }
    }
}
