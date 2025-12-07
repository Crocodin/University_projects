package com.ubb.domain.duck;

import com.ubb.domain.flock.Flock;

public class SwimingDuck extends Duck implements Swimming {

    public SwimingDuck(Long id, String username, String email, String password,
                        RaceType type, int speed, int endurance) {
        super(id, username, email, password, type, speed, endurance);
    }

    @Override
    public void swim() {

    }
}
