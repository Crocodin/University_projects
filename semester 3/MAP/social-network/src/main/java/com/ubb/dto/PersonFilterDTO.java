package com.ubb.dto;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class PersonFilterDTO implements ObjectFilterDTO {
    // from user table
    Optional<Integer> id = Optional.empty();
    Optional<String> username = Optional.empty();
    Optional<String> email = Optional.empty();
    Optional<String> password = Optional.empty();

    // from person table
    Optional<String> firstName = Optional.empty();
    Optional<String> lastName = Optional.empty();
    Optional<String> occupation = Optional.empty();
    Optional<Integer> empathyScore = Optional.empty();

    @Override
    public Pair<String, List<Object>> toSql() {
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        id.ifPresent(v -> {
            conditions.add("id = ?");
            params.add(v);
        });

        username.ifPresent(v -> {
            conditions.add("username LIKE ?");
            params.add("%" + v + "%");
        });

        email.ifPresent(v -> {
            conditions.add("email LIKE ?");
            params.add("%" + v + "%");
        });

        password.ifPresent(v -> {
            conditions.add("password LIKE ?");
            params.add("%" + v + "%");
        });

        firstName.ifPresent(v -> {
            conditions.add("first_name LIKE ?");
            params.add("%" + v + "%");
        });

        lastName.ifPresent(v -> {
            conditions.add("last_name LIKE ?");
            params.add("%" + v + "%");
        });

        occupation.ifPresent(v -> {
            conditions.add("occupation LIKE ?");
            params.add("%" + v + "%");
        });

        empathyScore.ifPresent(v -> {
            conditions.add("empathy_score = ?");
            params.add(v);
        });

        String sql = String.join(" AND ", conditions);
        return new Pair<>(sql, params);
    }
}
