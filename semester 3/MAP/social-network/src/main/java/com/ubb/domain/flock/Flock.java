package com.ubb.domain.flock;

import com.ubb.domain.Entity;
import com.ubb.domain.duck.Duck;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class Flock extends Entity<Long> {
    private final String name;
    private final List<Duck>  ducks;

    public Flock(Long id, String name, List<Duck> ducks) {
        super(id);
        this.name = name;
        this.ducks = ducks;
    }

    public PerformanceAvg getPerformanceAvg() {
        int avgSpeed = (int) ducks.stream().mapToInt(Duck::getSpeed).average().orElse(0);
        int avgEndurance = (int) ducks.stream().mapToInt(Duck::getEndurance).average().orElse(0);
        return new PerformanceAvg(avgSpeed, avgEndurance);
    }

    public void addDuck(Duck duck) {
        ducks.add(duck);
    }

    public void removeDuck(Duck duck) { ducks.remove(duck); }

    @Override
    public String toString() {
        String ducksId = ducks.stream()
                .map(u -> String.valueOf(u.getId()))
                .collect(Collectors.joining("|"));
        return super.getId().toString() + "," + name + "," + ducksId;
    }

    public static Flock fromString(String line, List<Duck> duckList) {
        // Expected format: <id>,<name>,<id1>|<id2>|<id3>
        String[] parts = line.split(",", 4);
        Map<Long, Duck> map = duckList.stream().collect(Collectors.toMap(Duck::getId, d -> d));

        Long id = Long.parseLong(parts[0]);
        String name = parts[1];

        List<Duck> auxDucks = new ArrayList<>();
        if (parts.length == 3 && !parts[2].isEmpty()) {
            for (String idStr : parts[2].split("\\|")) {
                long userId = Long.parseLong(idStr);
                Duck d = map.get(userId);
                if (d != null) auxDucks.add(d);
            }
        }

        return new Flock(id, name, auxDucks);
    }
}
