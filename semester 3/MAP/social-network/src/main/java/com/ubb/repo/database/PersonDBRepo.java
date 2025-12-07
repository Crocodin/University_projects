package com.ubb.repo.database;

import com.ubb.domain.person.Person;
import com.ubb.dto.ObjectFilterDTO;
import com.ubb.utils.paging.Pageable;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonDBRepo extends DBRepo<Person> {

    public PersonDBRepo(String url, String username, String password) {
        super(url, username, password, "users JOIN person ON users.id = person.user_id");
    }

    @Override
    public Optional<Person> findId(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement(
                    "SELECT * FROM users JOIN person ON users.id = person.user_id WHERE users.id = ?"
            );
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var user = getPerson(resultSet);
                return Optional.of(user);
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Person> getObjects() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var query = connection.prepareStatement(
                    "SELECT * FROM users JOIN person ON users.id = person.user_id"
            );
            var resultSet = query.executeQuery();
            List<Person> persons = new ArrayList<>();
            while (resultSet.next()) {
                var obj = getPerson(resultSet);
                persons.add(obj);
            }
            return persons;
        }
    }

    private Person getPerson(ResultSet resultSet) throws SQLException {
        // from user table
        var id = resultSet.getLong("id");
        var username = resultSet.getString("username");
        var email = resultSet.getString("email");
        var password = resultSet.getString("password");

        // from person table
        var firstname = resultSet.getString("first_name");
        var lastname = resultSet.getString("last_name");
        var occupation = resultSet.getString("occupation");
        var empathyScore = resultSet.getInt("empathy_score");

        return new Person(id, username,  email, password, firstname, lastname, occupation, empathyScore);
    }

    @Override
    public Optional<Person> add(Person obj) throws SQLException {
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


            String insertPersonSql = "INSERT INTO person (user_id, first_name, last_name, occupation, empathy_score) VALUES (?, ?, ?, ?, ?)";

            try (var personStatement = connection.prepareStatement(insertPersonSql)) {
                personStatement.setLong(1, userId);
                personStatement.setString(2, obj.getFirstName());
                personStatement.setString(3, obj.getLastName());
                personStatement.setString(4, obj.getOccupation());
                personStatement.setInt(5, obj.getEmpathyScore());
                personStatement.executeUpdate();
            }

            // Update the obj id and return it
            obj.setId(userId);
            return Optional.of(obj);
        }
    }

    @Override
    protected List<Person> findAllOnPage(Connection connection, Pageable pageable, ObjectFilterDTO filter) throws SQLException {
        List<Person> personOnPage = new ArrayList<>();
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
                    Person person = getPerson(resultSet);
                    personOnPage.add(person);
                }
            }
        }
        return personOnPage;
    }
}
