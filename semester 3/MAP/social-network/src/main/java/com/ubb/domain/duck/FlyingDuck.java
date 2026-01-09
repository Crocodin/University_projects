package com.ubb.domain.duck;

import com.ubb.domain.flock.Flock;

public class FlyingDuck extends Duck implements Flying {

    public FlyingDuck(Long id, String username, String email, String password,
                      RaceType type, int speed, int endurance) {
        super(id, username, email, password, type, speed, endurance);
    }

    public FlyingDuck(Long id, String username, String email, String password, byte[] profilePicture,
                      RaceType type, int speed, int endurance) {
        super(id, username, email, password, profilePicture, type, speed, endurance);
    }

    @Override
    public void fly() {

    }
}
