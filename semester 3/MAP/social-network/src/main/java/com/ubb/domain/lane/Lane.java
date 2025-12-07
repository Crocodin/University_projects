package com.ubb.domain.lane;

import com.ubb.domain.Entity;
import lombok.Getter;

@Getter
public class Lane extends Entity<Long> {
    private final int distance;

    public Lane(Long id, int distance) {
        super(id);
        this.distance = distance;
    }

    @Override
    public String toString() {
        return super.getId() + "," + distance;
    }

    public static Lane fromString(String line) {
        String[] parts = line.split(",");
        if  (parts.length != 2) return null;
        return new Lane(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
    }
}
