package com.ubb.repo.database;

import com.ubb.domain.Entity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DBRepoInt<ID, E extends Entity<ID>> {
    /**
     * Adds an object to the repository.
     * @param obj the object to add
     */
    public Optional<E> add(E obj) throws SQLException;

    /**
     * Removes an object from the repository.
     * @param obj the object to remove
     */
    public Optional<E> remove(E obj) throws SQLException;

    /**
     * Returns the list of objects in the repository.
     * @return the list of stored objects
     */
    public List<E> getObjects() throws SQLException;

    /**
     *  Returns a object via id
     * @param id the id of the object
     */
    public Optional<E> findId(ID id) throws SQLException;
}
