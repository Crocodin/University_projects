package com.ubb.service;

import com.ubb.domain.Entity;
import com.ubb.dto.ObjectFilterDTO;
import com.ubb.exception.EntityException;
import com.ubb.repo.database.DBRepo;
import com.ubb.utils.paging.Page;
import com.ubb.utils.paging.Pageable;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class DBService<E extends Entity<Long>> {
    protected DBRepo<E> dbRepo;
    protected Function<String, E> fromStringFunction;

    public DBService(DBRepo<E> dbRepo, Function<String, E> fromStringFunction) {
        this.dbRepo = dbRepo;
        this.fromStringFunction = fromStringFunction;
    }

    public void addObject(String data) throws SQLException {
        E object = fromStringFunction.apply(data);
        dbRepo.add(object);
    }

    public void removeObject(E data) throws SQLException {
        dbRepo.remove(data);
    }

    public List<E> getObjects() {
        try {
            return dbRepo.getObjects();
        } catch (SQLException e) {
            return  Collections.emptyList();
        }
    }

    /**
     * Finds an entity by its unique identifier. <br>
     * The search is performed linearly through the list of managed entities.
     *
     * @param id the unique identifier of the entity
     * @return the entity with the specified ID
     * @throws EntityException if no entity with the given ID exists
     */
    public E findId(long id) throws SQLException, EntityException {
        var optional = dbRepo.findId(id);
        if (optional.isPresent()) return optional.get();
        else throw new EntityException("Object not found");
    }

    public Page<E> findAllOnPage(Pageable pageable) {
        return dbRepo.findAllOnPage(pageable);
    }

    public Page<E> findAllOnPage(Pageable pageable, ObjectFilterDTO filter) {
        return dbRepo.findAllOnPage(pageable, filter);
    }
}
