package com.ubb.dto;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class FlockFilterDTO implements ObjectFilterDTO {
    Optional<Integer> id = Optional.empty();
    Optional<String> name = Optional.empty();

    @Override
    public Pair<String, List<Object>> toSql() {
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        id.ifPresent(v -> {
            conditions.add("id = ?");
            params.add(v);
        });

        name.ifPresent(v -> {
            conditions.add("name LIKE ?");
            params.add("%" + v + "%");
        });

        String sql = String.join(" AND ", conditions);
        return new Pair<>(sql, params);
    }
}
