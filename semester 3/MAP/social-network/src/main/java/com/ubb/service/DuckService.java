package com.ubb.service;

import com.ubb.config.Config;
import com.ubb.domain.duck.Duck;
import com.ubb.repo.database.DuckDBRepo;

public class DuckService extends DBService<Duck> {

    public DuckService() {
        super(
                new DuckDBRepo(
                        Config.getProperties().getProperty("DB_URL"),
                        Config.getProperties().getProperty("DB_USER"),
                        Config.getProperties().getProperty("DB_PASSWORD")
                ),
                Duck::fromString
        );
    }
}
