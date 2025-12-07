package com.ubb.repo.database;

import com.ubb.domain.Entity;
import com.ubb.domain.duck.Duck;
import com.ubb.utils.paging.Page;
import com.ubb.utils.paging.Pageable;

import java.sql.SQLException;
import java.util.Optional;

public interface PagedRepo<ID, E extends Entity<ID>> extends DBRepoInt<ID, E> {
    Page<E> findAllOnPage(Pageable pageable);
}
