package com.ubb.repo.database;

import com.ubb.domain.lane.Lane;
import com.ubb.dto.ObjectFilterDTO;
import com.ubb.utils.paging.Pageable;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LaneDBRepo extends DBRepo<Lane> {

    public LaneDBRepo(String url, String username, String password) {
        super(url, username, password, "lane");
    }

    @Override
    public Optional<Lane> findId(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement(
                    "SELECT * FROM lane WHERE id = ?"
            );
            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of(getLane(rs));
            }
            return Optional.empty();
        }
    }

    private Lane getLane(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        int distance = rs.getInt("distance");
        return new Lane(id, distance);
    }

    @Override
    public List<Lane> getObjects() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var query = connection.prepareStatement("SELECT * FROM lane");
            var rs = query.executeQuery();

            List<Lane> lanes = new ArrayList<>();
            while (rs.next()) lanes.add(getLane(rs));

            return lanes;
        }
    }

    @Override
    public Optional<Lane> add(Lane obj) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var statement = connection.prepareStatement(
                    "INSERT INTO lane(distance) VALUES (?) RETURNING id"
            );
            statement.setInt(1, obj.getDistance());

            var rs = statement.executeQuery();
            if (rs.next()) {
                obj.setId(rs.getLong("id"));
                return Optional.of(obj);
            }
            return Optional.empty();
        }
    }

    @Override
    protected List<Lane> findAllOnPage(Connection connection, Pageable pageable, ObjectFilterDTO filter) throws SQLException {
        List<Lane> laneOnPage = new ArrayList<>();
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
                    Lane lane = getLane(resultSet);
                    laneOnPage.add(lane);
                }
            }
        }
        return laneOnPage;
    }
}

