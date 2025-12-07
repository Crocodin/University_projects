package com.ubb.repo.file;

import java.util.List;

public interface RepoInt<ID, E> {
    /**
     * Adds an object to the repository.
     * @param obj the object to add
     */
    public void add(E obj);

    /**
     * Removes an object from the repository.
     *
     * @param obj the object to remove
     */
    public void remove(E obj);

    /**
     * Returns the list of objects in the repository.
     * @return the list of stored objects
     */
    public List<E> getObjects();

    /**
     * Loads objects from the file into the repository.
     */
    public void loadFromFile();

    /**
     * Saves the objects in the repository to the file.
     * <br>
     * Each object is written as a line using its {@link Object#toString()} representation.
     */
    public void saveToFile();

    /**
     *  Returns a object via id
     * @param id the id of the object
     */
    public E findId(ID id);
}
