package com.ubb.utils.algorithms;

import com.ubb.domain.duck.Duck;
import com.ubb.domain.lane.Lane;

import java.util.List;

public interface TimeAlgorithm {
    /**
     * Calculates the best time for a Race Event
     * @param ducks list of ducks that are in the race
     * @param lanes list of lanes the ducks will race
     */
    public double findBestTime(List<Duck> ducks, List<Lane> lanes);

    /**
     * Get the list that was calculated at {@link TimeAlgorithm#findBestTime(List, List)}
     * @return the list of the {@code ducks}
     */
    public List<Duck> getDucks();
}
