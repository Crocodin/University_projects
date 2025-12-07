package com.ubb.utils.algorithms;

import com.ubb.domain.duck.Duck;
import com.ubb.domain.lane.Lane;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BinarySearch implements TimeAlgorithm {

    private static final int MAX_ITERATIONS = 1000;
    private List<Duck> assignedDucks = new ArrayList<>();

    @Override
    public List<Duck> getDucks() {
        return assignedDucks;
    }

    @Override
    public double findBestTime(List<Duck> ducks, List<Lane> lanes) {
        if (lanes == null || lanes.isEmpty()) return -1;
        if (ducks == null || ducks.size() < lanes.size()) return -1;

        // sort ducks by endurance, then speed
        List<Duck> ducksSorted = ducks.stream().sorted(Comparator
                .comparingInt(Duck::getEndurance)
                .thenComparingInt(Duck::getSpeed)
        ).toList();

        int minSpeed = Integer.MAX_VALUE;
        int maxDistance = lanes.getLast().getDistance();

        for (Duck d : ducksSorted) {
            if (d.getSpeed() < minSpeed) minSpeed = d.getSpeed();
        }

        double low = 0;
        double high = 2.0 * maxDistance / minSpeed;
        double bestTime = high;

        List<Duck> bestAssignment =  new ArrayList<>();

        for (int i = 0; i < MAX_ITERATIONS; i++) {
            double mid = (low + high) / 2.0;
            List<Duck> current = new ArrayList<>();

            boolean canDo = feasible(mid, ducksSorted, lanes, current);

            if (canDo) {
                bestTime = mid;
                high = mid;
                bestAssignment = current;
            } else low = mid;
        }

        assignedDucks = bestAssignment;

        return bestTime;
    }

    /**
     * this function verifies if the current time can be achieved
     * @param T             time we try to gte
     * @param ducksSorted   the list of ducks that race
     * @param lanes         the list of lanes the ducks race on
     * @param assignment    the {@code ducks} in the position we chose
     * @return              true if the position is achieved
     */
    private boolean feasible(double T, List<Duck> ducksSorted, List<Lane> lanes, List<Duck> assignment) {
        int j = 0;
        for (int i = 0; i < lanes.size(); i++) {
            double requiredSpeed = 2.0 * lanes.get(i).getDistance() / T;

            while (j < ducksSorted.size() && ducksSorted.get(j).getSpeed() < requiredSpeed) {
                j++;
            }

            if (j == ducksSorted.size()) {
                return false;
            }
            assignment.add(ducksSorted.get(j));
            j++;
        }
        return true;
    }
}

