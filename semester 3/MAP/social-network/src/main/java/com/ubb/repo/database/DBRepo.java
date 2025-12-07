package com.ubb.repo.database;

import com.ubb.domain.Entity;
import com.ubb.domain.duck.Duck;
import com.ubb.utils.paging.Page;
import com.ubb.utils.paging.Pageable;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import com.ubb.dto.ObjectFilterDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class DBRepo<E extends Entity<Long>> implements PagedRepo<Long, E> {
    protected final String url;
    protected final String username;
    protected final String password;

    protected final String tableName;

    @Override
    public Optional<E> remove(E obj) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            var sql = "DELETE FROM " + tableName + " WHERE id = ?";
            var statement = connection.prepareStatement(sql);
            statement.setLong(1, obj.getId());
            return statement.executeUpdate() > 0 ? Optional.of(obj) : Optional.empty();
        }
    }

    @Override
    public Page<E> findAllOnPage(Pageable pageable) {
        return findAllOnPage(pageable, null);
    }

    public Page<E> findAllOnPage(Pageable pageable, ObjectFilterDTO filter) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            int totalNumberOfObjects = count(connection, filter);
            List<E> objOnPage;
            if (totalNumberOfObjects > 0) {
                objOnPage = findAllOnPage(connection, pageable, filter);
            } else {
                objOnPage = new ArrayList<>();
            }
            return new Page<>(objOnPage, totalNumberOfObjects);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected int count(Connection connection, ObjectFilterDTO filter) throws SQLException {
        String sql = "select count(*) as count from " + tableName;

        Pair<String, List<Object>> sqlFilter;
        if (filter != null) {
            sqlFilter = filter.toSql();
        }
        else sqlFilter = new Pair<>("", List.of());

        if (!sqlFilter.getKey().isEmpty()) {
            sql += " where " + sqlFilter.getKey();
        }

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int paramIndex = 0;
            for (Object param : sqlFilter.getValue()) {
                statement.setObject(++paramIndex, param);
            }
            try (ResultSet result = statement.executeQuery()) {
                int totalNumberOfMovies = 0;
                if (result.next()) {
                    totalNumberOfMovies = result.getInt("count");
                }
                return totalNumberOfMovies;
            }
        }
    }

    protected abstract List<E> findAllOnPage(Connection connection, Pageable pageable, ObjectFilterDTO filter) throws SQLException;
}
