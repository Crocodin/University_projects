package com.ubb.service;

import com.ubb.domain.Entity;
import com.ubb.exception.EntityException;
import com.ubb.repo.file.FileRepo;

import java.util.List;
import java.util.function.Function;

/**
 * Provides a generic implementation of the {@link Service} interface
 * for managing entities that extend the {@link Entity} class.
 * <p>
 * The {@code EntityService} handles basic CRUD operations such as adding,
 * removing, finding, and saving entities using a {@link FileRepo} as the
 * underlying persistence mechanism. It relies on a conversion function
 * to transform textual data into entity objects.
 * </p>
 *
 * @param <ID> the type of the entity's unique identifier
 * @param <E>  the type of entity managed by this service
 */
public class EntityService<ID, E extends Entity<ID>> implements Service<ID, E> {
    /** The repository responsible for storing and managing entities. */
    protected FileRepo<ID, E> fileRepo;
    /** Function that converts a string representation into an entity instance. */
    protected Function<String, E> fromStringFunction;

    /**
     * Constructs a new {@code EntityService} with the specified file path
     * and string-to-entity conversion function.
     *
     * @param path              the file path used by the repository for persistence
     * @param fromStringFunction the function used to convert text data into entity instances
     */
    public EntityService(String path, Function<String, E> fromStringFunction) {
        this.fileRepo = new FileRepo<>(path, fromStringFunction);
        this.fromStringFunction = fromStringFunction;
        fileRepo.loadFromFile();
    }

    @Override
    public void addObject(String data) {
        E object = fromStringFunction.apply(data);
        fileRepo.add(object);
    }

    @Override
    public void removeObject(E data) {
        fileRepo.remove(data);
    }

    @Override
    public List<E> getObjects() {
        return fileRepo.getObjects();
    }

    @Override
    public void saveObjects() {
        fileRepo.saveToFile();
    }

    /**
     * Finds an entity by its unique identifier. <br>
     * The search is performed linearly through the list of managed entities.
     *
     * @param id the unique identifier of the entity
     * @return the entity with the specified ID
     * @throws EntityException if no entity with the given ID exists
     */
    @Override
    public E findId(ID id) throws EntityException {
        return fileRepo.findId(id);
    }
}
