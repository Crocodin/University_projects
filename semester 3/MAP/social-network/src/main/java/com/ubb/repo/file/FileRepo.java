package com.ubb.repo.file;

import com.ubb.domain.Entity;
import com.ubb.domain.user.User;
import com.ubb.exception.EntityException;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A generic repository for storing {@link User}-type objects
 * that can be loaded from or saved to a file.
 *
 * @param <E> the type of objects stored in the repository, extending {@link User}
 */
public class FileRepo<ID, E extends Entity<ID>> implements RepoInt<ID, E> {
    /** The file path where the repository data is stored. */
    private final String path;

    /** The in-memory list of objects in the repository.
     * -- GETTER --
     *  Returns the list of objects in the repository.
     */
    @Getter
    protected final List<E> objects = new ArrayList<>();

    /** Function to convert a string from the file into an object of type E.
      * <br> An example can be sean at {@link com.ubb.domain.duck.Duck#fromString(String)}
      */
    private final Function<String, E> fromStringFunction;

    /**
     * Constructs a new {@code FileRepo} with the specified file path
     * and string-to-object conversion function.
     *
     * @param path               the path to the file used for persistence
     * @param fromStringFunction a function to convert a line from the file into an object
     */
    public FileRepo(String path, Function<String, E> fromStringFunction) {
        this.path = path;
        this.fromStringFunction = fromStringFunction;
    }

    /**
     * Adds an object to the repository.
     * @param obj the object to add
     */
    public void add(E obj) {
        objects.add(obj);
    }

    /**
     * Removes an object from the repository.
     * <br> THIS DOESN'T REMOVE THE FRIEND OF THE {@code USER}
     *
     * @param obj the object to remove
     */
    public void remove(E obj) {
        objects.remove(obj);
    }

    /**
     * Loads objects from the file into the repository.
     * <br>
     * Clears the current in-memory list and reads the file line by line,
     * converting each line into an object using {@link #fromStringFunction}.
     * Invalid lines (where conversion returns {@code null}) are skipped.
     */
    public void loadFromFile() {
        objects.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                E e = fromStringFunction.apply(line);
                if (e!=null) {
                    objects.add(e);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the objects in the repository to the file.
     * <br>
     * Each object is written as a line using its {@link Object#toString()} representation.
     */
    public void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (E e : objects) {
                writer.write(e.toString());
                writer.newLine();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public E findId(ID id) throws  EntityException {
        for (E obj : getObjects()) {
            if (obj.getId().equals(id)) return obj;
        }
        throw new EntityException("No object with id " + id);
    }
}
