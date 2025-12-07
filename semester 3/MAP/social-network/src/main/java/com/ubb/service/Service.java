package com.ubb.service;

import java.util.List;

/**
 * Defines a generic service interface for managing domain entities.
 * <p>
 * The {@code Service} interface provides a standard set of CRUD-like
 * operations for objects of a given type {@code E}. Implementations of
 * this interface are responsible for managing specific entity types.
 * </p>
 *
 * @param <E> the type of entity managed by this service
 */
public interface Service<ID, E> {

    /**
     * Adds a new object to the service using the provided data.
     * <br>
     * The data format and parsing logic are defined by the concrete implementation.
     * @param data a {@link String} representation of the entity to add
     */
    void addObject(String data);

    /**
     * Removes the specified object from the service.
     * @param data the entity instance to remove
     */
    void removeObject(E data);

    /**
     * Retrieves all objects managed by this service.
     * @return a list containing all managed entities
     */
    List<E> getObjects();

    /**
     * Persists all managed objects to the underlying storage. <br>
     * Implementations should define how data is saved (e.g., to a file, database, etc.).
     */
    void saveObjects();

    /**
     * Finds an entity by its unique identifier.
     *
     * @param id the unique identifier of the entity
     * @return the entity with the specified ID, or {@code null} if not found
     */
    E findId(ID id);
}
