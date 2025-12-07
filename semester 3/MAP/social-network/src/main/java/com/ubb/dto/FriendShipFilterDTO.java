package com.ubb.dto;

import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
public class FriendShipFilterDTO implements ObjectFilterDTO {
    Optional<Integer> id = Optional.empty();
    Optional<Integer> userOneId = Optional.empty();
    Optional<Integer> userTwoId = Optional.empty();

    @Override
    public Pair<String, List<Object>> toSql() {
        List<String> conditions = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        id.ifPresent(v -> {
            conditions.add("id = ?");
            params.add(v);
        });

        userOneId.ifPresent(v -> {
            conditions.add("user_one_id = ?");
            params.add(v);
        });

        userTwoId.ifPresent(v -> {
            conditions.add("user_two_id = ?");
            params.add(v);
        });

        String sql = String.join(" AND ", conditions);
        return new Pair<>(sql, params);
    }
}
