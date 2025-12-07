package com.ubb.dto;

import com.ubb.domain.duck.RaceType;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class DuckFilterDTO implements ObjectFilterDTO {
    // from user table
    Optional<Integer> id = Optional.empty();
    Optional<String> username = Optional.empty();
    Optional<String> email = Optional.empty();
    Optional<String> password = Optional.empty();

    // from duck table
    Optional<RaceType> type = Optional.empty();
    Optional<Integer> speed = Optional.empty();
    Optional<Integer> endurance = Optional.empty();

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

        type.ifPresent(v -> {
            conditions.add("type = ?");
            params.add(v.name());
        });

        speed.ifPresent(v -> {
            conditions.add("speed = ?");
            params.add(v);
        });

        endurance.ifPresent(v -> {
            conditions.add("endurance = ?");
            params.add(v);
        });

        String sql = String.join(" AND ", conditions);
        return new Pair<>(sql, params);
    }
}//ati
