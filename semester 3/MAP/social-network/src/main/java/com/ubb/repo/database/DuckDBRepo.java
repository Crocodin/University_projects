package com.ubb.repo.database;

import com.ubb.domain.duck.*;
import com.ubb.dto.ObjectFilterDTO;
import com.ubb.utils.paging.Pageable;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DuckDBRepo extends DBRepo<Duck> {

    public DuckDBRepo(String url, String username, String password) {
        super(url, username, password, "users JOIN duck ON users.id = duck.user_id");
    }

    @Override
    public Optional<Duck> remove(Duck obj) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var sql = "DELETE FROM users WHERE id = ?";
            var statement = connection.prepareStatement(sql);
            statement.setLong(1, obj.getId());
            return statement.executeUpdate() > 0 ? Optional.of(obj) : Optional.empty();
        }
    }

    @Override
    public Optional<Duck> findId(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement(
                    "SELECT * FROM users JOIN duck ON users.id = duck.user_id WHERE users.id = ?"
            );
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var duck = getDuck(resultSet);
                return Optional.of(duck);
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Duck> getObjects() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var query = connection.prepareStatement(
                    "SELECT * FROM users JOIN duck ON users.id = duck.user_id"
            );
            var resultSet = query.executeQuery();
            List<Duck> ducks = new ArrayList<>();
            while (resultSet.next()) {
                var obj = getDuck(resultSet);
                ducks.add(obj);
            }
            return ducks;
        }
    }

    public static Duck getDuck(ResultSet resultSet) throws SQLException {
        // form user table
        var id = resultSet.getLong("id");
        var username = resultSet.getString("username");
        var email = resultSet.getString("email");
        var password = resultSet.getString("password");
        var profilePicture = resultSet.getBytes("profile_picture");

        // from duck table
        var type = RaceType.valueOf(resultSet.getString("type"));
        var speed = resultSet.getInt("speed");
        var endurance = resultSet.getInt("endurance");

        return switch (type) {
            case FLYING -> new FlyingDuck(id, username, email, password, profilePicture, type, speed, endurance);
            case SWIMMING -> new SwimingDuck(id, username, email, password, profilePicture, type, speed, endurance);
            case FLYING_AND_SWIMMING -> new FlyingAndSwimmingDuck(id, username, email, password, profilePicture, type, speed, endurance);
        };
    }

    @Override
    public Optional<Duck> add(Duck obj) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            String insertUserSql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?) RETURNING id";

            long userId;

            try (var userStatement = connection.prepareStatement(insertUserSql)) {
                userStatement.setString(1, obj.getUsername());
                userStatement.setString(2, obj.getEmail());
                userStatement.setString(3, obj.getPassword());

                try (var rs = userStatement.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getLong(1); // get generated ID
                    } else {
                        return Optional.empty();
                    }
                }
            }


            String insertDuckSql = "INSERT INTO duck (user_id, type, speed, endurance) VALUES (?, ?, ?, ?)";

            try (var duckStatement = connection.prepareStatement(insertDuckSql)) {
                duckStatement.setLong(1, userId);
                duckStatement.setString(2, obj.getType().toString());
                duckStatement.setInt(3, obj.getSpeed());
                duckStatement.setInt(4, obj.getEndurance());
                duckStatement.executeUpdate();
            }

            // Update the obj id and return it
            obj.setId(userId);
            return Optional.of(obj);
        }
    }

    @Override
    protected List<Duck> findAllOnPage(Connection connection, Pageable pageable, ObjectFilterDTO filter) throws SQLException {
        List<Duck> ducksOnPage = new ArrayList<>();
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
                    Duck duck = getDuck(resultSet);
                    ducksOnPage.add(duck);
                }
            }
        }
        return ducksOnPage;
    }
}
