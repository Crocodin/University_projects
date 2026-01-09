package com.ubb.domain.event;

import com.ubb.domain.duck.Duck;
import com.ubb.domain.duck.RaceType;
import com.ubb.domain.lane.Lane;
import com.ubb.domain.user.User;
import com.ubb.utils.algorithms.BinarySearch;
import com.ubb.utils.algorithms.TimeAlgorithm;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a race event specifically for ducks.
 * <p>
 * A {@code RaceEvent} extends {@link Event} and adds a list of participating
 * {@link Duck} instances. Users subscribed to this event can be notified
 * when the race starts or updates occur.
 * </p>
 */
@Getter
public class RaceEvent extends Event {
    TimeAlgorithm timeAlgorithm = new BinarySearch();

    /** The list of ducks participating in this race. */
    private List<Duck> ducks;
    private final List<Lane> lanes;
    private double time;

    public RaceEvent(Long id, String name, List<User> subscribers, List<Lane> lanes) {
        super(id, name, subscribers);
        this.lanes = lanes;
    }

    public RaceEvent(Long id, String name, List<User> subscribers, List<Duck> ducks, List<Lane> lanes) {
        super(id, name, subscribers);
        this.lanes = lanes;
        this.ducks = ducks;
    }

    @Override
    public String toString() {
        return super.toString().replaceFirst("^E,", "RE,");
    }

    public static RaceEvent fromString(String line, Map<Long, User> allUsers, List<Lane> lanes) {
        if (!line.startsWith("RE,")) return null;

        String[] parts = line.split(",");
        long id = Long.parseLong(parts[1]);
        String name = parts[2];
        String subscriberIds = parts.length > 3 ? parts[3] : "";

        List<User> subs = new ArrayList<>();
        for (String s : subscriberIds.split("\\|")) {
            if (s.isEmpty()) continue;
            User u = allUsers.get(Long.parseLong(s));
            if (u != null) subs.add(u);
        }

        return new RaceEvent(id, name, subs, lanes);
    }

    public void startEvent(List<Duck> ducks) {
        this.time = timeAlgorithm.findBestTime(
                ducks.stream().filter(
                        duck -> duck.getType().equals(RaceType.FLYING_AND_SWIMMING) ||
                                duck.getType().equals(RaceType.SWIMMING)
                ).toList(),
                lanes
        );
        this.ducks = timeAlgorithm.getDucks();
    }
}

