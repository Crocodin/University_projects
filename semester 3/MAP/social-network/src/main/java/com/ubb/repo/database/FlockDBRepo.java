package com.ubb.repo.database;

import com.ubb.domain.duck.Duck;
import com.ubb.domain.flock.Flock;
import com.ubb.dto.ObjectFilterDTO;
import com.ubb.service.DuckService;
import com.ubb.utils.paging.Pageable;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlockDBRepo extends DBRepo<Flock> {

    private final DuckService duckService; // To load Duck objects by ID

    public FlockDBRepo(String url, String username, String password, DuckService duckService) {
        super(url, username, password, "flock");
        this.duckService = duckService;
    }

    @Override
    public Optional<Flock> findId(Long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Load flock row
            var stmt = connection.prepareStatement("SELECT * FROM flock WHERE id = ?");
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) return Optional.empty();

            Flock flock = getFlockWithoutDucks(rs);

            // Load ducks inside this flock
            flock.getDucks().addAll(getDucksForFlock(connection, id));

            return Optional.of(flock);
        }
    }

    private Flock getFlockWithoutDucks(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Flock(id, name, new ArrayList<>());
    }

    private List<Duck> getDucksForFlock(Connection connection, long flockId) throws SQLException {
        var stmt = connection.prepareStatement(
                "SELECT duck_id FROM flock_duck WHERE flock_id = ?"
        );
        stmt.setLong(1, flockId);

        ResultSet rs = stmt.executeQuery();
        List<Duck> ducks = new ArrayList<>();

        while (rs.next()) {
            long duckId = rs.getLong("duck_id");
            ducks.add(duckService.findId(duckId));
        }

        return ducks;
    }

    @Override
    public List<Flock> getObjects() throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            var stmt = connection.prepareStatement("SELECT * FROM flock");
            ResultSet rs = stmt.executeQuery();

            List<Flock> flocks = new ArrayList<>();

            while (rs.next()) {
                Flock flock = getFlockWithoutDucks(rs);
                flock.getDucks().addAll(getDucksForFlock(connection, flock.getId()));
                flocks.add(flock);
            }

            return flocks;
        }
    }

    @Override
    public Optional<Flock> add(Flock obj) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            // Insert into flock table
            var stmt = connection.prepareStatement(
                    "INSERT INTO flock(name) VALUES (?) RETURNING id"
            );
            stmt.setString(1, obj.getName());

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return Optional.empty();

            long id = rs.getLong("id");
            obj.setId(id);

            // Insert into flock_duck table
            for (Duck d : obj.getDucks()) {
                addDuckToFlock(connection, id, d.getId());
            }

            return Optional.of(obj);
        }
    }

    private void addDuckToFlock(Connection connection, long flockId, long duckId) throws SQLException {
        var stmt = connection.prepareStatement(
                "INSERT INTO flock_duck(flock_id, duck_id) VALUES (?, ?)"
        );
        stmt.setLong(1, flockId);
        stmt.setLong(2, duckId);
        stmt.executeUpdate();
    }

    public void addDuck(Long flockId, Long duckId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            addDuckToFlock(connection, flockId, duckId);
        }
    }

    public void removeDuck(Long flockId, Long duckId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var stmt = connection.prepareStatement(
                    "DELETE FROM flock_duck WHERE flock_id = ? AND duck_id = ?"
            );

            stmt.setLong(1, flockId);
            stmt.setLong(2, duckId);
            stmt.executeUpdate();
        }
    }

    @Override
    protected List<Flock> findAllOnPage(Connection connection, Pageable pageable, ObjectFilterDTO filter) throws SQLException {
        List<Flock> flockOnPage = new ArrayList<>();
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
                    Flock flock = getFlockWithoutDucks(resultSet);
                    flock.getDucks().addAll(getDucksForFlock(connection, flock.getId()));
                    flockOnPage.add(flock);
                }
            }
        }
        return flockOnPage;
    }
}

