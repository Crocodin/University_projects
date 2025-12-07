package com.ubb.repo.database;

import com.ubb.domain.connection.FriendShip;
import com.ubb.domain.user.User;
import com.ubb.dto.ObjectFilterDTO;
import com.ubb.facade.UserFacade;
import com.ubb.utils.paging.Pageable;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendShipDBRepo extends DBRepo<FriendShip> {
    UserFacade userFacade;

    public FriendShipDBRepo(String url, String username, String password, UserFacade userFacade) {
        super(url, username, password, "friendship");
        this.userFacade = userFacade;
    }

    @Override
    public Optional<FriendShip> findId(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement("SELECT * FROM friendship WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var fr = getFriendShip(resultSet);
                return Optional.of(fr);
            }
            return Optional.empty();
        }
    }

    private FriendShip getFriendShip(ResultSet resultSet) throws SQLException {
        User user_one, user_two;
        user_one = userFacade.getUser(resultSet.getLong("user_one"));
        user_two = userFacade.getUser(resultSet.getLong("user_two"));
        var id = resultSet.getLong("id");
        return new FriendShip(id, user_one, user_two);
    }

    @Override
    public List<FriendShip> getObjects() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var query = connection.prepareStatement("SELECT * FROM friendship");
            var resultSet = query.executeQuery();
            List<FriendShip> fr = new ArrayList<>();
            while (resultSet.next()) {
                var obj = getFriendShip(resultSet);
                fr.add(obj);
            }
            return fr;
        }
    }

    @Override
    public Optional<FriendShip> add(FriendShip obj) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement(
                    "INSERT INTO friendship(user_one, user_two) VALUES (?, ?) RETURNING id"
            );
            statement.setLong(1, obj.getUserOne().getId());
            statement.setLong(2, obj.getUserTwo().getId());
            var  result = statement.executeQuery();

            if  (result.next()) {
                obj.setId(result.getLong("id"));
                return Optional.of(obj);
            } else return Optional.empty();
        }
    }

    public Optional<FriendShip> getObjectViaKeys(long id1, long id2) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement(
                    "SELECT * FROM friendship WHERE user_one = ? AND user_two = ?"
            );
            statement.setLong(1, id1);
            statement.setLong(2, id2);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var fr = getFriendShip(resultSet);
                return Optional.of(fr);
            }
            return Optional.empty();
        }
    }

    @Override
    protected List<FriendShip> findAllOnPage(Connection connection, Pageable pageable, ObjectFilterDTO filter) throws SQLException {
        List<FriendShip> friendShipsOnPage = new ArrayList<>();
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
                    FriendShip friendShip = getFriendShip(resultSet);
                    friendShipsOnPage.add(friendShip);
                }
            }
        }
        return friendShipsOnPage;
    }
}
