package ro.mpp.repository;

import ro.mpp.domain.Entity;

import java.util.List;
import java.util.Optional;

public interface Repository <ID, T extends Entity<ID>> {
    Optional<T> save(T entity);
    Optional<T> update(T entity);
    void delete(T entity);
    Optional<T> find(ID id);
    List<T> findAll();
}
