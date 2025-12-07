package com.ubb.service;

import com.ubb.config.Config;
import com.ubb.domain.lane.Lane;
import com.ubb.repo.database.LaneDBRepo;

public class LaneService extends DBService<Lane> {
    public LaneService() {
        super(
                new LaneDBRepo(
                        Config.getProperties().getProperty("DB_URL"),
                        Config.getProperties().getProperty("DB_USER"),
                        Config.getProperties().getProperty("DB_PASSWORD")
                ),
                Lane::fromString
        );
    }
}
