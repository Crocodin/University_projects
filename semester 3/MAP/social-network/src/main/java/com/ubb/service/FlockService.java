package com.ubb.service;

import com.ubb.config.Config;
import com.ubb.domain.flock.Flock;
import com.ubb.exception.EntityException;
import com.ubb.exception.flockException.FlockException;
import com.ubb.exception.userException.UserException;
import com.ubb.repo.database.FlockDBRepo;

import java.sql.SQLException;

public class FlockService extends DBService<Flock> {

    public FlockService(DuckService duckService) {
        super(
            new FlockDBRepo(
                    Config.getProperties().getProperty("DB_URL"),
                    Config.getProperties().getProperty("DB_USER"),
                    Config.getProperties().getProperty("DB_PASSWORD"),
                    duckService
            ),
            string -> { return Flock.fromString(string, duckService.getObjects()); }
        );
    }

    public void addToFlock(Long idF, Long idD) throws FlockException, UserException {
        try {
            ((FlockDBRepo) dbRepo).addDuck(idF, idD);
        } catch (EntityException | SQLException e) { throw new UserException("The ID " + idD + " is not a Duck ID"); }
    }

    public void removeFromFlock(Long idF,  Long idD) throws FlockException, UserException {
        try {
            ((FlockDBRepo) dbRepo).removeDuck(idF, idD);
        } catch (EntityException | SQLException e) { throw new UserException("The ID " + idD + " is not a Duck ID"); }
    }
}
